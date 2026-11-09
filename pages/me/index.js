const { request, post } = require('../../utils/request')
const { uploadFile } = require('../../utils/upload')
const i18n = require('../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    isLoggedIn: false,
    userInfo: {
      avatarUrl: '',
      nickName: '',
      community: '阳光小区', // 默认或示例
      commissionTotal: '128.50',
      commissionPending: '25.00',
      commissionSettled: '103.50'
    },
    withdrawRate: 0,
    showEditModal: false,
    tempNickName: '',
    noticeList: []
  },

  onLoad() {
    i18n.applyLocale(this)
    this.checkLoginStatus()
    this.fetchWithdrawRate()
  },
  
  onShow() {
    i18n.applyLocale(this)
    this.checkLoginStatus()
    this.loadNotices()
  },

  onHide() {
    this.clearNoticeTimer()
  },

  onUnload() {
    this.clearNoticeTimer()
  },

  // -------- 来单提醒（未读智能派单 / 系统指派） --------

  loadNotices() {
    if (!wx.getStorageSync('mini_token')) {
      this.clearNoticeTimer()
      this.setData({ noticeList: [] })
      return
    }
    request({
      url: '/miniapp/runner/notice/list',
      method: 'GET'
    }).then(res => {
      const raw = res && res.data ? (res.data.items || []) : []
      const list = raw.map(it => {
        const amount = it.actualAmount != null ? it.actualAmount : (it.commissionAmount || 0)
        const remain = Math.max(0, Number(it.remainSeconds) || 0)
        return Object.assign({}, it, {
          amountText: ((Number(amount) || 0) / 100).toFixed(2),
          remainSeconds: remain,
          remainText: this.formatRemain(remain)
        })
      })
      this.setData({ noticeList: list })
      this.startNoticeTimer()
    }).catch(() => {
      // 非接单员或接口异常时不显示提醒区块
      this.clearNoticeTimer()
      this.setData({ noticeList: [] })
    })
  },

  formatRemain(sec) {
    const s = Math.max(0, Number(sec) || 0)
    const m = Math.floor(s / 60)
    const r = s % 60
    return (m < 10 ? '0' + m : m) + ':' + (r < 10 ? '0' + r : r)
  },

  startNoticeTimer() {
    this.clearNoticeTimer()
    if (!this.data.noticeList.some(i => i.type === 'push')) return
    this._noticeTimer = setInterval(() => {
      const list = this.data.noticeList.map(i => {
        if (i.type !== 'push') return i
        const remain = Math.max(0, (i.remainSeconds || 0) - 1)
        return Object.assign({}, i, { remainSeconds: remain, remainText: this.formatRemain(remain) })
      })
      this.setData({ noticeList: list })
      if (list.some(i => i.remainSeconds <= 0)) {
        this.clearNoticeTimer()
        this.loadNotices()
      }
    }, 1000)
  },

  clearNoticeTimer() {
    if (this._noticeTimer) {
      clearInterval(this._noticeTimer)
      this._noticeTimer = null
    }
  },

  // 标记已读：logIds 为空表示全部已读
  markNoticeRead(logIds) {
    post('/miniapp/runner/notice/read', logIds && logIds.length ? { logIds } : {}).catch(() => {})
  },

  onNoticeAccept(e) {
    const orderId = e.currentTarget.dataset.orderid
    wx.showLoading({ title: i18n.t('接单中...') })
    request({
      url: '/miniapp/runner/dispatch/accept',
      method: 'POST',
      data: { orderId },
      header: { 'content-type': 'application/x-www-form-urlencoded' }
    }).then(() => {
      wx.hideLoading()
      wx.showToast({ title: i18n.t('接单成功'), icon: 'success' })
      this.loadNotices()
    }).catch(res => {
      wx.hideLoading()
      wx.showToast({ title: (res && res.msg) || i18n.t('接单失败'), icon: 'none' })
      this.loadNotices()
    })
  },

  onNoticeReject(e) {
    const orderId = e.currentTarget.dataset.orderid
    wx.showModal({
      title: i18n.t('提示'),
      content: i18n.t('确定拒绝该派单吗？'),
      success: (res) => {
        if (!res.confirm) return
        request({
          url: '/miniapp/runner/dispatch/reject',
          method: 'POST',
          data: { orderId, reason: '接单员拒绝' },
          header: { 'content-type': 'application/x-www-form-urlencoded' }
        }).then(() => {
          wx.showToast({ title: i18n.t('已拒绝'), icon: 'none' })
          this.loadNotices()
        }).catch(() => {
          wx.showToast({ title: i18n.t('操作失败'), icon: 'none' })
        })
      }
    })
  },

  onNoticeDetail(e) {
    const orderId = e.currentTarget.dataset.orderid
    const item = this.data.noticeList.find(i => i.orderId === orderId)
    if (item && item.logId) {
      this.markNoticeRead([item.logId])
    }
    wx.navigateTo({ url: '/pages/orders/detail/index?id=' + orderId })
  },

  fetchWithdrawRate() {
    request({
      url: '/miniapp/config/withdrawRate',
      method: 'GET'
    }).then(res => {
      if (res.code === 200) {
        this.setData({ withdrawRate: res.data })
      }
    })
  },

  checkLoginStatus() {
    const token = wx.getStorageSync('mini_token')
    const isLoggedIn = !!token
    this.setData({ isLoggedIn })
    if (isLoggedIn) {
      this.fetchUserProfile()
    } else {
      // 未登录，清除显示信息，保留默认值
      this.setData({
        userInfo: {
          avatarUrl: '',
          nickName: '',
          community: '阳光小区',
          commissionTotal: '0.00',
          commissionPending: '0.00',
          commissionSettled: '0.00'
        }
      })
    }
  },

  // 获取用户信息
  fetchUserProfile() {
    request({
      url: '/miniapp/user/profile',
      method: 'GET'
    }).then(res => {
      if (res.code === 200) {
        const user = res.data
        const rawAvatar = user.avatarUrl || ''
        const avatarUrl = rawAvatar.trim().replace(/^`|`$/g, '')
        this.setData({
          userInfo: {
            avatarUrl: avatarUrl,
            nickName: user.nickname,
            community: user.communityName || i18n.t('未绑定小区'),
            commissionTotal: (user.totalCommission / 100).toFixed(2),
            commissionPending: (user.pendingCommission / 100).toFixed(2),
            commissionSettled: (user.settledCommission / 100).toFixed(2),
            isAdmin: user.isAdmin
          }
        })
        this.saveUserToStorage()
      }
    }).catch(err => {
      console.error('获取用户信息失败', err)
    })
  },

  loadUserInfo() {
    // 此方法主要用于从缓存加载，现在优先使用 fetchUserProfile
    // 保留作为 fallback 或初始加载
    try {
      const user = wx.getStorageSync('user') || {}
      // 合并默认数据与缓存数据
      this.setData({
        userInfo: {
          ...this.data.userInfo,
          ...user,
          // 确保佣金数据存在（如果缓存没有则用默认/Mock）
          commissionTotal: user.commissionTotal || '128.50',
          commissionPending: user.commissionPending || '25.00',
          commissionSettled: user.commissionSettled || '103.50',
          community: user.community || '阳光小区'
        }
      })
    } catch (e) {
      console.error('加载用户信息失败', e)
    }
  },

  onGoToLogin() {
    wx.navigateTo({ url: '/pages/login/login' })
  },

  // 头像选择
  onChooseAvatar(e) {
    const { avatarUrl } = e.detail
    // 先显示临时头像
    this.setData({
      'userInfo.avatarUrl': avatarUrl
    })

    // 统一上传：保存到服务器本地，返回 http 地址
    uploadFile(avatarUrl, { showLoading: true, loadingText: i18n.t('上传中...') }).then((url) => {
      // 上传成功，保存返回的 http 地址
      this.updateUserProfile({ avatarUrl: url })
    }).catch(() => {
      wx.showToast({ title: i18n.t('头像上传失败'), icon: 'none' })
    })
  },

  // 显示修改昵称弹窗
  showEditModal() {
    this.setData({
      showEditModal: true,
      tempNickName: this.data.userInfo.nickName || ''
    })
  },

  // 隐藏弹窗
  hideEditModal() {
    this.setData({
      showEditModal: false
    })
  },

  // 昵称输入监听
  onNickNameInput(e) {
    this.setData({
      tempNickName: e.detail.value
    })
  },
  
  // 昵称改变（用于键盘回填）
  onNickNameChange(e) {
    this.setData({
      tempNickName: e.detail.value
    })
  },

  // 保存昵称
  saveNickName() {
    const name = this.data.tempNickName
    if (!name || !name.trim()) {
      wx.showToast({ title: i18n.t('昵称不能为空'), icon: 'none' })
      return
    }
    
    this.setData({
      showEditModal: false
    })
    
    this.updateUserProfile({ nickname: name })
  },

  // 更新用户信息到后端
  updateUserProfile(profileData) {
    wx.showLoading({ title: i18n.t('更新中') })
    request({
      url: '/miniapp/user/profile',
      method: 'PUT',
      data: profileData
    }).then((res) => {
      wx.hideLoading()
      if (res.code === 200) {
        // 更新本地数据
        // 注意：本地userInfo使用的是 nickName (驼峰)，而后端DTO是 nickname
        // 需要做个映射或者直接合并时小心
        const localUpdates = {}
        if (profileData.nickname) localUpdates.nickName = profileData.nickname
        if (profileData.avatarUrl) localUpdates.avatarUrl = profileData.avatarUrl
        
        this.setData({
          'userInfo': { ...this.data.userInfo, ...localUpdates }
        })
        this.saveUserToStorage()
        wx.showToast({ title: i18n.t('更新成功'), icon: 'success' })
      } else {
        wx.showToast({ title: res.msg || i18n.t('更新失败'), icon: 'none' })
      }
    }).catch((err) => {
      wx.hideLoading()
      // request.js 会处理 401/500-unlogged，其他错误在这里捕获
      console.error('更新用户信息失败', err)
      // 如果没有被全局拦截处理，显示提示
      if (!err || (err.code !== 401 && !(err.code === 500 && err.msg === '未登录或登录已过期'))) {
        wx.showToast({ title: i18n.t('网络错误'), icon: 'none' })
      }
    })
  },

  // 保存用户信息到本地缓存
  saveUserToStorage() {
    try {
      wx.setStorageSync('user', this.data.userInfo)
    } catch (e) {
      console.error('保存用户信息失败', e)
    }
  },

  // 阻止冒泡
  stopProp() {},

  // 导航方法
  onAcceptedOrders() {
    wx.navigateTo({ url: '/pages/orders/accepted/index' })
  },

  onAddressSettings() {
    wx.navigateTo({ url: '/pages/me/address/index' })
  },

  onBuyPackage() {
    wx.navigateTo({ url: '/pages/package/buy/index' })
  },

  onMyPackages() {
    wx.navigateTo({ url: '/pages/package/my/index' })
  },

  onWithdrawRecords() {
    wx.navigateTo({ url: '/pages/me/withdrawRecords/index' })
  },

  onTakeoutOrders() {
    wx.navigateTo({ url: '/pages/orders/takeout/index' })
  },

  onMarketOrders() {
    wx.navigateTo({ url: '/pages/orders/market/index' })
  },

  // 中韩语言切换
  onSwitchLanguage() {
    const current = i18n.getLocale()
    wx.showActionSheet({
      itemList: ['简体中文', '한국어'],
      success: (res) => {
        const target = res.tapIndex === 1 ? 'ko' : 'zh'
        if (target === current) return
        i18n.setLocale(target)
        wx.showToast({ title: i18n.t('操作成功'), icon: 'success' })
        // 重新进入本页，使页面与公共组件按新语言重新渲染
        setTimeout(() => {
          wx.reLaunch({ url: '/pages/me/index' })
        }, 400)
      }
    })
  },

  onLogout() {
    wx.showModal({
      title: i18n.t('提示'),
      content: i18n.t('确定要退出登录吗？'),
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('mini_token')
          wx.removeStorageSync('user')
          this.setData({
            isLoggedIn: false,
            userInfo: {
              avatarUrl: '',
              nickName: '',
              community: '阳光小区',
              commissionTotal: '0.00',
              commissionPending: '0.00',
              commissionSettled: '0.00'
            }
          })
          wx.showToast({ title: i18n.t('已退出登录'), icon: 'none' })
          wx.reLaunch({ url: '/pages/login/login' })
        }
      }
    })
  },

  onWithdraw() {
    const isLoggedIn = this.data.isLoggedIn
    if (!isLoggedIn) {
      wx.showToast({ title: i18n.t('请先登录'), icon: 'none' })
      setTimeout(() => { wx.navigateTo({ url: '/pages/login/login' }) }, 800)
      return
    }
    const pending = parseFloat(this.data.userInfo.commissionPending) || 0
    if (pending <= 0) {
      wx.showToast({ title: i18n.t('可提现金额不足'), icon: 'none' })
      return
    }
    const rate = this.data.withdrawRate || 0
    const credited = Math.round(pending * (100 - rate)) / 100
    wx.showModal({
      title: i18n.t('确认提现'),
      content: `${i18n.t('可提现金额')}：￥${pending.toFixed(2)}\n${i18n.t('费率')}：${rate}%\n${i18n.t('预计到账')}：￥${credited.toFixed(2)}`,
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: i18n.t('提交中') })
          const amount = Math.round(pending * 100)
          post('/miniapp/pay/withdraw/apply', { amount })
            .then(() => {
              wx.hideLoading()
              wx.showToast({ title: i18n.t('申请已提交'), icon: 'success' })
              setTimeout(() => {
                wx.showModal({
                  title: i18n.t('审核提醒'),
                  content: i18n.t('系统会在三天内审核完并安排人工转账'),
                  showCancel: false,
                  confirmText: i18n.t('知道了')
                })
              }, 800)
              this.fetchUserProfile()
            })
            .catch((err) => {
              wx.hideLoading()
              const msg = (err && (err.msg || err.message)) ? (err.msg || err.message) : i18n.t('提交失败')
              wx.showToast({ title: msg, icon: 'none' })
            })
        }
      }
    })
  },

  onTabChange(e) {
    const { key } = e.detail || {}
    if (key === 'index') {
      wx.redirectTo({ url: '/pages/index/index' })
    } else if (key === 'takeout') {
      wx.redirectTo({ url: '/pages/takout/index' })
    } else if (key === 'orders') {
      wx.redirectTo({ url: '/pages/orders/index' })
    }
  }
})
