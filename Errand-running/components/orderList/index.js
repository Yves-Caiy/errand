const i18n = require('../../utils/i18n')

Component({
  properties: {},
  data: {
    locale: 'zh',
    activeTab: 'all',
    activeType: '全部',
    orders: [],
    typeFilters: ['全部', '快递代拿', '垃圾代扔', '物品代送', '代购服务', '万能服务', '便民超市', '打印服务', '物品寄存'],
    displayOrders: [],
    pageNum: 1,
    pageSize: 5,
    total: 0,
    hasMore: true,
    loading: false
  },
  lifetimes: {
    attached() {
      i18n.applyLocale(this)
      this.loadOrders(true)
    }
  },
  pageLifetimes: {
    show() {
      i18n.applyLocale(this)
    }
  },
  methods: {
    loadOrders(reset = false) {
      if (this.data.loading) return
      if (!reset && !this.data.hasMore) return

      this.setData({ loading: true })
      if (reset) {
        this.setData({ pageNum: 1, orders: [], displayOrders: [], hasMore: true })
      }

      const { request } = require('../../utils/request')
      const t = this.data.activeType
      const params = {
        pageNum: reset ? 1 : this.data.pageNum,
        pageSize: this.data.pageSize
      }
      if (t && t !== '全部') params.serviceType = t

      // 获取当前小区ID
      const currentCommunity = wx.getStorageSync('currentCommunity')
      if (currentCommunity && currentCommunity.id) {
        params.communityId = currentCommunity.id
      }

      request({ url: '/miniapp/order/list', method: 'GET', data: params })
        .then((resp) => {
          // Backend returns AjaxResult with rows and total in body (which is resolved as resp)
          const list = resp.rows || []
          const total = resp.total || 0
          
          const currentList = this.data.orders
          const newList = reset ? list : currentList.concat(list)
          
          this.setData({
            orders: newList,
            total: total,
            hasMore: newList.length < total,
            pageNum: (reset ? 1 : this.data.pageNum) + 1,
            loading: false
          })
          this.refreshDisplay()
        })
        .catch(() => {
          this.setData({ loading: false })
          if (reset) this.setData({ orders: [], displayOrders: [] })
        })
    },
    refreshDisplay() {
      let list = this.data.orders
      // Backend handles sorting (ORDER BY create_time DESC)
      
      const display = list.map(o => {
        const orderType = o.orderType
        const showRunner = o.orderStatus !== 1 && !!o.runnerNickname
        const cardTag = orderType === 4 ? (o.serviceType || '其他服务') : String(orderType)
        const actual = (o.actualAmount != null ? o.actualAmount : (o.budget || 0))
        return {
          id: o.id,
          orderNo: o.orderNo,
          cardTag,
          orderType,
          status: o.orderStatus,
          runnerNickname: showRunner ? o.runnerNickname : '',
          communityName: o.communityName || '',
          detailAddress: o.detailAddress || '',
          startAddress: o.startAddress || '',
          endAddress: o.endAddress || '',
          itemDesc: o.itemDesc || '',
          budget: o.budget != null ? (o.budget / 100).toFixed(2) : '',
          specName: o.specName || '',
          wechatId: o.wechatId || '',
          actualAmount: (actual / 100).toFixed(2),
          payStatus: o.payStatus,
          payTime: o.payTime || '',
          finishTime: o.finishTime || '',
          cancelReason: o.cancelReason || '',
          note: o.note || '',
          createTime: o.createTime || ''
        }
      })
      this.setData({ displayOrders: display })
    },
    onTabTap(e) {
      const key = e.currentTarget.dataset.key
      this.setData({ activeTab: key })
      this.loadOrders(true)
    },
    onTypeTap(e) {
      const type = e.currentTarget.dataset.type
      this.setData({ activeType: type })
      this.loadOrders(true)
    },
    onLoadMore() {
      this.loadOrders(false)
    },
    onTakeTap(e) {
      const id = e.currentTarget.dataset.id
      wx.showModal({
        title: i18n.t('提示'),
        content: i18n.t('确认要接下此单吗？'),
        success: (res) => {
          if (res.confirm) {
            const { request } = require('../../utils/request')
            request({
              url: '/miniapp/order/accept',
              method: 'PUT',
              data: { id: id },
              header: { 'content-type': 'application/x-www-form-urlencoded' }
            }).then(res => {
              wx.showToast({ title: i18n.t('接单成功'), icon: 'success' })
              this.loadOrders(true)
              this.triggerEvent('takeOrder', { id })
            }).catch(err => {
              if (err && err.code === 500) {
                wx.showModal({
                  title: i18n.t('提示'),
                  content: err.msg || i18n.t('接单失败'),
                  showCancel: false
                })
              } else {
                wx.showToast({ title: (err && err.msg) || i18n.t('接单失败'), icon: 'none' })
              }
            })
          }
        }
      })
    }
  }
})
