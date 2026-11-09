const { request } = require('../../../utils/request')
const i18n = require('../../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    packageList: [],
    loading: true
  },

  onLoad(options) {
    i18n.applyLocale(this)
    this.fetchMyPackages()
  },

  onShow() {
    i18n.applyLocale(this)
  },

  fetchMyPackages() {
    this.setData({ loading: true })
    request({
      url: '/miniapp/servicePackage/myList',
      method: 'GET'
    }).then(res => {
      if (res.code === 200) {
        this.setData({
          packageList: res.data,
          loading: false
        })
      } else {
        this.setData({ loading: false })
      }
    }).catch(err => {
      console.error(err)
      this.setData({ loading: false })
    })
  }
})
