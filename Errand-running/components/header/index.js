const i18n = require('../../utils/i18n')

Component({
  properties: {
    title: {
      type: String,
      value: ''
    },
    showBack: {
      type: Boolean,
      value: true
    }
  },
  data: {
    locale: 'zh',
    statusBarHeight: 20,
    navBarHeight: 48,
    headerHeight: 68,
    isHome: false,
    communityList: [],
    communityIndex: 0
  },
  lifetimes: {
    attached() {
      i18n.applyLocale(this)
      try {
        const win = wx.getWindowInfo ? wx.getWindowInfo() : wx.getSystemInfoSync();
        const dev = wx.getDeviceInfo ? wx.getDeviceInfo() : {};
        const statusBarHeight = win.statusBarHeight || 20;
        const sys = (dev.system || win.system || '').toLowerCase();
        const platform = (dev.platform || win.platform || '').toLowerCase();
        const isIOS = sys.includes('ios') || platform === 'ios';
        const navBarHeight = isIOS ? 44 : 48;
        const headerHeight = statusBarHeight + navBarHeight;
        const pages = getCurrentPages();
        const current = pages && pages.length ? pages[pages.length - 1] : null;
        const route = (current && current.route) ? current.route : '';
        const isHome = route.indexOf('pages/index/index') === 0;
        this.setData({ statusBarHeight, navBarHeight, headerHeight, isHome });
      } catch (e) {}
      const { request } = require('../../utils/request')
      let storedId = ''
      try { storedId = wx.getStorageSync('current_community_id') || '' } catch (e) {}
      request({ url: '/miniapp/community/list', method: 'GET' })
        .then((resp) => {
          const list = (resp.data || []).map(item => ({ id: item.id, communityName: item.communityName }))
          let idx = 0
          if (storedId) {
            const foundIndex = list.findIndex(x => String(x.id) === String(storedId))
            idx = foundIndex >= 0 ? foundIndex : 0
          }
          this.setData({ communityList: list, communityIndex: idx })
          try {
            const sel = list[idx] || {}
            wx.setStorageSync('current_community_id', sel.id || '')
            wx.setStorageSync('current_community_name', sel.communityName || '')
          } catch (e) {}
        })
        .catch(() => {})
    }
  },
  pageLifetimes: {
    show() {
      i18n.applyLocale(this)
    }
  },
  methods: {
    onBackTap() {
      // 若页面栈只有一个，navigateBack 会失败，兜底触发事件给外部处理
      const pages = getCurrentPages();
      if (pages && pages.length > 1) {
        wx.navigateBack({ delta: 1 });
      } else {
        this.triggerEvent('back');
      }
    }
    ,
    onCommunityChange(e) {
      const { value } = e.detail || {};
      const idx = Number(value) || 0;
      this.setData({ communityIndex: idx });
      const sel = this.data.communityList[idx] || {};
      try {
        wx.setStorageSync('current_community_id', sel.id || '')
        wx.setStorageSync('current_community_name', sel.communityName || '')
      } catch (e) {}
      this.triggerEvent('communityChange', { index: idx, id: sel.id, name: sel.communityName });
    }
  }
});
