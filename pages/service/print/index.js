const { request } = require('../../../utils/request')
const { uploadFile } = require('../../../utils/upload')
const i18n = require('../../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    typeId: '',
    serviceName: '',
    productList: [],
    productRange: [],
    
    // Form
    specIndex: null,
    paperSizeOptions: ['A0', 'A1', 'A2', 'A3', 'A4'],
    paperSizeIndex: 4, // Default A4
    copies: 1,
    wechat: '',
    fileList: [], // List of uploaded files
    totalPages: 1, // Manual input or calculated
    address: '',
    
    // Time Picker
    timeRange: [],
    startTime: '',
    endTime: '',
    startTimeIndex: [0, 0, 0],
    endTimeIndex: [0, 0, 0],

    remark: '',
    
    totalPrice: 0.00
  },

  onLoad(options) {
    i18n.applyLocale(this)
    const { id, name } = options
    this.setData({
      typeId: id,
      serviceName: name || '打印服务'
    })
    this.loadProducts(id)
    this.initTimePicker()
  },

  onShow() {
    i18n.applyLocale(this)
  },

  initTimePicker() {
    const days = []
    const hours = []
    const minutes = []
    
    const today = new Date()
    for (let i = 0; i < 7; i++) {
      const d = new Date(today.getTime() + i * 24 * 60 * 60 * 1000)
      const year = d.getFullYear()
      const month = (d.getMonth() + 1).toString().padStart(2, '0')
      const day = d.getDate().toString().padStart(2, '0')
      days.push(`${year}-${month}-${day}`)
    }

    for (let i = 0; i < 24; i++) {
      hours.push(i.toString().padStart(2, '0'))
    }

    for (let i = 0; i < 60; i += 10) {
      minutes.push(i.toString().padStart(2, '0'))
    }

    this.setData({
      timeRange: [days, hours, minutes]
    })
  },

  loadProducts(typeId) {
    if (!typeId) return
    request({
      url: '/miniapp/product/list',
      method: 'GET',
      data: { typeId }
    }).then(res => {
      const list = res.data || []
      const range = list.map(item => {
        const priceYuan = (item.priceMax / 100).toFixed(2)
        // Format: spec_name-price_max￥
        return `${item.specName}-${priceYuan}￥`
      })
      this.setData({
        productList: list,
        productRange: range
      })
    })
  },

  onSpecChange(e) {
    const val = Number(e.detail.value)
    this.setData({ specIndex: val })
    this.calculateTotal()
  },

  onPaperSizeChange(e) {
    this.setData({ paperSizeIndex: Number(e.detail.value) })
  },

  onCopiesChange(e) {
    const type = e.currentTarget.dataset.type
    let { copies } = this.data
    if (type === 'minus' && copies > 1) {
      copies--
    } else if (type === 'plus') {
      copies++
    }
    this.setData({ copies })
    this.calculateTotal()
  },

  onWechatInput(e) {
    this.setData({ wechat: e.detail.value })
  },
  
  onAddressInput(e) {
    this.setData({ address: e.detail.value })
  },

  onChooseFile() {
    wx.chooseMessageFile({
      count: 9,
      type: 'file',
      extension: ['png', 'jpg', 'jpeg', 'pdf', 'doc', 'docx'],
      success: (res) => {
        const newFiles = res.tempFiles
        const validFiles = newFiles.filter(f => {
          const ext = f.name.split('.').pop().toLowerCase()
          return ['png', 'jpg', 'jpeg', 'pdf', 'doc', 'docx'].includes(ext)
        })
        
        if (validFiles.length === 0) {
           wx.showToast({ title: i18n.t('仅支持图片/PDF/Word'), icon: 'none' })
           return
        }

        const oldList = this.data.fileList
        const currentList = oldList.concat(validFiles)
        
        // Smart update for page count
        // If previously empty, set to file count.
        // If adding to existing, add count.
        let newTotalPages = this.data.totalPages
        if (oldList.length === 0) {
          newTotalPages = validFiles.length
        } else {
          newTotalPages += validFiles.length
        }

        this.setData({ 
          fileList: currentList,
          totalPages: newTotalPages
        })
        
        // Check for docs to warn user
        const hasDoc = validFiles.some(f => {
          const ext = f.name.split('.').pop().toLowerCase()
          return ['pdf', 'doc', 'docx'].includes(ext)
        })
        
        if (hasDoc) {
          wx.showToast({ title: i18n.t('文档页数请手动确认'), icon: 'none' })
        }

        this.calculateTotal()
      }
    })
  },

  onRemoveFile(e) {
    const index = e.currentTarget.dataset.index
    const list = this.data.fileList
    list.splice(index, 1)
    
    let newPages = this.data.totalPages - 1
    if (newPages < 1) newPages = 1
    
    this.setData({ 
      fileList: list,
      totalPages: newPages
    })
    
    this.calculateTotal()
  },

  onTotalPagesInput(e) {
    let val = parseInt(e.detail.value)
    if (isNaN(val) || val < 1) val = 1
    this.setData({ totalPages: val })
    this.calculateTotal()
  },

  formatTimeFromPicker(indices) {
    const { timeRange } = this.data
    const day = timeRange[0][indices[0]]
    const hour = timeRange[1][indices[1]]
    const minute = timeRange[2][indices[2]]
    return `${day} ${hour}:${minute}`
  },

  onStartTimeChange(e) {
    const indices = e.detail.value
    const timeStr = this.formatTimeFromPicker(indices)
    this.setData({
      startTimeIndex: indices,
      startTime: timeStr
    })
  },

  onEndTimeChange(e) {
    const indices = e.detail.value
    const timeStr = this.formatTimeFromPicker(indices)
    this.setData({
      endTimeIndex: indices,
      endTime: timeStr
    })
  },
  
  onRemarkInput(e) {
    this.setData({ remark: e.detail.value })
  },

  calculateTotal() {
    const { specIndex, productList, copies, totalPages } = this.data
    if (specIndex !== null && productList[specIndex]) {
      const pricePerItem = productList[specIndex].priceMax / 100
      // Assumption: Price is per page * copies
      const total = (pricePerItem * copies * totalPages).toFixed(2)
      this.setData({ totalPrice: total })
    } else {
      this.setData({ totalPrice: 0.00 })
    }
  },

  async onSubmit() {
    const { address, specIndex, startTime, endTime, remark, productList, totalPrice, wechat, fileList, paperSizeOptions, paperSizeIndex, copies } = this.data
    const bound = await this.checkCommunityBinding()
    if (!bound) return
    
    if (specIndex === null) {
      wx.showToast({ title: i18n.t('请选择打印规格'), icon: 'none' })
      return
    }

    if (fileList.length === 0) {
       wx.showToast({ title: i18n.t('请上传文件'), icon: 'none' })
       return
    }

    if (!wechat.trim()) {
      wx.showToast({ title: i18n.t('请填写微信号'), icon: 'none' })
      return
    }

    if (!address.trim()) {
      wx.showToast({ title: i18n.t('请填写配送地址'), icon: 'none' })
      return
    }

    if (!startTime) {
      wx.showToast({ title: i18n.t('请选择开始时间'), icon: 'none' })
      return
    }

    if (!endTime) {
      wx.showToast({ title: i18n.t('请选择结束时间'), icon: 'none' })
      return
    }
    
    if (startTime >= endTime) {
       wx.showToast({ title: i18n.t('结束时间必须晚于开始时间'), icon: 'none' })
       return
    }

    // Splice note
    let noteParts = []
    
    // 1. Spec: 打印规格：选择的规格-选择的纸张大小-份数
    const specName = productList[specIndex].specName
    const paperSize = paperSizeOptions[paperSizeIndex]
    noteParts.push(`打印规格：${specName}-${paperSize}-${copies}份`)
    
    // 2. Appointment time
    noteParts.push(`需要在${startTime}-${endTime}内为我服务`)
    
    // 3. Remark & Wechat & Files
    // Note: User prompt asked to pass wechat_id as param, but also splice note.
    // Files are usually important for print service, so appending them to note as well.
    let extraInfo = []
    if (remark && remark.trim()) {
        extraInfo.push(`备注：${remark}`)
    }
    extraInfo.push(`文件列表：${fileList.map(f=>f.name).join(',')}`)
    
    noteParts.push(extraInfo.join('，'))
    
    const finalNote = noteParts.join('\n')
    
    // Calculate amounts (totalPrice is in Yuan string, convert to cents)
    const amountInCents = Math.round(parseFloat(totalPrice) * 100)
    
    const payload = {
        detailAddress: address,
        serviceType: '打印服务',
        specName: specName,
        wechatId: wechat,
        totalAmount: amountInCents,
        actualAmount: amountInCents,
        commissionAmount: amountInCents,
        note: finalNote
    }
    
    request({
        url: '/miniapp/order/create/other',
        method: 'POST',
        data: payload
    }).then(res => {
        if (res.code === 200) {
             const data = res.data || {}
             const orderId = data.id
             const orderNo = data.orderNo
             if (!orderId || !orderNo) {
               wx.showToast({ title: i18n.t('订单信息异常'), icon: 'none' })
               setTimeout(() => {
                 wx.redirectTo({ url: '/pages/orders/index' })
               }, 800)
               return
             }
             this.uploadFilesAndPay(orderId, orderNo)
        } else {
             wx.showToast({ title: res.msg || i18n.t('下单失败'), icon: 'none' })
        }
    })
  },

  uploadFilesAndPay(orderId, orderNo) {
      const { fileList } = this.data
      if (!fileList || fileList.length === 0) {
          this.payOrder(orderNo)
          return
      }

      // 统一上传：保存到服务器本地，返回 http 地址
      const uploadPromises = fileList.map(file => {
          return uploadFile(file.path).then((url) => ({ url: url, name: file.name }))
      })
      
      wx.showLoading({ title: i18n.t('上传文件中...') })
      
      Promise.all(uploadPromises).then(files => {
          // Save to DB
          request({
              url: '/miniapp/order/save/print-files',
              method: 'POST',
              data: {
                  orderId: orderId,
                  files: files
              }
          }).then(res => {
              wx.hideLoading()
              if (res.code === 200) {
                  this.payOrder(orderNo)
              } else {
                   wx.showToast({ title: i18n.t('文件保存失败'), icon: 'none' })
              }
          }).catch(err => {
              wx.hideLoading()
              wx.showToast({ title: i18n.t('文件保存异常'), icon: 'none' })
          })
      }).catch(err => {
          wx.hideLoading()
          wx.showToast({ title: i18n.t('文件上传失败'), icon: 'none' })
      })
  },

  payOrder(orderNo) {
    const { mockPay } = require('../../../utils/pay')
    mockPay(orderNo).then(() => {
      setTimeout(() => {
        wx.redirectTo({ url: '/pages/orders/index' })
      }, 800)
    }).catch(() => {
      setTimeout(() => {
        wx.redirectTo({ url: '/pages/orders/index' })
      }, 800)
    })
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

  onBack() {
    wx.navigateBack()
  }
})
