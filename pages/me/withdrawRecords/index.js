const { request } = require('../../../utils/request')
const i18n = require('../../../utils/i18n')

Page({
  data: {
    locale: 'zh',
    records: [],
    loading: false,
    statusFilter: -1,
    pageNum: 1,
    pageSize: 10,
    hasMore: true
  },
  onLoad() {
    i18n.applyLocale(this)
    this.fetchList(true)
  },
  onShow() {
    i18n.applyLocale(this)
  },
  onReachBottom() {
    if (this.data.loading || !this.data.hasMore) return
    this.setData({ pageNum: this.data.pageNum + 1 })
    this.fetchList(false)
  },
  onFilterTap(e) {
    const status = Number(e.currentTarget.dataset.status)
    this.setData({ statusFilter: status, pageNum: 1, hasMore: true, records: [] })
    this.fetchList(true)
  },
  fetchList(reset) {
    this.setData({ loading: true })
    const params = {
      pageNum: this.data.pageNum,
      pageSize: this.data.pageSize
    }
    if (this.data.statusFilter !== -1) {
      params.status = this.data.statusFilter
    }
    request({
      url: '/miniapp/pay/withdraw/list',
      method: 'GET',
      data: params
    }).then(res => {
      const rows = Array.isArray(res.data) ? res.data : (res.rows || [])
      const mapped = rows.map(it => ({
        ...it,
        amountYuan: ((Number(it.amount) || 0) / 100).toFixed(2),
        creditedAmountYuan: ((Number(it.creditedAmount) || 0) / 100).toFixed(2),
        statusText: this.statusText(it.status),
        createTimeText: this.formatLocalTime(it.createTime)
      }))
      const newList = reset ? mapped : this.data.records.concat(mapped)
      const total = typeof res.total === 'number' ? res.total : newList.length
      const hasMore = newList.length < total
      this.setData({ records: newList, hasMore })
    }).finally(() => {
      this.setData({ loading: false })
    })
  },
  statusText(s) {
    const m = { 0: i18n.t('申请中'), 1: i18n.t('审核通过'), 2: i18n.t('审核拒绝'), 3: i18n.t('已打款') }
    return m[Number(s)] || s
  },
  formatLocalTime(t) {
    if (!t) return ''
    try {
      const d = new Date(t)
      const y = d.getFullYear()
      const M = String(d.getMonth() + 1).padStart(2, '0')
      const D = String(d.getDate()).padStart(2, '0')
      const h = String(d.getHours()).padStart(2, '0')
      const m = String(d.getMinutes()).padStart(2, '0')
      return `${y}-${M}-${D} ${h}:${m}`
    } catch (e) {
      return String(t)
    }
  }
})
