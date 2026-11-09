<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px" class="mb8">
      <el-form-item label="openid" prop="openid">
        <el-input v-model="queryParams.openid" placeholder="请输入openid" clearable style="width: 240px;" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 240px;">
          <el-option v-for="d in statusOptions" :key="d.value" :label="d.label" :value="d.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['errand:withdrawApply:export']"
        >导出申请通过未打款列表</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange" style="width: 100%">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="openid" align="center" prop="userOpenid" :min-width="420">
        <template #default="scope">
          <span class="nowrap">{{ scope.row.userOpenid }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户昵称" align="center" prop="userNickname" />
      <el-table-column label="联系方式" align="center" prop="userPhone" />
      <el-table-column label="提现金额(元)" align="center" prop="amount">
        <template #default="scope">
          <span>{{ fmtAmount(scope.row.amount) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到账金额(元)" align="center" prop="creditedAmount">
        <template #default="scope">
          <span>{{ fmtAmount(scope.row.creditedAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="比率(%)" align="center" prop="rate" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <span>{{ statusLabel(scope.row.status) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template #default="scope">
          <template v-if="Number(scope.row.status) === 0">
            <el-button link type="success" @click="handleApprove(scope.row)">审核通过</el-button>
            <el-button link type="danger" @click="handleReject(scope.row)">审核拒绝</el-button>
          </template>
          <template v-else-if="Number(scope.row.status) === 1 && Number(scope.row.payStatus) !== 1">
            <el-button link type="primary" @click="handlePay(scope.row)">确认打款</el-button>
          </template>
          <template v-else-if="Number(scope.row.status) === 2">
            <el-button link disabled>审核通过</el-button>
            <el-button link disabled>审核拒绝</el-button>
          </template>
          <template v-else>
            <el-button link disabled>已打款</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </div>
  </template>

<script setup name="WithdrawApply">
import { listWithdrawApply, approveWithdrawApply, rejectWithdrawApply, payWithdrawApply } from '@/api/errand/withdrawApply'

const { proxy } = getCurrentInstance()

const statusOptions = [
  { value: 0, label: '申请中' },
  { value: 1, label: '审核通过' },
  { value: 2, label: '审核拒绝' },
  { value: 3, label: '已打款' }
]
function statusLabel(v) {
  const val = Number(v)
  const m = { 0: '申请中', 1: '审核通过', 2: '审核拒绝', 3: '已打款' }
  return m[val] ?? v
}
function fmtAmount(v) {
  if (v == null) return v
  return (Number(v) || 0) / 100
}

const list = ref([])
const loading = ref(false)
const showSearch = ref(true)
const ids = ref([])
const total = ref(0)

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  status: undefined,
  openid: undefined
})

function getList() {
  loading.value = true
  listWithdrawApply(queryParams.value).then(res => {
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

function handleApprove(row) {
  proxy.$modal.confirm('确认审核通过该申请？').then(() => {
    approveWithdrawApply(row.id).then(res => {
      const msg = res.msg || '已提交审核通过（占位）'
      proxy.$modal.msgSuccess(msg)
      getList()
    }).catch(() => {
      proxy.$modal.msgError('审核通过失败')
    })
  })
}

function handleReject(row) {
  proxy.$modal.confirm('确认审核拒绝该申请？').then(() => {
    rejectWithdrawApply(row.id).then(() => {
      proxy.$modal.msgSuccess('已拒绝')
      getList()
    }).catch(() => {
      proxy.$modal.msgError('审核拒绝失败')
    })
  })
}

function handlePay(row) {
  proxy.$modal.confirm('确认已完成人工打款？').then(() => {
    payWithdrawApply(row.id).then(() => {
      proxy.$modal.msgSuccess('已确认打款')
      getList()
    }).catch(() => {
      proxy.$modal.msgError('确认打款失败')
    })
  })
}

function handleExport() {
  proxy.download('/errand/withdrawApply/export', {}, `withdraw_${new Date().getTime()}.xlsx`)
}

getList()
</script>

<style scoped>
.mb8 { margin-bottom: 8px; }
.ml8 { margin-left: 8px; }
.nowrap { white-space: nowrap; }
</style>
