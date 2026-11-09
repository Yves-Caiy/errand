const i18n = require('../../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    orders: [],
    dispatchList: [],
    loading: false,
    statusLabel: {
      0: '待支付',
      1: '待接单',
      2: '已接单',
      3: '已完成',
      4: '已取消'
    },
    typeNameMap: {
      1: '外卖',
      2: '超市',
      3: '套餐',
      4: '其他'
    }
  },

  onLoad() {
    i18n.applyLocale(this)
    this.loadOrders()
    this.loadDispatch()
  },

  onShow() {
    i18n.applyLocale(this)
    this.loadOrders()
    this.loadDispatch()
  },

  onHide() {
    this.clearDispatchTimer()
  },

  onUnload() {
    this.clearDispatchTimer()
  },

  onPullDownRefresh() {
    this.loadOrders()
    this.loadDispatch()
    wx.stopPullDownRefresh()
  },

  // -------- 指派给我（智能派单） --------

  loadDispatch() {
    const { request } = require('../../../utils/request')
    request({
      url: '/miniapp/runner/dispatch/pending',
      method: 'GET'
    }).then(res => {
      const list = (res.data || []).map(d => {
        const remain = Math.max(0, Number(d.remainSeconds) || 0)
        const amount = d.actualAmount != null ? d.actualAmount : (d.commissionAmount || 0)
        return {
          orderId: d.orderId,
          orderNo: d.orderNo,
          communityName: d.communityName || '',
          detailAddress: d.detailAddress || '',
          note: d.note || '',
          dispatchRound: d.dispatchRound,
          score: d.score,
          remainSeconds: remain,
          remainText: this.formatRemain(remain),
          amount: (amount / 100).toFixed(2)
        }
      })
      this.setData({ dispatchList: list })
      this.startDispatchTimer()
    }).catch(() => {
      // 非接单员或未开启派单时无数据，静默处理
      this.setData({ dispatchList: [] })
      this.clearDispatchTimer()
    })
  },

  formatRemain(sec) {
    const s = Math.max(0, Number(sec) || 0)
    const m = Math.floor(s / 60)
    const r = s % 60
    return (m < 10 ? '0' + m : m) + ':' + (r < 10 ? '0' + r : r)
  },

  startDispatchTimer() {
    this.clearDispatchTimer()
    if (!this.data.dispatchList.length) return
    this._dispatchTimer = setInterval(() => {
      const list = this.data.dispatchList.map(i => {
        const remain = Math.max(0, (i.remainSeconds || 0) - 1)
        return Object.assign({}, i, { remainSeconds: remain, remainText: this.formatRemain(remain) })
      })
      this.setData({ dispatchList: list })
      if (list.some(i => i.remainSeconds <= 0)) {
        this.clearDispatchTimer()
        this.loadDispatch()
      }
    }, 1000)
  },

  clearDispatchTimer() {
    if (this._dispatchTimer) {
      clearInterval(this._dispatchTimer)
      this._dispatchTimer = null
    }
  },

  onAcceptDispatch(e) {
    const orderId = e.currentTarget.dataset.id
    const { request } = require('../../../utils/request')
    wx.showLoading({ title: i18n.t('接单中...') })
    request({
      url: '/miniapp/runner/dispatch/accept',
      method: 'POST',
      data: { orderId: orderId },
      header: { 'content-type': 'application/x-www-form-urlencoded' }
    }).then(() => {
      wx.hideLoading()
      wx.showToast({ title: i18n.t('接单成功'), icon: 'success' })
      this.loadDispatch()
      this.loadOrders()
    }).catch(res => {
      wx.hideLoading()
      wx.showToast({ title: (res && res.msg) || i18n.t('接单失败'), icon: 'none' })
      this.loadDispatch()
    })
  },

  onRejectDispatch(e) {
    const orderId = e.currentTarget.dataset.id
    wx.showModal({
      title: i18n.t('提示'),
      content: i18n.t('确定拒绝该派单吗？'),
      success: (res) => {
        if (!res.confirm) return
        const { request } = require('../../../utils/request')
        request({
          url: '/miniapp/runner/dispatch/reject',
          method: 'POST',
          data: { orderId: orderId, reason: '接单员拒绝' },
          header: { 'content-type': 'application/x-www-form-urlencoded' }
        }).then(() => {
          wx.showToast({ title: i18n.t('已拒绝'), icon: 'none' })
          this.loadDispatch()
        }).catch(() => {
          wx.showToast({ title: i18n.t('操作失败'), icon: 'none' })
        })
      }
    })
  },

  loadOrders() {
    if (this.data.loading) return
    this.setData({ loading: true })

    const { request } = require('../../../utils/request')
    
    // 调用获取超市跑腿待接单列表接口
    request({
      url: '/miniapp/order/supermarketList',
      method: 'GET'
    }).then(res => {
      wx.stopPullDownRefresh()
      const list = res.data || []
      
      const display = list.map(o => {
        const orderType = o.orderType
        // 超市单通常显示：超市名(或起始地) -> 目的地
        const cardTag = this.data.typeNameMap[orderType] || `类型${orderType}`
        const actual = (o.actualAmount != null ? o.actualAmount : (o.budget || 0))
        
        return {
          id: o.id,
          orderNo: o.orderNo,
          cardTag,
          orderType,
          status: o.orderStatus,
          payStatus: o.payStatus,
          
          communityName: o.communityName || '',
          detailAddress: o.detailAddress || '',
          
          // 外卖/超市单特有字段
          startAddress: o.startAddress || '', // 商家地址/取货地
          endAddress: o.endAddress || '',     // 送货地
          
          itemDesc: o.itemDesc || '',
          budget: o.budget != null ? (o.budget / 100).toFixed(2) : '',
          specName: o.specName || '',
          
          actualAmount: (actual / 100).toFixed(2),
          createTime: o.createTime || '',
          note: o.note || ''
        }
      })

      this.setData({
        orders: display,
        loading: false
      })
    }).catch(() => {
      wx.stopPullDownRefresh()
      this.setData({ loading: false })
      wx.showToast({ title: i18n.t('加载失败'), icon: 'none' })
    })
  },

  // 跳转详情
  onViewDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/orders/detail/index?id=${id}`
    })
  },

  // 接单
  onAcceptOrder(e) {
    const id = e.currentTarget.dataset.id
    
    wx.showModal({
      title: i18n.t('提示'),
      content: i18n.t('确定要接下此单吗？'),
      success: (res) => {
        if (res.confirm) {
          const { request } = require('../../../utils/request')
          wx.showLoading({ title: i18n.t('接单中...') })
          
          request({
            url: '/miniapp/order/accept',
            method: 'PUT',
            data: { id: id },
            header: { 'content-type': 'application/x-www-form-urlencoded' }
          }).then(res => {
            wx.hideLoading()
            if (res.code === 200) {
              wx.showToast({ title: i18n.t('接单成功'), icon: 'success' })
              // 刷新列表
              this.loadOrders()
            } else {
              wx.showToast({ title: res.msg || i18n.t('接单失败'), icon: 'none' })
            }
          }).catch(() => {
            wx.hideLoading()
            wx.showToast({ title: i18n.t('网络异常'), icon: 'none' })
          })
        }
      }
    })
  }
})
