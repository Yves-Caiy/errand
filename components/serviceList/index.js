const i18n = require('../../utils/i18n')

Component({
  properties: {
    types: {
      type: Array,
      value: [],
      observer: function (val) {
        this.updateRenderList(val)
      }
    }
  },
  data: {
    locale: 'zh',
    services: [
    ],
    renderList: [],
    routeMap: {
      '快递代拿': '/pages/service/express/index',
      '垃圾代扔': '/pages/service/trash/index',
      '物品代送': '/pages/service/delivery/index',
      '代购服务': '/pages/service/shop/index',
      '便民超市': '/pages/service/market/index',
      '打印服务': '/pages/service/print/index',
      '物品寄存': '/pages/service/storage/index',
      '万能服务':'/pages/service/universal/index'
    },
    disabledServices: []
  },
  lifetimes: {
    attached() {
      i18n.applyLocale(this)
      this.updateRenderList(this.properties.types || [])
    }
  },
  pageLifetimes: {
    show() {
      i18n.applyLocale(this)
    }
  },
  methods: {
    updateRenderList(types) {
      const BASE_URL = 'http://localhost:8080'
      const toAbs = (url) => {
        if (!url) return ''
        return /^https?:\/\//.test(url) ? url : (BASE_URL + url)
      }
      if (Array.isArray(types) && types.length) {
        const list = types.map(t => ({ typeName: t.typeName, iconUrlAbs: toAbs(t.iconUrl), id: t.id }))
        this.setData({ renderList: list })
      } else {
        const list = this.data.services.map(name => ({ typeName: name, iconUrlAbs: '', id: null }))
        this.setData({ renderList: list })
      }
    },
    onTap(e) {
      const name = e.currentTarget.dataset.name
      if (this.data.disabledServices.includes(name)) {
        wx.showToast({ title: i18n.t('暂未开通'), icon: 'none' })
        return
      }
      const url = this.data.routeMap[name]
      if (!url) {
        wx.showToast({ title: i18n.t('暂未配置'), icon: 'none' })
        return
      }
      // Find the service item to get its ID
      const item = this.data.renderList.find(i => i.typeName === name)
      const id = item ? item.id : ''
      wx.navigateTo({ url: `${url}?id=${id}&name=${name}` })
    }
  }
})
