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
    this.fetchPackages()
  },

  onShow() {
    i18n.applyLocale(this)
  },

  fetchPackages() {
    this.setData({ loading: true })
    request({
      url: '/miniapp/servicePackage/list',
      method: 'GET'
    }).then(res => {
      if (res.code === 200) {
        const list = (res.data || []).map(item => ({
          ...item,
          priceYuan: (item.price / 100).toFixed(2),
          firstMonthPriceYuan: item.firstMonthPrice ? (item.firstMonthPrice / 100).toFixed(2) : null,
          typeName: item.durationType === 1 ? i18n.t('包月') : (item.durationType === 2 ? i18n.t('包年') : i18n.t('固定时长'))
        }))
        this.setData({
          packageList: list,
          loading: false
        })
      } else {
        this.setData({ loading: false })
      }
    }).catch(err => {
      console.error(err)
      this.setData({ loading: false })
    })
  },

  handleBuy(e) {
    const pkg = e.currentTarget.dataset.item
    
    // 跳转到确认订单页面
    wx.navigateTo({
      url: '/pages/takout/confirm/index?type=3',
      success: (res) => {
        // 传递数据到目标页面
        res.eventChannel.emit('acceptDataFromOpenerPage', {
          items: [{
            productId: pkg.id,
            productName: pkg.packageName,
            price: pkg.price,
            quantity: 1,
            imageUrl: pkg.imageUrl
          }],
          totalAmount: (pkg.price / 100).toFixed(2),
          storeId: null,
          type: 3 // 套餐
        })
      }
    })
  }
})
