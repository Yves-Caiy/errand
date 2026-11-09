const i18n = require('../../utils/i18n')

Component({
  properties: {
    images: {
      type: Array,
      value: []
    },
    height: {
      type: Number,
      value: 200
    },
    autoplay: {
      type: Boolean,
      value: true
    },
    interval: {
      type: Number,
      value: 3000
    }
  },
  data: {
    locale: 'zh',
    defaultImage:
      'https://ts1.tc.mm.bing.net/th/id/R-C.987f582c510be58755c4933cda68d525?rik=C0D21hJDYvXosw&riu=http%3a%2f%2fimg.pconline.com.cn%2fimages%2fupload%2fupc%2ftx%2fwallpaper%2f1305%2f16%2fc4%2f20990657_1368686545122.jpg&ehk=netN2qzcCVS4ALUQfDOwxAwFcy41oxC%2b0xTFvOYy5ds%3d&risl=&pid=ImgRaw&r=0'
  },
  lifetimes: {
    attached() {
      i18n.applyLocale(this)
      // If no images provided, use 4 copies of the default image for testing
      if (!this.data.images || this.data.images.length === 0) {
        const imgs = new Array(4).fill(this.data.defaultImage)
        this.setData({ images: imgs })
      }
    }
  },
  pageLifetimes: {
    show() {
      i18n.applyLocale(this)
    }
  },
  methods: {}
})