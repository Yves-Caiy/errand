const { request } = require('../../../utils/request')
const i18n = require('../../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    address: '',
    loading: false,
    communities: [],
    communityIndex: -1,
    selectedCommunityId: null
  },

  onLoad(options) {
    i18n.applyLocale(this)
    this.fetchCommunities().then(() => {
      this.fetchUserProfile()
    })
  },

  onShow() {
    i18n.applyLocale(this)
  },

  fetchCommunities() {
    const { request } = require('../../../utils/request')
    return request({
      url: '/miniapp/community/list',
      method: 'GET'
    }).then(res => {
      if (res.code === 200) {
        this.setData({
          communities: res.data || []
        })
      }
    })
  },

  fetchUserProfile() {
    const { request } = require('../../../utils/request')
    request({
      url: '/miniapp/user/profile',
      method: 'GET'
    }).then(res => {
      if (res.code === 200) {
        const profile = res.data
        let idx = -1
        if (profile.communityId && this.data.communities.length > 0) {
          idx = this.data.communities.findIndex(c => c.id === profile.communityId)
        }
        
        this.setData({
          address: profile.detailAddress || '',
          selectedCommunityId: profile.communityId || null,
          communityIndex: idx
        })
      }
    })
  },

  onCommunityChange(e) {
    const idx = Number(e.detail.value)
    const community = this.data.communities[idx]
    this.setData({
      communityIndex: idx,
      selectedCommunityId: community ? community.id : null
    })
  },

  handleInput(e) {
    this.setData({
      address: e.detail.value
    })
  },

  handleSave() {
    if (!this.data.selectedCommunityId) {
      wx.showToast({ title: i18n.t('请选择小区'), icon: 'none' })
      return
    }
    if (!this.data.address.trim()) {
      wx.showToast({ title: i18n.t('请输入详细地址'), icon: 'none' })
      return
    }

    this.setData({ loading: true })
    const { request } = require('../../../utils/request')
    request({
      url: '/miniapp/user/profile',
      method: 'PUT',
      data: {
        detailAddress: this.data.address,
        communityId: this.data.selectedCommunityId
      }
    }).then(res => {
      this.setData({ loading: false })
      if (res.code === 200) {
        wx.showToast({
          title: i18n.t('保存成功'),
          icon: 'success'
        })
        setTimeout(() => {
          wx.navigateBack()
        }, 1500)
      } else {
        wx.showToast({
          title: res.msg || i18n.t('保存失败'),
          icon: 'none'
        })
      }
    }).catch(err => {
      this.setData({ loading: false })
      wx.showToast({
        title: i18n.t('网络错误'),
        icon: 'none'
      })
    })
  }
})
