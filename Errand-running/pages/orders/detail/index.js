const app = getApp()
const { request } = require('../../../utils/request')
const i18n = require('../../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    order: {},
    orderStatusText: '',
    payStatusText: ''
  },

  onLoad(options) {
    i18n.applyLocale(this)
    if (options.id) {
      this.getOrderDetail(options.id)
    }
  },

  onShow() {
    i18n.applyLocale(this)
  },

  getOrderDetail(id) {
    request({
      url: `/miniapp/order/detail/${id}`,
      method: 'GET'
    }).then(res => {
      if (res.code === 200) {
        // Format prices if needed or just use helpers in wxml
        this.setData({
          order: res.data,
          orderStatusText: i18n.t(this.getOrderStatusText(res.data.orderStatus)),
          payStatusText: i18n.t(this.getPayStatusText(res.data.payStatus))
        })
      } else {
        wx.showToast({ title: res.msg || i18n.t('获取详情失败'), icon: 'none' })
      }
    })
  },

  getOrderStatusText(status) {
    const map = {
      0: '待接单', // Assuming 0 is pending payment/dispatch, usually 0 is pending payment
      1: '待接单', // Usually paid, waiting for runner
      2: '配送中',
      3: '已完成',
      4: '已取消',
      5: '已退款'
    }
    // Adjust based on actual business logic if needed. 
    // Based on MiniappOrderController: 0=待支付? 
    // Let's check typical status. 
    // In OrderMain: 0=待支付, 1=待接单, 2=已接单/配送中, 3=已完成, 4=已取消, 5=已退款
    const statusMap = {
        0: '待支付',
        1: '待接单',
        2: '配送中',
        3: '已完成',
        4: '已取消',
        5: '已退款'
    }
    return statusMap[status] || '未知状态'
  },

  getPayStatusText(status) {
    const map = {
      0: '未支付',
      1: '已支付',
      2: '退款中',
      3: '已退款'
    }
    return map[status] || ''
  }
})