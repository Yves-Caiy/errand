// pages/takout/order/index.js
const { request, BASE_URL } = require('../../../utils/request')

Page({
  data: {
    shop: { id: 0, name: '', desc: '', hours: '09:00 - 22:00', avatarUrl: '' },
    categories: [],
    activeCategory: '',
    dishes: [],
    filteredDishes: [],
    
    // 购物车相关
    cartList: [],
    cartCount: 0,
    totalPrice: 0,
    cartVisible: false,
    cartX: 300, // 默认位置
    cartY: 500
  },

  onLoad(query) {
    const id = Number(query?.id || 0)
    const name = query?.name ? decodeURIComponent(query.name) : ''
    const desc = query?.desc ? decodeURIComponent(query.desc) : ''
    const avatar = query?.avatar ? decodeURIComponent(query.avatar) : ''
    
    // 如果 URL 传了参数则优先使用，否则使用模拟数据
    let shop = { id, hours: '09:00 - 22:00', avatarUrl: avatar || '/static/icons/service.svg' }
    if (name) {
      shop.name = name
      shop.desc = desc
    } else {
      const shopMap = {
        1: { name: '小馄饨外卖店', desc: '热乎馄饨，现煮现送，校园免配送费' },
        2: { name: '阿强麻辣烫', desc: '麻辣烫、冒菜、凉面，支持自选配菜' },
        3: { name: '王阿姨家常菜', desc: '家常快餐，量足实惠，套餐任选' },
        4: { name: '咖啡小站', desc: '手冲咖啡、轻食三明治，工作学习好伴侣' }
      }
      const mapData = shopMap[id] || { name: '外卖商家', desc: '欢迎光临' }
      shop.name = mapData.name
      shop.desc = mapData.desc
    }
    
    this.setData({ shop })
    this.loadProducts(id)
    this.initCartPosition()
  },

  onShow() {
    this.getCartList()
  },

  initCartPosition() {
    const sys = wx.getSystemInfoSync()
    this.setData({
      cartX: sys.windowWidth - 70,
      cartY: sys.windowHeight - 150
    })
  },

  loadProducts(storeId) {
    request({
      url: '/miniapp/takeawayProduct/list',
      data: { storeId }
    }).then(res => {
      if (res.code === 200) {
        const list = res.data || []
        const dishes = list.map(item => ({
          id: item.id,
          cat: item.productType,
          name: item.productName,
          desc: '', // 后端未返回描述
          price: (item.price / 100).toFixed(2), // 转为元
          img: this.handleImgUrl(item.imageUrl)
        }))

        // 提取分类
        const catSet = new Set(dishes.map(d => d.cat).filter(t => t))
        const categories = Array.from(catSet).map(type => ({
          name: type
        }))

        this.setData({
          dishes,
          categories,
          activeCategory: categories.length > 0 ? categories[0].name : ''
        }, () => {
          this.refreshDishes()
        })
      }
    })
  },

  handleImgUrl(url) {
    if (!url) return ''
    if (url.startsWith('http')) return url
    return BASE_URL + url
  },

  refreshDishes() {
    const list = this.data.dishes.filter(d => d.cat === this.data.activeCategory)
    this.setData({ filteredDishes: list })
  },

  onCategoryTap(e) {
    const name = e.currentTarget.dataset.name
    this.setData({ activeCategory: name })
    this.refreshDishes()
  },

  // --- 购物车逻辑 ---

  getCartList() {
    request({
      url: '/miniapp/cart/list',
      data: { productType: 2 } // 2=外卖
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
      url: '/miniapp/cart/addTakeout',
      method: 'POST',
      data: {
        productId: id,
        quantity: 1,
        storeId: this.data.shop.id
      }
    }).then(res => {
      if (res.code === 200) {
        wx.showToast({ title: '已加入购物车', icon: 'none' })
        this.getCartList()
      } else {
        wx.showToast({ title: res.msg || '添加失败', icon: 'none' })
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
        title: '提示',
        content: '确定要删除该商品吗？',
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
      title: '提示',
      content: '确定清空购物车吗？',
      success: (res) => {
        if (res.confirm) {
          const ids = this.data.cartList.map(item => item.id)
          this.deleteCartItem(ids.join(','))
        }
      }
    })
  },

  onSubmit() {
    if (this.data.cartList.length === 0) {
      wx.showToast({ title: '购物车是空的', icon: 'none' })
      return
    }
    const hasSelected = this.data.cartList.some(item => item.isSelected)
    if (!hasSelected) {
      wx.showToast({ title: '请选择商品', icon: 'none' })
      return
    }
    
    const selectedItems = this.data.cartList.filter(item => item.isSelected)
    const total = this.data.totalPrice

    wx.navigateTo({
      url: '/pages/takout/confirm/index?type=1',
      success: (res) => {
        res.eventChannel.emit('acceptDataFromOpenerPage', {
          items: selectedItems,
          totalAmount: total,
          storeId: this.data.shop.id,
          type: 1 // 外卖
        })
      }
    })
  },

  onBack() {
    wx.navigateBack({ delta: 1 })
  }
})
