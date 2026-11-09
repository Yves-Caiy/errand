const { request } = require('../../../utils/request')

Page({
  data: {
    items: [],
    totalAmount: 0,
    storeId: null,
    note: '',
    orderType: 1, // 1=外卖, 2=超市
    userInfo: {
      nickname: '',
      phone: '',
      detailAddress: '',
      communityName: '',
      communityId: null
    }
  },

  onLoad(options) {
    if (options.type) {
      this.setData({ orderType: Number(options.type) })
    }
    
    const eventChannel = this.getOpenerEventChannel()
    if (eventChannel && eventChannel.on) {
      eventChannel.on('acceptDataFromOpenerPage', (data) => {
        this.setData({
          items: data.items || [],
          totalAmount: data.totalAmount || 0,
          storeId: data.storeId,
          orderType: data.type ? Number(data.type) : this.data.orderType
        })
      })
    }
    this.getUserProfile()
  },

  getUserProfile() {
    request({
      url: '/miniapp/user/profile'
    }).then(res => {
      if (res.code === 200) {
        const user = res.data || {}
        this.setData({
          'userInfo.nickname': user.nickname,
          'userInfo.phone': user.phone, 
          'userInfo.detailAddress': user.detailAddress, 
          'userInfo.communityId': user.communityId,
          'userInfo.communityName': user.communityName
        })
        // 如果没有地址，提示去设置
        if (!user.communityId) {
          wx.showModal({
            title: '提示',
            content: '请先绑定小区信息',
            showCancel: false,
            success: () => {
              wx.navigateTo({ url: '/pages/me/address/index' })
            }
          })
        }
      }
    })
  },

  onNameInput(e) {
    this.setData({ 'userInfo.nickname': e.detail.value })
  },

  onPhoneInput(e) {
    this.setData({ 'userInfo.phone': e.detail.value })
  },

  onAddressInput(e) {
    this.setData({ 'userInfo.detailAddress': e.detail.value })
  },

  onNoteInput(e) {
    this.setData({ note: e.detail.value })
  },

  onSubmitOrder() {
    if (!this.data.userInfo.detailAddress || !this.data.userInfo.communityId) {
      wx.showToast({ title: '请完善收货地址', icon: 'none' })
      return
    }

    if (!this.data.userInfo.phone) {
      wx.showToast({ title: '请填写联系电话', icon: 'none' })
      return
    }

    const orderItems = this.data.items.map(item => ({
      itemType: this.data.orderType,
      productId: item.productId || item.id,
      productName: item.productName || item.name,
      quantity: item.quantity,
      unitPrice: item.price || item.unitPrice,
      subtotal: (item.price || item.unitPrice) * item.quantity,
      storeId: this.data.storeId
    }))

    const actualAmount = Math.floor(parseFloat(this.data.totalAmount) * 100)

    const params = {
      orderType: this.data.orderType,
      communityId: this.data.userInfo.communityId,
      detailAddress: this.data.userInfo.detailAddress,
      totalAmount: actualAmount,
      actualAmount: actualAmount,
      commissionAmount: actualAmount,
      discountAmount: 0,
      payMethod: 1,
      items: orderItems,
      note: this.data.note
    }

    request({
      url: '/miniapp/order/create',
      method: 'POST',
      data: params
    }).then(res => {
      if (res.code === 200) {
        const orderNo = res.data && res.data.orderNo
        if (!orderNo) {
          wx.showToast({ title: '订单号异常', icon: 'none' })
          setTimeout(() => {
            wx.redirectTo({ url: '/pages/orders/index' })
          }, 800)
          return
        }
        this.payOrder(orderNo)
      } else {
        wx.showToast({ title: res.msg || '下单失败', icon: 'none' })
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
  }
})
