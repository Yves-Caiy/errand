// pages/orders/index.js
const i18n = require('../../utils/i18n')
Page({
  data: {
    locale: 'zh',
    activeStatus: 'all',
    statusLabel: {
      all: '全部',
      0: '待支付',
      1: '待接单',
      2: '已接单',
      3: '已完成',
      4: '已取消'
    },
    typeNameMap: {
      1: '外卖',
      2: '超市',
      3: '套餐'
    },
    orders: [],
    displayOrders: [],
    pageNum: 1,
    pageSize: 5,
    total: 0,
    hasMore: true,
    loading: false
  },

  onLoad() {
    i18n.applyLocale(this)
    this.loadOrders(true)
  },

  onShow() {
    i18n.applyLocale(this)
    this.loadOrders(true)
  },

  onTabsChange(e) {
    const name = (e && e.detail && (e.detail.name !== undefined ? e.detail.name : e.detail))
    const index = e && e.detail && e.detail.index
    let next = this.data.activeStatus
    if (name !== undefined) next = name
    else if (index !== undefined) {
      const map = ['all', '0', '1', '2', '3', '4']
      next = map[index] || 'all'
    }
    this.setData({ activeStatus: next })
    this.loadOrders(true)
  },

  loadOrders(reset = false) {
    if (this.data.loading) return
    if (!reset && !this.data.hasMore) return

    this.setData({ loading: true })
    if (reset) {
      this.setData({ pageNum: 1, orders: [], displayOrders: [], hasMore: true })
    }

    const { request } = require('../../utils/request')
    const status = this.data.activeStatus
    const params = {
      pageNum: reset ? 1 : this.data.pageNum,
      pageSize: this.data.pageSize
    }
    if (status !== 'all') params.orderStatus = Number(status)
    
    request({ url: '/miniapp/order/myList', method: 'GET', data: params })
      .then((resp) => {
        const list = resp.rows || []
        const total = resp.total || 0
        
        const display = list.map(o => {
          const orderType = o.orderType
          const showRunner = o.orderStatus !== 1 && !!o.runnerNickname
          const cardTag = orderType === 4 ? (o.serviceType || '其他服务') : (this.data.typeNameMap[orderType] || `类型${orderType}`)
          const actual = (o.actualAmount != null ? o.actualAmount : (o.budget || 0))
          return {
            id: o.id,
            orderNo: o.orderNo,
            cardTag,
            orderType,
            status: o.orderStatus,
            runnerNickname: showRunner ? o.runnerNickname : '',
            communityName: o.communityName || '',
            detailAddress: o.detailAddress || '',
            startAddress: o.startAddress || '',
            endAddress: o.endAddress || '',
            itemDesc: o.itemDesc || '',
            budget: o.budget != null ? (o.budget / 100).toFixed(2) : '',
            specName: o.specName || '',
            wechatId: o.wechatId || '',
            actualAmount: (actual / 100).toFixed(2),
            payStatus: o.payStatus,
            payTime: o.payTime || '',
            finishTime: o.finishTime || '',
            cancelReason: o.cancelReason || '',
            note: o.note || '',
            createTime: o.createTime || ''
          }
        })
        
        const currentList = this.data.displayOrders
        const newList = reset ? display : currentList.concat(display)
        
        this.setData({ 
          orders: newList, 
          displayOrders: newList,
          total: total,
          hasMore: newList.length < total,
          pageNum: (reset ? 1 : this.data.pageNum) + 1,
          loading: false
        })
      })
      .catch(() => {
        this.setData({ loading: false })
        if (reset) this.setData({ orders: [], displayOrders: [] })
      })
  },

  onLoadMore() {
    this.loadOrders(false)
  },

  onTabChange(e) {
    const { key } = e.detail || {}
    if (key === 'index') {
      wx.redirectTo({ url: '/pages/index/index' })
      return
    } else if (key === 'takeout') {
      wx.redirectTo({ url: '/pages/takout/index' })
      return
    } else if (key === 'me') {
      wx.redirectTo({ url: '/pages/me/index' })
      return
    }
    wx.showToast({ title: `切换到 ${key}`, icon: 'none' })
  },

  onPayOrder(e) {
    const orderNo = e.currentTarget.dataset.orderNo
    if (!orderNo) return

    const { mockPay } = require('../../utils/pay')
    mockPay(orderNo).then(() => {
      setTimeout(() => this.loadOrders(true), 800)
    }).catch(() => {})
  },

  onFinishOrder(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: i18n.t('提示'),
      content: i18n.t('确认服务已完成吗？'),
      success: (res) => {
        if (res.confirm) {
          const { request } = require('../../utils/request')
          request({
            url: '/miniapp/order/complete',
            method: 'PUT',
            data: { id: id },
            header: { 'content-type': 'application/x-www-form-urlencoded' }
          }).then(res => {
            if (res.code === 200) {
              wx.showToast({ title: i18n.t('操作成功'), icon: 'success' })
              this.loadOrders(true)
            } else {
              wx.showToast({ title: res.msg || i18n.t('操作失败'), icon: 'none' })
            }
          })
        }
      }
    })
  },

  onCancelOrder(e) {
    const id = e.currentTarget.dataset.id
    const payStatus = e.currentTarget.dataset.payStatus
    
    if (Number(payStatus) !== 0) {
      wx.showToast({ title: i18n.t('当前订单状态不可取消'), icon: 'none' })
      return
    }

    wx.showModal({
      title: i18n.t('提示'),
      content: i18n.t('确定要取消订单吗？'),
      success: (res) => {
        if (res.confirm) {
          const { request } = require('../../utils/request')
          request({
              url: '/miniapp/order/cancel',
              method: 'PUT',
              data: { id: id, reason: '用户主动取消' },
              header: { 'content-type': 'application/x-www-form-urlencoded' }
          }).then(res => {
              if (res.code === 200) {
                   wx.showToast({ title: i18n.t('取消成功'), icon: 'success' })
                   this.loadOrders(true)
              } else {
                   wx.showToast({ title: res.msg || i18n.t('取消失败'), icon: 'none' })
              }
          })
        }
      }
    })
  },

  onRefundOrder(e) {
    const id = e.currentTarget.dataset.id
    const payStatus = e.currentTarget.dataset.payStatus

    if (Number(payStatus) !== 1) {
      wx.showToast({ title: i18n.t('当前订单状态不可申请退款'), icon: 'none' })
      return
    }

    wx.showModal({
      title: i18n.t('提示'),
      content: i18n.t('确定要申请退款吗？'),
      editable: true,
      placeholderText: i18n.t('请输入退款原因'),
      success: (res) => {
        if (res.confirm) {
          const reason = res.content || '用户申请退款'
          const { request } = require('../../utils/request')
          request({
              url: '/miniapp/order/refund',
              method: 'PUT',
              data: { id: id, reason: reason },
              header: { 'content-type': 'application/x-www-form-urlencoded' }
          }).then(res => {
              if (res.code === 200) {
                   wx.showToast({ title: i18n.t('申请成功'), icon: 'success' })
                   this.loadOrders()
              } else {
                   wx.showToast({ title: res.msg || i18n.t('申请失败'), icon: 'none' })
              }
          })
        }
      }
    })
  },

  onViewDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/orders/detail/index?id=${id}`
    })
  }
})
