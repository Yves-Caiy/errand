<template>
  <div class="app-container">
    <!-- 派单工具：候选打分预览 + 手动触发 -->
    <el-card class="tool-card" shadow="never">
      <div class="tool-row">
        <span class="tool-label">订单ID</span>
        <el-input v-model="toolOrderId" placeholder="请输入外卖订单ID" clearable style="width: 220px" />
        <el-button type="primary" plain icon="DataAnalysis" :disabled="!toolOrderId" @click="previewCandidates">候选打分预览</el-button>
        <el-button type="warning" plain icon="Promotion" :disabled="!toolOrderId" @click="handleTrigger">手动触发派单</el-button>
        <span class="tool-tip">仅 order_type=1（外卖）且派单开关开启、测试模式关闭时生效</span>
      </div>
    </el-card>

    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="订单ID" prop="orderId">
        <el-input v-model="queryParams.orderId" placeholder="请输入订单ID" clearable style="width: 180px;" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="接单员ID" prop="runnerUserId">
        <el-input v-model="queryParams.runnerUserId" placeholder="请输入接单员用户ID" clearable style="width: 180px;" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="轮次" prop="dispatchRound">
        <el-input-number v-model="queryParams.dispatchRound" :min="1" :max="99" controls-position="right" style="width: 140px" />
      </el-form-item>
      <el-form-item label="记录状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 160px;">
          <el-option v-for="(label, key) in statusMap" :key="key" :label="label" :value="Number(key)" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="List" :disabled="!single" @click="handleViewTrajectory()">查看轨迹</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单号" align="center" prop="orderNo" :show-overflow-tooltip="true" />
      <el-table-column label="订单ID" align="center" prop="orderId" width="90" />
      <el-table-column label="接单员" align="center" width="150">
        <template #default="scope">
          <span>{{ scope.row.runnerNickname || ('用户#' + scope.row.runnerUserId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="得分" align="center" width="90">
        <template #default="scope">
          <span class="score">{{ scope.row.score != null ? scope.row.score : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="轮次" align="center" prop="dispatchRound" width="80" />
      <el-table-column label="派单类型" align="center" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.dispatchType === 2 ? 'warning' : 'primary'" effect="plain">
            {{ typeMap[scope.row.dispatchType] || scope.row.dispatchType }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="记录状态" align="center" width="110">
        <template #default="scope">
          <el-tag :type="statusTagType(scope.row.status)" effect="light">
            {{ statusMap[scope.row.status] || scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="时间" align="center" width="170">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="110">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleViewTrajectory(scope.row)">轨迹</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 候选打分预览 -->
    <el-dialog v-model="openPreview" title="候选接单员打分预览" width="880px" append-to-body>
      <el-alert
        type="info"
        :closable="false"
        show-icon
        title="按订单小区与取货店铺小区筛选候选，并给出六维加权得分（不写库、不改单）"
        style="margin-bottom: 12px"
      />
      <el-table :data="candidates" v-loading="previewLoading" size="small">
        <el-table-column label="排名" align="center" prop="rank" width="70" />
        <el-table-column label="接单员" align="center" width="160">
          <template #default="scope">
            <span>{{ scope.row.nickname || ('用户#' + scope.row.userId) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="总分" align="center" prop="score" width="80">
          <template #default="scope"><span class="score">{{ scope.row.score }}</span></template>
        </el-table-column>
        <el-table-column label="小区(25)" align="center" width="100">
          <template #default="scope">{{ dimScore(scope.row, 's_comm', 25) }}</template>
        </el-table-column>
        <el-table-column label="店铺(15)" align="center" width="100">
          <template #default="scope">{{ dimScore(scope.row, 's_store', 15) }}</template>
        </el-table-column>
        <el-table-column label="负荷(25)" align="center" width="100">
          <template #default="scope">{{ dimScore(scope.row, 's_load', 25) }}</template>
        </el-table-column>
        <el-table-column label="履约(20)" align="center" width="100">
          <template #default="scope">{{ dimScore(scope.row, 's_deliver', 20) }}</template>
        </el-table-column>
        <el-table-column label="质量(10)" align="center" width="100">
          <template #default="scope">{{ dimScore(scope.row, 's_qual', 10) }}</template>
        </el-table-column>
        <el-table-column label="活跃(5)" align="center" width="90">
          <template #default="scope">{{ dimScore(scope.row, 's_active', 5) }}</template>
        </el-table-column>
        <el-table-column label="加分" align="center" width="90">
          <template #default="scope">
            {{ (Number(scope.row.detail.weightBoost) || 0) + (Number(scope.row.detail.fairnessBoost) || 0) }}
          </template>
        </el-table-column>
        <el-table-column label="在途/上限" align="center" width="100">
          <template #default="scope">
            {{ scope.row.detail.inTransit }} / {{ scope.row.detail.maxConcurrent }}
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!previewLoading && !candidates.length" description="暂无可用候选（可能小区不覆盖或产能已满）" />
      <template #footer>
        <el-button @click="openPreview = false">关 闭</el-button>
      </template>
    </el-dialog>

    <!-- 派单轨迹 -->
    <el-dialog v-model="openTrajectory" :title="trajectoryTitle" width="760px" append-to-body>
      <el-table :data="trajectory" v-loading="trajectoryLoading" size="small">
        <el-table-column label="轮次" align="center" prop="dispatchRound" width="70" />
        <el-table-column label="类型" align="center" width="90">
          <template #default="scope">{{ typeMap[scope.row.dispatchType] || scope.row.dispatchType }}</template>
        </el-table-column>
        <el-table-column label="接单员" align="center">
          <template #default="scope">
            <span>{{ scope.row.runnerNickname || ('用户#' + scope.row.runnerUserId) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="得分" align="center" prop="score" width="80" />
        <el-table-column label="状态" align="center" width="110">
          <template #default="scope">
            <el-tag :type="statusTagType(scope.row.status)" effect="light">
              {{ statusMap[scope.row.status] || scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="时间" align="center" width="170">
          <template #default="scope">{{ parseTime(scope.row.createTime) }}</template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="openTrajectory = false">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Dispatch">
import { listDispatchLog, listDispatchLogByOrder, listDispatchCandidates, triggerDispatch } from '@/api/errand/dispatch'

const { proxy } = getCurrentInstance()

const statusMap = { 1: '已推送', 2: '已接受', 3: '超时未接', 4: '已拒绝', 5: '被指派', 9: '他人已接' }
const typeMap = { 1: '推送', 2: '指派' }

const list = ref([])
const loading = ref(false)
const showSearch = ref(true)
const total = ref(0)
const ids = ref([])
const single = ref(true)

const toolOrderId = ref('')
const candidates = ref([])
const previewLoading = ref(false)
const openPreview = ref(false)

const trajectory = ref([])
const trajectoryLoading = ref(false)
const openTrajectory = ref(false)
const trajectoryTitle = ref('派单轨迹')

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  orderId: undefined,
  runnerUserId: undefined,
  dispatchRound: undefined,
  status: undefined
})

function statusTagType(status) {
  if (status === 2) return 'success'
  if (status === 5) return 'warning'
  if (status === 3 || status === 4 || status === 9) return 'info'
  return 'primary'
}

// 六维：展示 归一化得分 x 权重 后的实际贡献分
function dimScore(row, key, weight) {
  const v = row.detail ? Number(row.detail[key]) : NaN
  if (isNaN(v)) return '-'
  return (v * weight).toFixed(2)
}

function getList() {
  loading.value = true
  listDispatchLog(queryParams.value).then(res => {
    list.value = res.rows || []
    total.value = res.total || 0
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm('queryRef')
  queryParams.value.dispatchRound = undefined
  queryParams.value.pageNum = 1
  getList()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(i => i.orderId)
  single.value = selection.length !== 1
}

function previewCandidates() {
  previewLoading.value = true
  openPreview.value = true
  listDispatchCandidates(toolOrderId.value).then(res => {
    candidates.value = res.data || []
    previewLoading.value = false
  }).catch(() => {
    candidates.value = []
    previewLoading.value = false
  })
}

function handleTrigger() {
  proxy.$modal.confirm(`确认对该订单重新发起第 1 轮派单推送？`).then(() => {
    return triggerDispatch(toolOrderId.value)
  }).then(() => {
    proxy.$modal.msgSuccess('已触发派单')
    getList()
  }).catch(() => {})
}

function handleViewTrajectory(row) {
  const orderId = row && row.orderId ? row.orderId : ids.value[0]
  if (!orderId) return
  trajectoryTitle.value = `派单轨迹 - 订单 ${orderId}`
  trajectoryLoading.value = true
  openTrajectory.value = true
  listDispatchLogByOrder(orderId).then(res => {
    trajectory.value = res.data || []
    trajectoryLoading.value = false
  }).catch(() => {
    trajectory.value = []
    trajectoryLoading.value = false
  })
}

getList()
</script>

<style scoped>
.mb8 { margin-bottom: 8px; }
.tool-card { margin-bottom: 16px; background: #F5F6F8; border: 1px solid #EBEEF5; }
.tool-row { display: flex; align-items: center; gap: 10px; flex-wrap: wrap; }
.tool-label { color: #1A1A1A; font-weight: 500; }
.tool-tip { color: #909399; font-size: 12px; }
.score { color: #1677FF; font-weight: 600; }
</style>
