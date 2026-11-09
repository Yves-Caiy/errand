// pages/takout/index.js
Page({
  data: {
    shops: []
  },

  onLoad() {
    this.loadStoresByCommunity()
  },

  onShow() {
    this.loadStoresByCommunity()
  },

  onTabChange(e) {
    const { key } = e.detail || {}
    if (key === 'index') {
      wx.redirectTo({ url: '/pages/index/index' })
      return
    } else if (key === 'orders') {
      wx.redirectTo({ url: '/pages/orders/index' })
      return
    } else if (key === 'me') {
      wx.redirectTo({ url: '/pages/me/index' })
      return
    }
    // 其他 Tab 暂未实现，给出提示
    wx.showToast({ title: `切换到 ${key}`, icon: 'none' })
  },
  onShopTap(e) {
    const id = e.currentTarget.dataset.id
    const shop = this.data.shops.find(s => s.id === id)
    if (shop) {
      const name = encodeURIComponent(shop.name)
      const desc = encodeURIComponent(`地址：${shop.address || '暂无'}  电话：${shop.contact || '暂无'}`)
      const avatar = encodeURIComponent(shop.avatarUrl || '')
      wx.navigateTo({ url: `/pages/takout/order/index?id=${id}&name=${name}&desc=${desc}&avatar=${avatar}` })
    }
  },

  loadStoresByCommunity() {
    let cid = ''
    try { cid = wx.getStorageSync('current_community_id') || '' } catch (e) {}
    const { request, BASE_URL } = require('../../utils/request')
    const params = {}
    if (cid) params.communityId = cid
    request({ url: '/miniapp/takeawayStore/list', method: 'GET', data: params })
      .then((resp) => {
        const list = Array.isArray(resp.data) ? resp.data : []
        const shops = list.map(s => ({
          id: s.id,
          name: s.storeName || '店铺',
          address: s.address,
          contact: s.contact,
          avatarUrl: s.avatarUrl && (s.avatarUrl.startsWith('http') ? s.avatarUrl : (BASE_URL + s.avatarUrl))
        }))
        this.setData({ shops })
      })
      .catch(() => {
        this.setData({ shops: [] })
      })
  }
})
