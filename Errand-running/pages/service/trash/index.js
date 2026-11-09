const { request } = require('../../../utils/request')
const i18n = require('../../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    typeId: '',
    serviceName: '',
    productList: [], // List of available product specifications from backend
    productRange: [], // Display strings for the picker
    
    // Form data
    trashItems: [
      { specIndex: null } // Initial trash item
    ],
    address: '', // Delivery Address
    
    // Time Picker Data
    timeRange: [], // [Days, Hours, Minutes]
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
      serviceName: name || i18n.t('代扔垃圾')
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
    
    // Generate next 7 days
    const today = new Date()
    for (let i = 0; i < 7; i++) {
      const d = new Date(today.getTime() + i * 24 * 60 * 60 * 1000)
      const year = d.getFullYear()
      const month = (d.getMonth() + 1).toString().padStart(2, '0')
      const day = d.getDate().toString().padStart(2, '0')
      days.push(`${year}-${month}-${day}`)
    }

    // Hours 00-23
    for (let i = 0; i < 24; i++) {
      hours.push(i.toString().padStart(2, '0'))
    }

    // Minutes 00-50 (step 10)
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
      // Format: spec_name（price_max￥/件）
      const range = list.map(item => {
        const priceYuan = (item.priceMax / 100).toFixed(2)
        return `${item.specName}（${priceYuan}￥/件）`
      })
      this.setData({
        productList: list,
        productRange: range
      })
    })
  },

  // Add a new trash item row
  addTrashItem() {
    const trashItems = this.data.trashItems
    trashItems.push({ specIndex: null })
    this.setData({ trashItems })
    this.calculateTotal()
  },

  // Remove a trash item row
  removeTrashItem(e) {
    const index = e.currentTarget.dataset.index
    const trashItems = this.data.trashItems
    if (trashItems.length <= 1) {
      wx.showToast({ title: i18n.t('至少需要一项垃圾'), icon: 'none' })
      return
    }
    trashItems.splice(index, 1)
    this.setData({ trashItems })
    this.calculateTotal()
  },

  // Handle specification change for a specific item
  onSpecChange(e) {
    const index = e.currentTarget.dataset.index
    const val = Number(e.detail.value)
    const trashItems = this.data.trashItems
    trashItems[index].specIndex = val
    this.setData({ trashItems })
    this.calculateTotal()
  },

  // Handle address input
  onAddressInput(e) {
    this.setData({ address: e.detail.value })
  },

  // Helper to format time from picker selection
  formatTimeFromPicker(indices) {
    const { timeRange } = this.data
    const day = timeRange[0][indices[0]]
    const hour = timeRange[1][indices[1]]
    const minute = timeRange[2][indices[2]]
    return `${day} ${hour}:${minute}`
  },

  // Handle start time change
  onStartTimeChange(e) {
    const indices = e.detail.value
    const timeStr = this.formatTimeFromPicker(indices)
    this.setData({
      startTimeIndex: indices,
      startTime: timeStr
    })
  },

  // Handle end time change
  onEndTimeChange(e) {
    const indices = e.detail.value
    const timeStr = this.formatTimeFromPicker(indices)
    this.setData({
      endTimeIndex: indices,
      endTime: timeStr
    })
  },

  // Handle remark input
  onRemarkInput(e) {
    this.setData({ remark: e.detail.value })
  },

  // Calculate total price
  calculateTotal() {
    const { trashItems, productList } = this.data
    let totalCents = 0
    trashItems.forEach(p => {
      if (p.specIndex !== null && productList[p.specIndex]) {
        totalCents += productList[p.specIndex].priceMax
      }
    })
    this.setData({
      totalPrice: (totalCents / 100).toFixed(2)
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

  async onSubmit() {
    const { trashItems, address, startTime, endTime, remark, productList, totalPrice } = this.data
    const bound = await this.checkCommunityBinding()
    if (!bound) return
    
    // Validation
    for (let i = 0; i < trashItems.length; i++) {
      const p = trashItems[i]
      if (p.specIndex === null) {
        wx.showToast({ title: `请选择第${i + 1}项垃圾的规格`, icon: 'none' })
        return
      }
    }

    if (!address.trim()) {
      wx.showToast({ title: i18n.t('请填写家庭住址'), icon: 'none' })
      return
    }

    if (!startTime) {
      wx.showToast({ title: i18n.t('请选择预约开始时间'), icon: 'none' })
      return
    }

    if (!endTime) {
      wx.showToast({ title: i18n.t('请选择预约结束时间'), icon: 'none' })
      return
    }
    
    // Simple validation: End time > Start time
    if (startTime >= endTime) {
       wx.showToast({ title: i18n.t('结束时间必须晚于开始时间'), icon: 'none' })
       return
    }

    // Splice note
    let noteParts = []
    let specNameList = []
    
    // 1. Trash items
    trashItems.forEach((p, index) => {
      const specName = productList[p.specIndex].specName
      specNameList.push(specName)
      noteParts.push(`${index + 1}.垃圾规格：${specName}`)
    })
    
    // 2. Appointment time
    noteParts.push(`需要在${startTime}-${endTime}内为我处理垃圾`)
    
    // 3. Remark
    if (remark && remark.trim()) {
        noteParts.push(`备注：${remark}`)
    }
    
    const finalNote = noteParts.join('\n')
    const finalSpecName = specNameList.join(',')
    
    // Calculate amounts (totalPrice is in Yuan string, convert to cents)
    const amountInCents = Math.round(parseFloat(totalPrice) * 100)
    
    const payload = {
        startAddress: address,
        detailAddress: address,
        serviceType: '垃圾代扔',
        specName: finalSpecName,
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
             const orderNo = res.data && res.data.orderNo
             if (!orderNo) {
               wx.showToast({ title: i18n.t('订单号异常'), icon: 'none' })
               setTimeout(() => {
                 wx.redirectTo({ url: '/pages/orders/index' })
               }, 800)
               return
             }
             this.payOrder(orderNo)
        } else {
             wx.showToast({ title: res.msg || i18n.t('下单失败'), icon: 'none' })
        }
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

  onBack() {
    wx.navigateBack()
  }
})
