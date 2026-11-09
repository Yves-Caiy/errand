const { request } = require('../../../utils/request')
const i18n = require('../../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    wechat: '',
    remark: ''
  },
  onLoad() {
    i18n.applyLocale(this)
  },
  onShow() {
    i18n.applyLocale(this)
  },
  onInput(e) {
    const key = e.currentTarget.dataset.key
    this.setData({ [key]: e.detail.value })
  },
  checkCommunityBinding() {
    return request({
      url: '/miniapp/user/profile'
    }).then(res => {
      if (res.code === 200) {
        const user = res.data || {}
        if (!user.communityId) {
          wx.showModal({
            title: i18n.t('提示'),
            content: i18n.t('请先绑定小区信息'),
            showCancel: false,
            success: () => {
              wx.navigateTo({ url: '/pages/me/address/index' })
            }
          })
          return false
        }
        return true
      }
      return false
    })
  },
  async onSubmit() {
    const bound = await this.checkCommunityBinding()
    if (!bound) return
    wx.showToast({ title: i18n.t('已提交'), icon: 'success' })
  },
  onBack() { wx.navigateBack() }
})
