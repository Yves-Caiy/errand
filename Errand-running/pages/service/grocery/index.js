const { request } = require('../../../utils/request')
const i18n = require('../../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    address: '',
    items: '',
    budget: '',
    remark: ''
  },
  onLoad() {
    i18n.applyLocale(this)
  },
  onShow() {
    i18n.applyLocale(this)
  },
  onInput(e) {
    const key = e.currentTarget.dataset.key
    this.setData({ [key]: e.detail.value })
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
    const bound = await this.checkCommunityBinding()
    if (!bound) return
    const { address, items, budget, remark } = this.data
    if (!address || !address.trim()) {
      wx.showToast({ title: i18n.t('请填写地址'), icon: 'none' })
      return
    }
    if (!items || !items.trim()) {
      wx.showToast({ title: i18n.t('请填写要购买的菜品'), icon: 'none' })
      return
    }
    const budgetVal = parseFloat(budget)
    if (isNaN(budgetVal) || budgetVal <= 0) {
      wx.showToast({ title: i18n.t('请填写正确的预算金额'), icon: 'none' })
      return
    }
    const amountInCents = Math.round(budgetVal * 100)
    const noteParts = []
    noteParts.push(`菜品：${items}`)
    noteParts.push(`预算：${budgetVal.toFixed(2)}元`)
    if (remark && remark.trim()) {
      noteParts.push(`备注：${remark}`)
    }
    const finalNote = noteParts.join('\n')
    const payload = {
      detailAddress: address,
      serviceType: '代买菜',
      specName: '代买菜',
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
  onBack() { wx.navigateBack() }
})
