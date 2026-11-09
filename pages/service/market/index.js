const { request, BASE_URL } = require('../../../utils/request')
const i18n = require('../../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    shopName: '',
    categories: [],
    goods: [],
    activeCat: '',
    filteredGoods: [],
    
    // 购物车相关
    cartList: [],
    cartCount: 0,
    totalPrice: 0,
    cartVisible: false,
    cartX: 300, // 默认位置
    cartY: 500
  },
  onLoad() {
    i18n.applyLocale(this)
    this.setShopName()
    this.loadProducts()
    this.initCartPosition()
  },
  onShow() {
    i18n.applyLocale(this)
    this.getCartList()
  },
  setShopName() {
    let name = ''
    try { name = wx.getStorageSync('current_community_name') || '' } catch (e) {}
    const display = name ? (name + i18n.t('便民超市')) : i18n.t('校园便民超市')
    this.setData({ shopName: display })
  },
  initCartPosition() {
    const sys = wx.getSystemInfoSync()
    this.setData({
      cartX: sys.windowWidth - 70,
      cartY: sys.windowHeight - 150
    })
  },
  loadProducts() {
    request({
      url: '/miniapp/convenienceProduct/list'
    }).then(res => {
      if (res.code === 200) {
        const list = res.data || []
        const goods = list.map(item => ({
          id: item.id,
          catId: item.productType,
          name: item.productName,
          desc: '', 
          price: (item.price / 100).toFixed(2), // 转为元
          img: this.handleImgUrl(item.imageUrl)
        }))

        // 提取分类
        const catSet = new Set(goods.map(g => g.catId).filter(t => t))
        const categories = Array.from(catSet).map(type => ({
          id: type,
          name: type // 如果有类型名称映射更好，这里直接用ID或字段
        }))

        this.setData({
          goods,
          categories,
          activeCat: categories.length > 0 ? categories[0].id : ''
        }, () => {
          this.refreshGoods()
        })
      }
    })
  },
  handleImgUrl(url) {
    if (!url) return ''
    if (url.startsWith('http')) return url
    return BASE_URL + url
  },
  refreshGoods() {
    const list = this.data.goods.filter(g => g.catId === this.data.activeCat)
    this.setData({ filteredGoods: list })
  },
  onCategoryTap(e) {
    const id = e.currentTarget.dataset.id
    this.setData({ activeCat: id }, () => this.refreshGoods())
  },

  // --- 购物车逻辑 ---

  getCartList() {
    request({
      url: '/miniapp/cart/list',
      data: { productType: 1 } // 1=超市
    }).then(res => {
      if (res.code === 200) {
        const list = res.data || []
        // 处理图片
        list.forEach(item => {
          item.imageUrl = this.handleImgUrl(item.imageUrl)
        })
        
        // 计算总数
        const count = list.reduce((sum, item) => sum + item.quantity, 0)
        
        this.setData({
          cartList: list,
          cartCount: count
        })
        this.calculateTotal()
      }
    })
  },

  calculateTotal() {
    const total = this.data.cartList.reduce((sum, item) => {
      if (item.isSelected) {
        return sum + (item.price * item.quantity)
      }
      return sum
    }, 0)
    this.setData({ totalPrice: (total / 100).toFixed(2) })
  },

  // 添加购物车（列表页点击）
  onAddTap(e) {
    const id = Number(e.currentTarget.dataset.id)
    request({
      url: '/miniapp/cart/addSupermarket',
      method: 'POST',
      data: {
        productId: id,
        quantity: 1
      }
    }).then(res => {
      if (res.code === 200) {
        wx.showToast({ title: i18n.t('已加入购物车'), icon: 'none' })
        this.getCartList()
      } else {
        wx.showToast({ title: res.msg || i18n.t('添加失败'), icon: 'none' })
      }
    })
  },

  toggleCart() {
    this.setData({ cartVisible: !this.data.cartVisible })
  },

  // 增加数量
  onPlusQty(e) {
    const item = e.currentTarget.dataset.item
    this.updateCartItem(item.id, item.quantity + 1, item.isSelected)
  },

  // 减少数量
  onMinusQty(e) {
    const item = e.currentTarget.dataset.item
    if (item.quantity > 1) {
      this.updateCartItem(item.id, item.quantity - 1, item.isSelected)
    } else {
      // 删除
      wx.showModal({
        title: i18n.t('提示'),
        content: i18n.t('确定要删除该商品吗？'),
        success: (res) => {
          if (res.confirm) {
            this.deleteCartItem(item.id)
          }
        }
      })
    }
  },

  // 切换选中
  onToggleSelect(e) {
    const index = e.currentTarget.dataset.index
    const item = this.data.cartList[index]
    // 切换状态 1/0 (后端可能是Integer)
    const newStatus = item.isSelected === 1 ? 0 : 1
    this.updateCartItem(item.id, item.quantity, newStatus)
  },

  updateCartItem(id, quantity, isSelected) {
    request({
      url: '/miniapp/cart/update',
      method: 'PUT',
      data: {
        id: id,
        quantity: quantity,
        isSelected: isSelected
      }
    }).then(res => {
      if (res.code === 200) {
        this.getCartList()
      }
    })
  },

  deleteCartItem(id) {
    request({
      url: `/miniapp/cart/${id}`,
      method: 'DELETE'
    }).then(res => {
      if (res.code === 200) {
        this.getCartList()
      }
    })
  },

  onClearCart() {
    if (this.data.cartList.length === 0) return
    wx.showModal({
      title: i18n.t('提示'),
      content: i18n.t('确定清空购物车吗？'),
      success: (res) => {
        if (res.confirm) {
          const ids = this.data.cartList.map(item => item.id)
          this.deleteCartItem(ids.join(','))
        }
      }
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
    if (this.data.cartList.length === 0) {
      wx.showToast({ title: i18n.t('购物车是空的'), icon: 'none' })
      return
    }
    const hasSelected = this.data.cartList.some(item => item.isSelected)
    if (!hasSelected) {
      wx.showToast({ title: i18n.t('请选择商品'), icon: 'none' })
      return
    }

    const selectedItems = this.data.cartList.filter(item => item.isSelected)
    const total = this.data.totalPrice
    const bound = await this.checkCommunityBinding()
    if (!bound) return
    
    wx.navigateTo({
      url: '/pages/takout/confirm/index?type=2',
      success: (res) => {
        res.eventChannel.emit('acceptDataFromOpenerPage', {
          items: selectedItems,
          totalAmount: total,
          storeId: null, 
          type: 2 // 超市
        })
      }
    })
  },
  
  onBack() { wx.navigateBack() }
})
