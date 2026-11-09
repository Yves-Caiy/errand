// pages/index/index.js
const i18n = require('../../utils/i18n')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    locale: 'zh',
    pageTitle: '首页',
    productTypes: [],
    carouselImages: [],
    noticeContent: '',
    noticeDuration: 10
  },

  onTabChange(e) {
    const { key } = e.detail || {}
    if (key === 'takeout') {
      wx.redirectTo({ url: '/pages/takout/index' })
      return
    } else if (key === 'orders') {
      wx.redirectTo({ url: '/pages/orders/index' })
      return
    } else if (key === 'me') {
      wx.redirectTo({ url: '/pages/me/index' })
      return
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    i18n.applyLocale(this)
    this.loadProductTypesByStoredCommunity()
    this.loadCarousel()
    this.loadNotice()
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    i18n.applyLocale(this)
    this.loadProductTypesByStoredCommunity()
    this.loadCarousel()
    this.loadNotice()
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
  ,
  onCommunityChange(e) {
    const { id } = e.detail || {}
    if (!id) {
      this.setData({ productTypes: [] })
      return
    }
    const { request } = require('../../utils/request')
    request({ url: '/miniapp/productType/list', method: 'GET', data: { communityId: id } })
      .then((resp) => {
        const list = resp.data || []
        this.setData({ productTypes: list })
      })
      .catch(() => {
        this.setData({ productTypes: [] })
      })
  }
  ,
  loadProductTypesByStoredCommunity() {
    let cid = ''
    try { cid = wx.getStorageSync('current_community_id') || '' } catch (e) {}
    if (!cid) { this.setData({ productTypes: [] }); return }
    const { request } = require('../../utils/request')
    request({ url: '/miniapp/productType/list', method: 'GET', data: { communityId: cid } })
      .then((resp) => {
        const list = resp.data || []
        this.setData({ productTypes: list })
      })
      .catch(() => {
        this.setData({ productTypes: [] })
      })
  }
  ,
  loadCarousel() {
    const { request, BASE_URL } = require('../../utils/request')
    request({ url: '/miniapp/config/carousel', method: 'GET' })
      .then((resp) => {
        const items = Array.isArray(resp.data) ? resp.data : []
        const imgs = items
          .map(it => {
            const url = it && it.imageUrl
            if (!url) return ''
            return url.startsWith('http') ? url : (BASE_URL + url)
          })
          .filter(Boolean)
        this.setData({ carouselImages: imgs })
      })
      .catch(() => {
        this.setData({ carouselImages: [] })
      })
  },
  loadNotice() {
    const { request } = require('../../utils/request')
    request({ url: '/miniapp/config/notice', method: 'GET' })
      .then((resp) => {
        const notice = resp.data || ''
        if (!notice) {
          this.setData({ noticeContent: '' })
          return
        }
        const apply = (text) => {
          // Calculate duration based on text length, assuming ~50px per second (adjust as needed)
          // Rough estimate: 1 char ~ 14px.
          const length = text.length * 14
          // Screen width ~375px (750rpx).
          // We want it to scroll across. Duration = (TextWidth + ScreenWidth) / Speed
          const duration = (length + 375) / 50
          this.setData({ noticeContent: text, noticeDuration: duration })
        }
        // 公告为后端动态下发的中文，切到韩语时按当前语言翻译后展示
        if (i18n.isKo()) {
          i18n.translate(notice, 'zh', 'ko').then((list) => apply((list && list[0]) || notice))
        } else {
          apply(notice)
        }
      })
      .catch(() => {
        this.setData({ noticeContent: '' })
      })
  }
})
