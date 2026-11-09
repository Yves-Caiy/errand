const i18n = require('../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    policyHtml: ''
  },
  onLoad() {
    i18n.applyLocale(this)
    const { request } = require('../../utils/request')
    request({ url: '/miniapp/config/privacyPolicy', method: 'GET' })
      .then((resp) => {
        let txt = ''
        if (typeof resp.data === 'string' && resp.data) {
          txt = resp.data
        } else if (typeof resp.msg === 'string' && resp.msg) {
          txt = resp.msg
        }
        this.renderPolicy(txt)
      })
      .catch(() => {
        this.setData({ policyHtml: '' })
      })
  },
  onShow() {
    i18n.applyLocale(this)
  },

  // 政策正文为后端动态下发的中文，切到韩语时按当前语言翻译后渲染
  renderPolicy(txt) {
    if (!txt) {
      this.setData({ policyHtml: '' })
      return
    }
    if (!i18n.isKo()) {
      this.setData({ policyHtml: txt })
      return
    }
    // 公共翻译接口对含标签的 HTML 会原样返回，先转纯文本再翻译，
    // 译文按换行渲染，避免格式丢失同时也保证翻译质量
    const plain = this.htmlToText(txt)
    i18n.translate(plain, 'zh', 'ko').then((list) => {
      const translated = (list && list[0]) || plain
      this.setData({ policyHtml: translated.replace(/\n/g, '<br/>') })
    })
  },

  // 将 HTML 正文转为纯文本，块级标签与换行转为换行符
  htmlToText(html) {
    if (!html) return ''
    return html
      .replace(/<(script|style)[^>]*>[\s\S]*?<\/\1>/gi, '')
      .replace(/<br\s*\/?>/gi, '\n')
      .replace(/<\/(p|div|h[1-6]|li|tr|section|article|blockquote)>/gi, '\n')
      .replace(/<[^>]+>/g, '')
      .replace(/&nbsp;/gi, ' ')
      .replace(/&lt;/gi, '<')
      .replace(/&gt;/gi, '>')
      .replace(/&quot;/gi, '"')
      .replace(/&#39;/gi, "'")
      .replace(/&amp;/gi, '&')
      .replace(/\n{3,}/g, '\n\n')
      .trim()
  },

  onBack() {
    const pages = getCurrentPages()
    if (pages && pages.length > 1) {
      wx.navigateBack({ delta: 1 })
    } else {
      wx.redirectTo({ url: '/pages/login/login' })
    }
  }
})
