// pages/login/login.js
const i18n = require('../../utils/i18n')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    locale: 'zh',
    isSelected: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    i18n.applyLocale(this)
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
  /**
   * 自定义方法：切换勾选状态
   */
  clickSelectView() {
    this.setData({
      isSelected: !this.data.isSelected
    });
  },

  /**
   * 自定义方法：未勾选协议时提示
   */
  clickLoginButton() {
    wx.showToast({ title: i18n.t('请先阅读并同意法律条款及隐私政策'), icon: 'none', duration: 2000 });
  },

  /**
   * 自定义方法：点击法律条款及隐私政策
   */
  clickLegalTerms() {
    wx.navigateTo({ url: '/pages/legal/legal' });
  },

  /**
   * 获取手机号授权回调（已勾选）
   */
  onGetPhoneNumber(e) {
    const { errMsg, code } = e.detail || {};
    if (errMsg === 'getPhoneNumber:ok' && code) {
      const { post } = require('../../utils/request')
      wx.showLoading({ title: i18n.t('登录中...'), mask: true })
      wx.login({
        success: (res) => {
          const loginCode = (res || {}).code
          if (!loginCode) {
            wx.hideLoading()
            wx.showToast({ title: i18n.t('获取登录码失败'), icon: 'none' })
            return
          }
          post('/miniapp/auth/login', { code: loginCode })
            .then((resp) => {
              const vo = resp.data || {}
              try {
                wx.setStorageSync('mini_token', vo.token || '')
                wx.setStorageSync('mini_token_expire', vo.expireTime || 0)
              } catch (e) {}
              return post('/miniapp/auth/phone', { code })
            })
            .then(() => {
              wx.hideLoading()
              wx.showToast({ title: i18n.t('登录成功'), icon: 'success' })
              setTimeout(() => {
                wx.redirectTo({ url: '/pages/index/index' })
              }, 500)
            })
            .catch((err) => {
              wx.hideLoading()
              const msg = (err && err.msg) || i18n.t('登录失败')
              wx.showToast({ title: msg, icon: 'none' })
            })
        },
        fail: () => {
          wx.hideLoading()
          wx.showToast({ title: i18n.t('登录失败'), icon: 'none' })
        }
      })
    } else if (errMsg === 'getPhoneNumber:fail user deny') {
      wx.showToast({
        title: i18n.t('需要授权手机号才能完成登录'),
        icon: 'none',
        duration: 2000
      });
    } else {
      wx.showToast({
        title: i18n.t('授权失败，请重试'),
        icon: 'none',
        duration: 2000
      });
    }
  }
  ,
  onLoginTap() {
    if (!this.data.isSelected) {
      wx.showToast({ title: i18n.t('请先阅读并同意法律条款及隐私政策'), icon: 'none' });
      return;
    }
    const { post } = require('../../utils/request')
    wx.showLoading({ title: i18n.t('登录中...'), mask: true })
    wx.login({
      success: (res) => {
        const code = (res || {}).code
        if (!code) {
          wx.hideLoading()
          wx.showToast({ title: i18n.t('获取登录码失败'), icon: 'none' })
          return
        }
        post('/miniapp/auth/login', { code })
          .then((resp) => {
            const vo = resp.data || {}
            try {
              wx.setStorageSync('mini_token', vo.token || '')
              wx.setStorageSync('mini_token_expire', vo.expireTime || 0)
            } catch (e) {}
            wx.hideLoading()
            wx.showToast({ title: i18n.t('登录成功'), icon: 'success' })
            setTimeout(() => {
              wx.redirectTo({ url: '/pages/index/index' })
            }, 500)
          })
          .catch((err) => {
            wx.hideLoading()
            const msg = (err && err.msg) || i18n.t('登录失败')
            wx.showToast({ title: msg, icon: 'none' })
          })
      },
      fail: () => {
        wx.hideLoading()
        wx.showToast({ title: i18n.t('登录失败'), icon: 'none' })
      }
    })
  }
})
