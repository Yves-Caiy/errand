 const { request, BASE_URL } = require('../../../utils/request')
const i18n = require('../../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    orders: [],
    loading: false,
    statusLabel: {
      0: '待支付',
      1: '待接单',
      2: '已接单',
      3: '已完成',
      4: '已取消'
    },
    typeNameMap: {
      1: '外卖',
      2: '超市',
      3: '套餐'
    }
  },

  onLoad() {
    i18n.applyLocale(this)
    this.loadOrders()
  },

  onShow() {
    i18n.applyLocale(this)
  },

  onPullDownRefresh() {
    this.loadOrders(() => {
      wx.stopPullDownRefresh()
    })
  },

  loadOrders(cb) {
    this.setData({ loading: true })
    request({
      url: '/miniapp/order/acceptedList',
      method: 'GET'
    }).then(res => {
      if (res.code === 200) {
        const list = res.data || []
        const display = list.map(o => {
          const orderType = o.orderType
          const cardTag = orderType === 4 ? (o.serviceType || '其他服务') : (this.data.typeNameMap[orderType] || `类型${orderType}`)
          const actual = (o.actualAmount != null ? o.actualAmount : (o.budget || 0))
          
          return {
            id: o.id,
            orderNo: o.orderNo,
            cardTag,
            orderType,
            status: o.orderStatus,
            runnerNickname: o.runnerNickname || '',
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
            createTime: o.createTime || '',
            // Additional fields for runner view
            userNickname: o.userNickname || '',
            serviceType: o.serviceType || '',
            isPrintService: (orderType === 4) && ((o.serviceType || '').indexOf('打印') !== -1)
          }
        })
        this.setData({ orders: display, loading: false })
      } else {
        this.setData({ loading: false })
      }
      if (cb) cb()
    }).catch(err => {
      console.error(err)
      this.setData({ loading: false })
      if (cb) cb()
    })
  },

  handleContact(e) {
    const item = e.currentTarget.dataset.item
    if (item.wechatId) {
      wx.setClipboardData({
        data: item.wechatId,
        success: () => {
          wx.showToast({ title: i18n.t('微信号已复制'), icon: 'success' })
        }
      })
    } else {
      wx.showToast({ title: i18n.t('暂无联系方式'), icon: 'none' })
    }
  },

  handleDownload(e) {
    const item = e.currentTarget.dataset.item
    if (!item || !item.id) return
    wx.showLoading({ title: i18n.t('加载中'), mask: true })
    request({
      url: '/miniapp/order/print-files',
      method: 'GET',
      data: { orderId: item.id }
    }).then(res => {
      if (res.code !== 200) {
        wx.hideLoading()
        wx.showToast({ title: res.msg || i18n.t('获取文件失败'), icon: 'none' })
        return
      }
      const files = res.rows || res.data || []
      if (!files || files.length === 0) {
        wx.hideLoading()
        wx.showToast({ title: i18n.t('暂无打印文件'), icon: 'none' })
        return
      }
      const token = wx.getStorageSync('mini_token') || ''
      const sys = wx.getSystemInfoSync()
      const isPC = sys.platform === 'windows' || sys.platform === 'mac'
      const download = (f) => {
        let url = ''
        let header = {}
        if (f.fileUrl && (f.fileUrl.startsWith('http://') || f.fileUrl.startsWith('https://'))) {
          url = f.fileUrl
          header = {}
        } else {
          url = `${BASE_URL}/miniapp/order/print-files/download/${f.id}`
          header = { 'Mini-Token': token }
        }
        wx.downloadFile({
          url,
          header,
          success: (r) => {
            if (r.statusCode !== 200) {
              wx.hideLoading()
              wx.showToast({ title: i18n.t('下载失败'), icon: 'none' })
              return
            }
            const tempPath = r.tempFilePath
            const nameSource = f.fileName || f.fileUrl || ''
            const lowerName = nameSource.toLowerCase()
            const isImage = lowerName.endsWith('.png') || lowerName.endsWith('.jpg') || lowerName.endsWith('.jpeg') || lowerName.endsWith('.gif') || lowerName.endsWith('.bmp') || lowerName.endsWith('.webp')
            if (!isPC && isImage && wx.saveImageToPhotosAlbum) {
              wx.hideLoading()
              wx.saveImageToPhotosAlbum({
                filePath: tempPath,
                success: () => {
                  wx.showToast({ title: i18n.t('已保存到相册'), icon: 'success' })
                },
                fail: () => {
                  wx.showToast({ title: i18n.t('保存到相册失败'), icon: 'none' })
                }
              })
            } else if (isPC && wx.canIUse && wx.canIUse('saveFileToDisk')) {
              wx.saveFileToDisk({
                filePath: tempPath,
                success: () => {
                  wx.hideLoading()
                  wx.showToast({ title: i18n.t('已保存到本地'), icon: 'success' })
                },
                fail: () => {
                  wx.hideLoading()
                  wx.showToast({ title: i18n.t('保存失败'), icon: 'none' })
                }
              })
            } else {
              wx.hideLoading()
              wx.openDocument({
                filePath: tempPath,
                showMenu: true,
                fail: () => {
                  wx.showToast({ title: i18n.t('打开失败'), icon: 'none' })
                }
              })
            }
          },
          fail: () => {
            wx.hideLoading()
            wx.showToast({ title: i18n.t('下载失败'), icon: 'none' })
          }
        })
      }
      if (files.length === 1) {
        download(files[0])
      } else {
        wx.hideLoading()
        wx.showActionSheet({
          itemList: files.map(f => (f.fileName || f.fileUrl || i18n.t('文件'))),
          success: (res2) => {
            const idx = res2.tapIndex
            const f = files[idx]
            if (f) {
              wx.showLoading({ title: i18n.t('下载中'), mask: true })
              download(f)
            }
          }
        })
      }
    }).catch(() => {
      wx.hideLoading()
      wx.showToast({ title: i18n.t('获取文件失败'), icon: 'none' })
    })
  }
})
