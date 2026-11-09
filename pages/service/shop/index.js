const { request } = require('../../../utils/request')
const i18n = require('../../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    typeId: '',
    serviceName: '',
    productList: [], // List of available product specifications
    productRange: [], // Display strings for the picker
    
    // Form data
    address: '',
    specIndex: null,
    
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
      serviceName: name || '代购服务'
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
      // Format: spec_name（weight_max kg以内，价格：price_max￥）
      const range = list.map(item => {
        const priceYuan = (item.priceMax / 100).toFixed(2)
        return `${item.specName}（${item.weightMax}kg以内，价格：${priceYuan}￥）`
      })
      this.setData({
        productList: list,
        productRange: range
      })
    })
  },

  // Handle address input
  onAddressInput(e) {
    this.setData({ address: e.detail.value })
  },

  // Handle spec change
  onSpecChange(e) {
    const val = Number(e.detail.value)
    this.setData({ specIndex: val })
    this.calculateTotal()
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
    const { specIndex, productList } = this.data
    if (specIndex !== null && productList[specIndex]) {
      const price = (productList[specIndex].priceMax / 100).toFixed(2)
      this.setData({ totalPrice: price })
    } else {
      this.setData({ totalPrice: 0.00 })
    }
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
    const { address, specIndex, startTime, endTime, remark, productList, totalPrice } = this.data
    const bound = await this.checkCommunityBinding()
    if (!bound) return
    
    // Validation
    if (!address.trim()) {
      wx.showToast({ title: i18n.t('请填写家庭住址'), icon: 'none' })
      return
    }

    if (specIndex === null) {
      wx.showToast({ title: i18n.t('请选择代购规格'), icon: 'none' })
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
    
    if (startTime >= endTime) {
       wx.showToast({ title: i18n.t('结束时间必须晚于开始时间'), icon: 'none' })
       return
    }

    // Splice note
    let noteParts = []
    
    // 1. Spec
    const specName = productList[specIndex].specName
    noteParts.push(`代购规格：${specName}`)
    
    // 2. Appointment time
    noteParts.push(`需要在${startTime}-${endTime}内为我购买配送`)
    
    // 3. Remark
    if (remark && remark.trim()) {
        noteParts.push(`备注：${remark}`)
    }
    
    const finalNote = noteParts.join('\n')
    
    // Calculate amounts (totalPrice is in Yuan string, convert to cents)
    const amountInCents = Math.round(parseFloat(totalPrice) * 100)
    
    const payload = {
        detailAddress: address,
        serviceType: '代购服务',
        specName: specName,
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
