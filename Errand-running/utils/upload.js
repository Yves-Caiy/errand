// 统一上传工具：头像、图片、文件都走 /miniapp/upload，
// 后端保存到服务器本地磁盘并返回 http 格式的访问地址。
const { BASE_URL } = require('./request')

function getToken() {
  try {
    return wx.getStorageSync('mini_token') || ''
  } catch (e) {
    return ''
  }
}

/**
 * 上传单个文件到服务端本地
 * @param {string} filePath 本地临时文件路径
 * @param {object} options 可选：{ name, formData, showLoading, loadingText }
 * @returns {Promise<string>} 服务端返回的 http 地址
 */
function uploadFile(filePath, options) {
  const opts = options || {}
  const name = opts.name || 'file'
  const formData = opts.formData || {}
  const showLoading = !!opts.showLoading
  const loadingText = opts.loadingText || '上传中...'

  if (showLoading) {
    wx.showLoading({ title: loadingText, mask: true })
  }

  return new Promise((resolve, reject) => {
    wx.uploadFile({
      url: `${BASE_URL}/miniapp/upload`,
      filePath: filePath,
      name: name,
      formData: formData,
      header: {
        'Mini-Token': getToken()
      },
      success: (res) => {
        if (showLoading) wx.hideLoading()
        let data = {}
        try {
          data = JSON.parse(res.data || '{}')
        } catch (e) {
          reject({ msg: '上传返回值解析失败' })
          return
        }
        if (data.code === 200 && data.url) {
          resolve(data.url)
        } else {
          reject(data)
        }
      },
      fail: (err) => {
        if (showLoading) wx.hideLoading()
        reject(err)
      }
    })
  })
}

module.exports = { uploadFile }
