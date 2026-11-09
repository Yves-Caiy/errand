// 模拟支付：不再调起真实微信支付，请求后端直接将订单标记为已支付
const { post } = require('./request')

/**
 * 模拟支付订单
 * @param {string} orderNo 订单号
 * @returns {Promise} resolve 支付成功，reject 支付失败/取消
 */
function mockPay(orderNo) {
  return new Promise((resolve, reject) => {
    if (!orderNo) {
      wx.showToast({ title: '订单号异常', icon: 'none' })
      reject(new Error('empty orderNo'))
      return
    }
    wx.showLoading({ title: '正在支付...', mask: true })
    post('/miniapp/pay/prepay', { orderNo }).then(res => {
      wx.hideLoading()
      if (res.code === 200) {
        wx.showToast({ title: '支付成功', icon: 'success' })
        resolve(res.data)
      } else {
        wx.showToast({ title: res.msg || '支付失败', icon: 'none' })
        reject(new Error(res.msg || '支付失败'))
      }
    }).catch(err => {
      wx.hideLoading()
      wx.showToast({ title: '网络异常', icon: 'none' })
      reject(err)
    })
  })
}

module.exports = { mockPay }
