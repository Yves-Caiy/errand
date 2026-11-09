// 后端地址：本地运行用 http://localhost:8080，线上用 https://xiaoyuzi.top
const BASE_URL = 'http://localhost:8080'

function getToken() {
  try {
    return wx.getStorageSync('mini_token') || ''
  } catch (e) {
    return ''
  }
}

function request(options) {
  const { url, method = 'GET', data = {}, header = {} } = options || {}
  const token = getToken()
  const finalHeader = Object.assign({ 'Mini-Token': token }, header)
  return new Promise((resolve, reject) => {
    wx.request({
      url: url.startsWith('http') ? url : BASE_URL + url,
      method,
      data,
      header: finalHeader,
      success: (res) => {
        if (res && res.data && res.data.code === 200) {
          resolve(res.data)
        } else if (res && res.data && (res.data.code === 401 || (res.data.code === 500 && res.data.msg === '未登录或登录已过期'))) {
          wx.removeStorageSync('mini_token')
          wx.showToast({ title: '未登录或登录过期', icon: 'none' })
          setTimeout(() => {
            wx.navigateTo({ url: '/pages/login/login' })
          }, 1500)
          reject(res.data)
        } else {
          reject(res.data || res)
        }
      },
      fail: reject
    })
  })
}

function post(url, data) {
  return request({ url, method: 'POST', data, header: { 'Content-Type': 'application/json' } })
}

module.exports = { request, post, BASE_URL }
