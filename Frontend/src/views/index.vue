<template>
  <div class="app-container home">
    <el-row :gutter="10">
      <el-col :span="6" class="card-box">
        <el-card>
          <template #header><Collection style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">总统计</span></template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%;">
              <thead>
                <tr>
                  <th class="el-table__cell is-leaf"><div class="cell">指标</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">数值</div></th>
                </tr>
              </thead>
              <tbody>
                <tr><td class="el-table__cell is-leaf"><div class="cell">订单总数</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.total.orders }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">进行中</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.total.progressing }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">已完成</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.total.completed }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">已取消</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.total.cancelled }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">收入金额</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.total.revenue }}元</div></td></tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6" class="card-box">
        <el-card>
          <template #header><Document style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">今日统计</span></template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%;">
              <thead>
                <tr>
                  <th class="el-table__cell is-leaf"><div class="cell">指标</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">数值</div></th>
                </tr>
              </thead>
              <tbody>
                <tr><td class="el-table__cell is-leaf"><div class="cell">订单总数</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.today.orders }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">进行中</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.today.progressing }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">已完成</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.today.completed }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">已取消</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.today.cancelled }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">收入金额</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.today.revenue }}元</div></td></tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6" class="card-box">
        <el-card>
          <template #header><MessageBox style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">昨日统计</span></template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%;">
              <thead>
                <tr>
                  <th class="el-table__cell is-leaf"><div class="cell">指标</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">数值</div></th>
                </tr>
              </thead>
              <tbody>
                <tr><td class="el-table__cell is-leaf"><div class="cell">订单总数</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.yesterday.orders }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">进行中</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.yesterday.progressing }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">已完成</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.yesterday.completed }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">已取消</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.yesterday.cancelled }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">收入金额</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.yesterday.revenue }}元</div></td></tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6" class="card-box">
        <el-card>
          <template #header><Collection style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">本月统计</span></template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%;">
              <thead>
                <tr>
                  <th class="el-table__cell is-leaf"><div class="cell">指标</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">数值</div></th>
                </tr>
              </thead>
              <tbody>
                <tr><td class="el-table__cell is-leaf"><div class="cell">订单总数</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.month.orders }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">进行中</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.month.progressing }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">已完成</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.month.completed }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">已取消</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.month.cancelled }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">收入金额</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ stats.month.revenue }}元</div></td></tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <el-col :span="6" class="card-box">
        <el-card>
          <template #header><Collection style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">外卖订单</span></template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%;">
              <thead>
                <tr>
                  <th class="el-table__cell is-leaf"><div class="cell">指标</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">数值</div></th>
                </tr>
              </thead>
              <tbody>
                <tr><td class="el-table__cell is-leaf"><div class="cell">订单数量</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.takeaway.count }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">订单总收入</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.takeaway.revenueTotal }}元</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">月收入</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.takeaway.revenueMonth }}元</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">日收入</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.takeaway.revenueToday }}元</div></td></tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="card-box">
        <el-card>
          <template #header><Collection style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">超市订单</span></template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%;">
              <thead>
                <tr>
                  <th class="el-table__cell is-leaf"><div class="cell">指标</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">数值</div></th>
                </tr>
              </thead>
              <tbody>
                <tr><td class="el-table__cell is-leaf"><div class="cell">订单数量</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.convenience.count }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">订单总收入</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.convenience.revenueTotal }}元</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">月收入</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.convenience.revenueMonth }}元</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">日收入</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.convenience.revenueToday }}元</div></td></tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="card-box">
        <el-card>
          <template #header><Collection style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">套餐订单</span></template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%;">
              <thead>
                <tr>
                  <th class="el-table__cell is-leaf"><div class="cell">指标</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">数值</div></th>
                </tr>
              </thead>
              <tbody>
                <tr><td class="el-table__cell is-leaf"><div class="cell">订单数量</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.package.count }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">订单总收入</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.package.revenueTotal }}元</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">月收入</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.package.revenueMonth }}元</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">日收入</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.package.revenueToday }}元</div></td></tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="card-box">
        <el-card>
          <template #header><Collection style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">服务订单</span></template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%;">
              <thead>
                <tr>
                  <th class="el-table__cell is-leaf"><div class="cell">指标</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">数值</div></th>
                </tr>
              </thead>
              <tbody>
                <tr><td class="el-table__cell is-leaf"><div class="cell">订单数量</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.service.count }}</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">订单总收入</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.service.revenueTotal }}元</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">月收入</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.service.revenueMonth }}元</div></td></tr>
                <tr><td class="el-table__cell is-leaf"><div class="cell">日收入</div></td><td class="el-table__cell is-leaf"><div class="cell">{{ typeStats.service.revenueToday }}元</div></td></tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <el-col :span="12" class="card-box">
        <el-card>
          <template #header>
            <span style="vertical-align: middle;">订单量</span>
            <el-radio-group v-model="range" size="small" style="float: right;">
              <el-radio-button label="today">今日</el-radio-button>
              <el-radio-button label="7">近七日</el-radio-button>
              <el-radio-button label="30">近三十日</el-radio-button>
            </el-radio-group>
          </template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="orderChart" style="height: 420px" />
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card>
          <template #header>
            <span style="vertical-align: middle;">收入统计</span>
            <el-radio-group v-model="range" size="small" style="float: right;">
              <el-radio-button label="today">今日</el-radio-button>
              <el-radio-button label="7">近七日</el-radio-button>
              <el-radio-button label="30">近三十日</el-radio-button>
            </el-radio-group>
          </template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="revenueChart" style="height: 420px" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <el-col :span="12" class="card-box">
        <el-card>
          <template #header><PieChart style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">订单状态分布</span></template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="statusChart" style="height: 420px" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12" class="card-box">
        <el-card>
          <template #header><PieChart style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">订单类型分布</span></template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="typeChart" style="height: 420px" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
import * as echarts from 'echarts'
import request from '@/utils/request'

const stats = reactive({
  total: { orders: 0, progressing: 0, completed: 0, cancelled: 0, revenue: 0 },
  today: { orders: 0, progressing: 0, completed: 0, cancelled: 0, revenue: 0 },
  yesterday: { orders: 0, progressing: 0, completed: 0, cancelled: 0, revenue: 0 },
  month: { orders: 0, progressing: 0, completed: 0, cancelled: 0, revenue: 0 }
})

function toYuan(v) {
  if (v == null) return 0
  return Math.round(Number(v)) / 100
}

function mapStats(dto) {
  if (!dto) return { orders: 0, progressing: 0, completed: 0, cancelled: 0, revenue: 0 }
  return {
    orders: dto.orders || 0,
    progressing: dto.progressing || 0,
    completed: dto.completed || 0,
    cancelled: dto.cancelled || 0,
    revenue: toYuan(dto.revenue)
  }
}

function loadStats() {
  request({
    url: '/errand/stats/overview',
    method: 'get'
  }).then(res => {
    const d = res.data || {}
    stats.total = mapStats(d.total)
    stats.today = mapStats(d.today)
    stats.yesterday = mapStats(d.yesterday)
    stats.month = mapStats(d.month)
  })
}

function loadTypeStats() {
  request({
    url: '/errand/stats/type',
    method: 'get'
  }).then(res => {
    const arr = Array.isArray(res.data) ? res.data : []
    const byType = {}
    arr.forEach(item => {
      byType[item.orderType] = item
    })
    const t1 = byType[1] || {}
    const t2 = byType[2] || {}
    const t3 = byType[3] || {}
    const t4 = byType[4] || {}
    typeStats.takeaway = {
      count: t1.count || 0,
      revenueTotal: toYuan(t1.revenueTotal || 0),
      revenueMonth: toYuan(t1.revenueMonth || 0),
      revenueToday: toYuan(t1.revenueToday || 0)
    }
    typeStats.convenience = {
      count: t2.count || 0,
      revenueTotal: toYuan(t2.revenueTotal || 0),
      revenueMonth: toYuan(t2.revenueMonth || 0),
      revenueToday: toYuan(t2.revenueToday || 0)
    }
    typeStats.package = {
      count: t3.count || 0,
      revenueTotal: toYuan(t3.revenueTotal || 0),
      revenueMonth: toYuan(t3.revenueMonth || 0),
      revenueToday: toYuan(t3.revenueToday || 0)
    }
    typeStats.service = {
      count: t4.count || 0,
      revenueTotal: toYuan(t4.revenueTotal || 0),
      revenueMonth: toYuan(t4.revenueMonth || 0),
      revenueToday: toYuan(t4.revenueToday || 0)
    }
  })
}

function loadOrderTrend() {
  request({
    url: '/errand/stats/orderTrend',
    method: 'get',
    params: { range: range.value }
  }).then(res => {
    const d = res.data || {}
    const x = Array.isArray(d.x) ? d.x : []
    const y = Array.isArray(d.y) ? d.y.map(v => Number(v) || 0) : []
    orderData[range.value] = { x, y }
    updateCharts()
  })
}

function loadRevenueTrend() {
  request({
    url: '/errand/stats/revenueTrend',
    method: 'get',
    params: { range: range.value }
  }).then(res => {
    const d = res.data || {}
    const x = Array.isArray(d.x) ? d.x : []
    const y = Array.isArray(d.y) ? d.y.map(v => toYuan(v)) : []
    revenueData[range.value] = { x, y }
    updateCharts()
  })
}

function loadStatusDist() {
  request({
    url: '/errand/stats/statusDist',
    method: 'get',
  }).then(res => {
    const arr = Array.isArray(res.data) ? res.data : []
    statusData[range.value] = arr.map(i => ({ name: i.name, value: Number(i.value) || 0 }))
    updateCharts()
  })
}

function loadTypeDist() {
  request({
    url: '/errand/stats/typeDist',
    method: 'get',
  }).then(res => {
    const arr = Array.isArray(res.data) ? res.data : []
    typeDistData[range.value] = arr.map(i => ({ name: i.name, value: Number(i.value) || 0 }))
    updateCharts()
  })
}

const range = ref('today')
const orderChart = ref(null)
const revenueChart = ref(null)
const statusChart = ref(null)
const typeChart = ref(null)
const orderInstance = ref(null)
const revenueInstance = ref(null)
const statusInstance = ref(null)
const typeInstance = ref(null)

const orderData = {
  today: {
    x: ['00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23'],
    y: [5,4,3,3,2,2,4,8,12,18,24,28,30,26,22,20,18,24,30,34,28,20,14,10]
  },
  '7': {
    x: ['周一','周二','周三','周四','周五','周六','周日'],
    y: [520,580,600,640,650,680,620]
  },
  '30': {
    x: ['01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30'],
    y: [410,420,430,440,450,460,480,500,520,540,560,580,600,620,640,660,680,700,720,740,760,780,800,820,840,860,880,900,920,940]
  }
}

const revenueData = {
  today: {
    x: ['00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23'],
    y: [1000,800,600,500,500,600,800,1500,2400,3200,4000,4600,5200,4800,4200,3800,3600,4200,5200,5800,5000,4200,3200,2400]
  },
  '7': {
    x: ['周一','周二','周三','周四','周五','周六','周日'],
    y: [72000,78000,82000,90000,95000,98000,88000]
  },
  '30': {
    x: ['01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30'],
    y: [62000,63000,64000,65000,66000,68000,70000,72000,74000,76000,78000,80000,82000,84000,86000,88000,90000,92000,94000,96000,98000,100000,102000,104000,106000,108000,110000,112000,114000,116000]
  }
}

const statusData = {
  today: [
    { name: '进行中', value: 45 },
    { name: '已完成', value: 560 },
    { name: '已取消', value: 15 }
  ],
  '7': [
    { name: '进行中', value: 300 },
    { name: '已完成', value: 4100 },
    { name: '已取消', value: 180 }
  ],
  '30': [
    { name: '进行中', value: 240 },
    { name: '已完成', value: 12100 },
    { name: '已取消', value: 460 }
  ]
}

const typeDistData = {
  today: [
    { name: '外卖订单', value: 220 },
    { name: '超市订单', value: 180 },
    { name: '套餐订单', value: 80 },
    { name: '服务订单', value: 140 }
  ],
  '7': [
    { name: '外卖订单', value: 1600 },
    { name: '超市订单', value: 1450 },
    { name: '套餐订单', value: 390 },
    { name: '服务订单', value: 850 }
  ],
  '30': [
    { name: '外卖订单', value: 4800 },
    { name: '超市订单', value: 4200 },
    { name: '套餐订单', value: 1200 },
    { name: '服务订单', value: 2600 }
  ]
}

const typeStats = reactive({
  takeaway: { count: 6500, revenueTotal: 950000, revenueMonth: 640000, revenueToday: 30000 },
  convenience: { count: 6000, revenueTotal: 850000, revenueMonth: 620000, revenueToday: 26000 },
  package: { count: 1800, revenueTotal: 450000, revenueMonth: 320000, revenueToday: 12000 },
  service: { count: 4350, revenueTotal: 504000, revenueMonth: 256000, revenueToday: 16500 }
})

function updateCharts() {
  const r = range.value
  orderInstance.value.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', boundaryGap: false, data: orderData[r].x },
    yAxis: { type: 'value' },
    series: [{ name: '订单量', type: 'line', smooth: true, data: orderData[r].y }]
  })
  revenueInstance.value.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', boundaryGap: false, data: revenueData[r].x },
    yAxis: { type: 'value' },
    series: [{ name: '收入金额', type: 'line', smooth: true, areaStyle: {}, data: revenueData[r].y }]
  })
  statusInstance.value.setOption({
    tooltip: { trigger: 'item', formatter: '{a} <br/>{b} : {c} ({d}%)' },
    series: [{ name: '订单状态', type: 'pie', radius: ['20%','60%'], center: ['50%','45%'], data: statusData[r], animationEasing: 'cubicInOut', animationDuration: 1000 }]
  })
  typeInstance.value.setOption({
    tooltip: { trigger: 'item', formatter: '{a} <br/>{b} : {c} ({d}%)' },
    series: [{ name: '订单类型', type: 'pie', radius: ['20%','60%'], center: ['50%','45%'], data: typeDistData[r], animationEasing: 'cubicInOut', animationDuration: 1000 }]
  })
}

let resizeHandler = null
onMounted(() => {
  loadStats()
  loadTypeStats()
  orderInstance.value = echarts.init(orderChart.value, 'macarons')
  revenueInstance.value = echarts.init(revenueChart.value, 'macarons')
  statusInstance.value = echarts.init(statusChart.value, 'macarons')
  typeInstance.value = echarts.init(typeChart.value, 'macarons')
  loadOrderTrend()
  loadRevenueTrend()
  loadStatusDist()
  loadTypeDist()
  updateCharts()
  resizeHandler = () => {
    orderInstance.value && orderInstance.value.resize()
    revenueInstance.value && revenueInstance.value.resize()
    statusInstance.value && statusInstance.value.resize()
    typeInstance.value && typeInstance.value.resize()
  }
  window.addEventListener('resize', resizeHandler)
})

watch(range, () => {
  loadOrderTrend()
  loadRevenueTrend()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeHandler)
  orderInstance.value && orderInstance.value.dispose()
  revenueInstance.value && revenueInstance.value.dispose()
  statusInstance.value && statusInstance.value.dispose()
  typeInstance.value && typeInstance.value.dispose()
})
</script>

<style scoped lang="scss">
.home .card-box {
  margin-bottom: 10px;
}
</style>

