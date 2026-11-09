const i18n = require('../../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    orders: [],
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
  },

  onShow() {
    i18n.applyLocale(this)
    this.loadOrders()
  },

  onPullDownRefresh() {
    this.loadOrders()
  },

  loadOrders() {
    if (this.data.loading) return
    this.setData({ loading: true })

    const { request } = require('../../../utils/request')
    
    // 调用获取外卖待接单列表接口
    request({
      url: '/miniapp/order/takeoutList',
      method: 'GET'
    }).then(res => {
      wx.stopPullDownRefresh()
      const list = res.data || []
      
      const display = list.map(o => {
        const orderType = o.orderType
        // 外卖单通常显示：商家名(或起始地) -> 目的地
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
