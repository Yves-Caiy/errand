<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px" class="mb8">
      <el-form-item label="订单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable style="width: 240px;" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input v-model.number="queryParams.userId" placeholder="请输入用户ID" clearable style="width: 240px;" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="接单员ID" prop="runnerId">
        <el-input v-model.number="queryParams.runnerId" placeholder="请输入接单员ID" clearable style="width: 240px;" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="小区" prop="communityId">
        <el-select v-model="queryParams.communityId" placeholder="请选择小区" clearable filterable style="width: 240px;">
          <el-option v-for="c in communityOptions" :key="c.id" :label="c.communityName" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="订单类型" prop="orderType">
        <el-select v-model="queryParams.orderType" placeholder="请选择订单类型" clearable style="width: 240px;">
          <el-option v-for="d in orderTypeOptions" :key="d.value" :label="d.label" :value="d.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="服务类型" prop="serviceType">
        <el-input v-model="queryParams.serviceType" placeholder="请输入服务类型" clearable style="width: 240px;" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择订单状态" clearable style="width: 240px;">
          <el-option v-for="d in orderStatusOptions" :key="d.value" :label="d.label" :value="d.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="支付状态" prop="payStatus">
        <el-select v-model="queryParams.payStatus" placeholder="请选择支付状态" clearable style="width: 240px;">
          <el-option v-for="d in payStatusOptions" :key="d.value" :label="d.label" :value="d.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange" style="width: 100%">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单号" align="center" prop="orderNo" :show-overflow-tooltip="true" />
      <el-table-column label="用户昵称" align="center">
        <template #default="scope">
          <span>{{ nicknameLabel(scope.row.userNickname, scope.row.userId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="接单员昵称" align="center">
        <template #default="scope">
          <span>{{ nicknameLabel(scope.row.runnerNickname, scope.row.runnerId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="小区" align="center" prop="communityId">
        <template #default="scope">
          <span>{{ communityName(scope.row.communityId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单类型" align="center" prop="orderType">
        <template #default="scope">
          <span>{{ orderTypeLabel(scope.row.orderType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderStatus">
        <template #default="scope">
          <span>{{ orderStatusLabel(scope.row.orderStatus) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付状态" align="center" prop="payStatus">
        <template #default="scope">
          <span>{{ payStatusLabel(scope.row.payStatus) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实付金额(元)" align="center" prop="actualAmount">
        <template #default="scope">
          <span>{{ fmtAmount(scope.row.actualAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付方式" align="center" prop="payMethod">
        <template #default="scope">
          <span>{{ payMethodLabel(scope.row.payMethod) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付时间" align="center" prop="payTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.payTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)">查看</el-button>
          <el-button
            link
            type="danger"
            @click="handleRefund(scope.row)"
            v-if="Number(scope.row.payStatus) === 2"
          >退款</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog v-model="openView" title="订单详情" width="1020px" append-to-body>
      <el-descriptions :column="5" border>
        <el-descriptions-item label="订单号">{{ current?.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单类型">{{ orderTypeLabel(current?.orderType) }}</el-descriptions-item>
        <el-descriptions-item label="用户昵称">{{ nicknameLabel(current?.userNickname, current?.userId) }}</el-descriptions-item>
        <el-descriptions-item label="接单员昵称">{{ nicknameLabel(current?.runnerNickname, current?.runnerId) }}</el-descriptions-item>
        <el-descriptions-item label="小区">{{ communityName(current?.communityId) }}</el-descriptions-item>
        <el-descriptions-item label="配送地址">{{ current?.detailAddress }}</el-descriptions-item>
        <el-descriptions-item label="起始地">{{ current?.startAddress }}</el-descriptions-item>
        <el-descriptions-item label="目的地">{{ current?.endAddress }}</el-descriptions-item>
        <el-descriptions-item label="配送地址2">{{ current?.deliveryAddress }}</el-descriptions-item>
        <el-descriptions-item label="描述">{{ current?.itemDesc }}</el-descriptions-item>
        <el-descriptions-item label="预算(元)">{{ fmtAmount(current?.budget) }}</el-descriptions-item>
        <el-descriptions-item label="规格">{{ current?.specName }}</el-descriptions-item>
        <el-descriptions-item label="微信号">{{ current?.wechatId }}</el-descriptions-item>
        <el-descriptions-item label="时长类型">{{ current?.durationType }}</el-descriptions-item>
        <el-descriptions-item label="时长值">{{ current?.durationValue }}</el-descriptions-item>
        <el-descriptions-item label="总金额(元)">{{ fmtAmount(current?.totalAmount) }}</el-descriptions-item>
        <el-descriptions-item label="优惠(元)">{{ fmtAmount(current?.discountAmount) }}</el-descriptions-item>
        <el-descriptions-item label="实付(元)">{{ fmtAmount(current?.actualAmount) }}</el-descriptions-item>
        <el-descriptions-item label="佣金(元)">{{ fmtAmount(current?.commissionAmount) }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">{{ orderStatusLabel(current?.orderStatus) }}</el-descriptions-item>
        <el-descriptions-item label="支付状态">{{ payStatusLabel(current?.payStatus) }}</el-descriptions-item>
        <el-descriptions-item label="支付方式">{{ payMethodLabel(current?.payMethod) }}</el-descriptions-item>
        <el-descriptions-item label="支付时间">{{ parseTime(current?.payTime) }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ parseTime(current?.finishTime) }}</el-descriptions-item>
        <el-descriptions-item label="取消原因">{{ current?.cancelReason }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(current?.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(current?.updateTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="5">{{ current?.note }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">订单项</el-divider>
      <el-table :data="currentItems" size="small">
        <el-table-column label="类型" align="center" prop="itemType" width="160">
          <template #default="scope">
            <span>{{ itemTypeLabel(scope.row.itemType) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="名称" align="center" prop="productName" :show-overflow-tooltip="true" width="160" />
        <el-table-column label="数量" align="center" prop="quantity" width="160" />
        <el-table-column label="单价(元)" align="center" prop="unitPrice" width="160">
          <template #default="scope">
            <span>{{ fmtAmount(scope.row.unitPrice) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="小计(元)" align="center" prop="subtotal" width="160">
          <template #default="scope">
            <span>{{ fmtAmount(scope.row.subtotal) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="openView = false">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Order">
import { listOrder, getOrder, applyRefund } from '@/api/errand/order'
import { listCommunity } from '@/api/errand/community'

const { proxy } = getCurrentInstance()
const { errand_order_status, errand_pay_status } = proxy.useDict('errand_order_status', 'errand_pay_status')

const communityOptions = ref([])
function loadCommunities() {
  listCommunity({ pageNum: 1, pageSize: 1000, isEnabled: 1 }).then(res => {
    communityOptions.value = res.rows || []
  })
}

function communityName(id) {
  if (!id) return ''
  const found = communityOptions.value.find(c => c.id === id)
  return found ? found.communityName : id
}

const orderTypeOptions = [
  { value: 1, label: '外卖' },
  { value: 2, label: '超市' },
  { value: 3, label: '套餐' },
  { value: 4, label: '其他' }
]
const orderStatusOptions = [
  { value: 0, label: '待支付' },
  { value: 1, label: '待接单' },
  { value: 2, label: '已接单' },
  { value: 3, label: '已完成' },
  { value: 4, label: '已取消' }
]
const payStatusOptions = [
  { value: 0, label: '未支付' },
  { value: 1, label: '已支付' },
  { value: 2, label: '退款中' },
  { value: 3, label: '已退款' }
]
function orderTypeLabel(v) {
  const val = Number(v)
  const o = orderTypeOptions.find(i => i.value === val)
  return o ? o.label : v
}
function itemTypeLabel(v) {
  const m = { 1: '外卖', 2: '超市', 3: '套餐' }
  return m[v] || v
}
function payMethodLabel(v) {
  const m = { 1: '微信', 2: '线下' }
  return m[v] || v
}
function orderStatusLabel(v) {
  const m = { 0: '待支付', 1: '待接单', 2: '已接单', 3: '已完成', 4: '已取消' }
  const val = Number(v)
  return m[val] ?? v
}
function payStatusLabel(v) {
  const m = { 0: '未支付', 1: '已支付', 2: '退款中', 3: '已退款' }
  const val = Number(v)
  return m[val] ?? v
}
function fmtAmount(v) {
  if (v == null) return v
  return (Number(v) || 0) / 100
}
function nicknameLabel(nick, id) {
  if (nick) return nick
  if (id == null || id === undefined) return '未接单'
  return id
}

const list = ref([])
const loading = ref(false)
const showSearch = ref(true)
const ids = ref([])
const total = ref(0)
const openView = ref(false)
const current = ref(null)
const currentItems = ref([])

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  orderNo: undefined,
  userId: undefined,
  runnerId: undefined,
  communityId: undefined,
  orderType: undefined,
  serviceType: undefined,
  orderStatus: undefined,
  payStatus: undefined
})

function getList() {
  loading.value = true
  listOrder(queryParams.value).then(res => {
    list.value = res.rows
    total.value = res.total
  }).finally(() => {
    loading.value = false
  })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm('queryRef')
  queryParams.value.pageNum = 1
  getList()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(i => i.id)
}

function handleView(row) {
  getOrder(row.id).then(res => {
    const d = res.data || {}
    current.value = d
    currentItems.value = Array.isArray(d.items) ? d.items : []
    openView.value = true
  })
}

function handleRefund(row) {
  const amountLabel = fmtAmount(row.actualAmount)
  proxy.$modal.confirm(`确认对该订单发起退款？默认按实付金额发起（¥${amountLabel}）`).then(() => {
    applyRefund({ id: row.id }).then(() => {
      proxy.$modal.msgSuccess('已发起退款')
      getList()
    }).catch(() => {
      proxy.$modal.msgError('退款申请失败')
    })
  })
}

loadCommunities()
getList()
</script>

<style scoped>
.mb8 { margin-bottom: 8px; }
.ml8 { margin-left: 8px; }
</style>
