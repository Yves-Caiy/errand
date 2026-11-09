const { request } = require('../../utils/request')
const i18n = require('../../utils/i18n')

const NOTICE_POLL_INTERVAL = 15000

Component({
  properties: {
    active: {
      type: String,
      value: 'index'
    }
  },
  data: {
    locale: 'zh',
    items: [
      { key: 'index', label: '首页', iconDefault: '/static/icons/home-default.svg', iconActive: '/static/icons/home.svg' },
      { key: 'takeout', label: '外卖', iconDefault: '/static/icons/takeout-default.svg', iconActive: '/static/icons/takeout.svg' },
      { key: 'orders', label: '订单', iconDefault: '/static/icons/order-default.svg', iconActive: '/static/icons/order.svg' },
      { key: 'me', label: '我的', iconDefault: '/static/icons/me-default.svg', iconActive: '/static/icons/me.svg' }
    ],
    barHeight: 56,
    safeBottom: 0,
    meDot: false,
    meDotCount: 0
  },
  lifetimes: {
    attached() {
      i18n.applyLocale(this)
      // 计算安全区底部（iPhone X 及以上），使用新 API
      try {
        const info = wx.getWindowInfo ? wx.getWindowInfo() : wx.getSystemInfoSync()
        if (info && info.safeArea) {
          const insetBottom = info.screenHeight - info.safeArea.bottom
          this.setData({ safeBottom: insetBottom > 0 ? insetBottom : 0 })
        } else {
          this.setData({ safeBottom: 0 })
        }
      } catch (e) {}

      this.startNoticePolling()
    },
    detached() {
      this.stopNoticePolling()
    }
  },
  // 仅当前可见页面的组件轮询，避免多页面栈内组件重复请求
  pageLifetimes: {
    show() {
      i18n.applyLocale(this)
      this.startNoticePolling()
    },
    hide() {
      this.stopNoticePolling()
    }
  },
  methods: {
    onItemTap(e) {
      const key = e.currentTarget.dataset.key
      if (key === this.data.active) return
      this.triggerEvent('tabChange', { key })
    },

    // 来单提醒红点：进入页面先查一次，之后每 15 秒轮询一次
    startNoticePolling() {
      this.stopNoticePolling()
      this.refreshNotice()
      this.noticeTimer = setInterval(() => {
        this.refreshNotice()
      }, NOTICE_POLL_INTERVAL)
    },

    stopNoticePolling() {
      if (this.noticeTimer) {
        clearInterval(this.noticeTimer)
        this.noticeTimer = null
      }
    },

    // 未登录不请求；接单员有新来单时在「我的」图标上显示红点
    refreshNotice() {
      if (!wx.getStorageSync('mini_token')) {
        if (this.data.meDot) {
          this.setData({ meDot: false, meDotCount: 0 })
        }
        return
      }
      request({ url: '/miniapp/runner/notice/unread', method: 'GET' })
        .then((res) => {
          const count = res && res.data ? Number(res.data.count) || 0 : 0
          this.setData({ meDot: count > 0, meDotCount: count })
        })
        .catch(() => {})
    }
  }
})
