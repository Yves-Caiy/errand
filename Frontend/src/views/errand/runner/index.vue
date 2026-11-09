<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="接单员ID" prop="userId">
        <el-input v-model="queryParams.userId" placeholder="请输入用户ID" clearable style="width: 180px;" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="接单员" prop="isRunner">
        <el-select v-model="queryParams.isRunner" placeholder="是否接单员" clearable style="width: 160px;">
          <el-option label="已启用" :value="1" />
          <el-option label="已停用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="在线状态" prop="onlineStatus">
        <el-select v-model="queryParams.onlineStatus" placeholder="在线状态" clearable style="width: 160px;">
          <el-option label="在线" :value="1" />
          <el-option label="离线" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增接单员</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list">
      <el-table-column label="接单员" align="left" width="180">
        <template #default="scope">
          <div class="runner-cell">
            <el-avatar :size="32" :src="fullUrl(scope.row.avatarUrl)">{{ (scope.row.nickname || '用户')[0] }}</el-avatar>
            <div class="runner-meta">
              <span class="nick">{{ scope.row.nickname || ('用户#' + scope.row.userId) }}</span>
              <span class="uid">ID: {{ scope.row.userId }}</span>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="联系电话" align="center" prop="phone" :show-overflow-tooltip="true" />
      <el-table-column label="接单员" align="center" width="110">
        <template #default="scope">
          <el-tag :type="scope.row.isRunner === 1 ? 'success' : 'info'" effect="light">
            {{ scope.row.isRunner === 1 ? '已启用' : '已停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="在线状态" align="center" width="110">
        <template #default="scope">
          <el-tag :type="scope.row.onlineStatus === 1 ? 'primary' : 'info'" effect="plain">
            {{ scope.row.onlineStatus === 1 ? '在线' : '离线' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="产能上限" align="center" prop="maxConcurrent" width="90" />
      <el-table-column label="权重加成" align="center" prop="weightBoost" width="90" />
      <el-table-column label="服务小区" align="center" :show-overflow-tooltip="true">
        <template #default="scope">
          <span v-if="scope.row.isAllCommunity === 1" class="all-community">全部小区</span>
          <span v-else>{{ scope.row.communityNames || '未配置' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" width="170">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="260">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="primary" icon="OfficeBuilding" @click="openCommunity(scope.row)">服务小区</el-button>
          <el-button
            link
            :type="scope.row.isRunner === 1 ? 'danger' : 'success'"
            :icon="scope.row.isRunner === 1 ? 'CircleClose' : 'CircleCheck'"
            @click="handleToggleStatus(scope.row)"
          >{{ scope.row.isRunner === 1 ? '停用' : '启用' }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 编辑接单员档案 -->
    <el-dialog v-model="openEdit" :title="editTitle" width="560px" append-to-body>
      <el-form :model="form" ref="formRef" :rules="rules" label-width="110px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" :disabled="!!form.id" placeholder="请输入小程序用户ID" />
          <div class="tip">对应小程序 wechat_user 表的用户 ID，保存后该用户即成为接单员</div>
        </el-form-item>
        <el-form-item label="产能上限" prop="maxConcurrent">
          <el-input-number v-model="form.maxConcurrent" :min="1" :max="50" />
          <span class="tip">同时在途（待接单+已接单）订单上限，超出则不再参与派单</span>
        </el-form-item>
        <el-form-item label="权重加成" prop="weightBoost">
          <el-input-number v-model="form.weightBoost" :min="-100" :max="100" />
          <span class="tip">打分时的固定加分，用于人工倾斜（如重点培养的接单员）</span>
        </el-form-item>
        <el-form-item label="服务范围" prop="isAllCommunity">
          <el-radio-group v-model="form.isAllCommunity">
            <el-radio :value="0">仅指定小区</el-radio>
            <el-radio :value="1">全部小区</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="技能标签" prop="skills">
          <el-input v-model="form.skills" placeholder="如：外卖,超市（逗号分隔，可留空）" />
        </el-form-item>
        <el-form-item label="启用状态" prop="isRunner">
          <el-switch v-model="form.isRunner" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitForm">保 存</el-button>
        <el-button @click="openEdit = false">取 消</el-button>
      </template>
    </el-dialog>

    <!-- 服务小区 -->
    <el-dialog v-model="openCommunityDlg" title="设置服务小区" width="560px" append-to-body>
      <el-alert
        type="info"
        :closable="false"
        show-icon
        title="仅当订单的收货小区或取货店铺小区在此范围内，该接单员才会进入派单候选池"
        style="margin-bottom: 12px"
      />
      <el-select v-model="communityIds" multiple filterable placeholder="请选择服务小区" style="width: 100%">
        <el-option v-for="c in communityOptions" :key="c.id" :label="c.communityName" :value="c.id" />
      </el-select>
      <template #footer>
        <el-button type="primary" @click="submitCommunity">保 存</el-button>
        <el-button @click="openCommunityDlg = false">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Runner">
import { listRunner, getRunner, saveRunner, setRunnerCommunity, changeRunnerStatus } from '@/api/errand/runner'
import { listCommunity } from '@/api/errand/community'

const { proxy } = getCurrentInstance()

const list = ref([])
const loading = ref(false)
const showSearch = ref(true)
const total = ref(0)
const openEdit = ref(false)
const editTitle = ref('')
const formRef = ref(null)
const openCommunityDlg = ref(false)
const communityIds = ref([])
const communityOptions = ref([])
const currentRunner = ref(null)

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  userId: undefined,
  isRunner: undefined,
  onlineStatus: undefined
})

const form = ref({
  id: undefined,
  userId: undefined,
  isRunner: 1,
  onlineStatus: 0,
  maxConcurrent: 3,
  weightBoost: 0,
  isAllCommunity: 0,
  skills: ''
})

const rules = {
  userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
  maxConcurrent: [{ required: true, message: '请输入产能上限', trigger: 'blur' }]
}

// 图片地址统一拼接接口前缀，避免相对路径 404
function fullUrl(url) {
  if (!url) return undefined
  if (/^https?:\/\//i.test(url)) return url
  const base = import.meta.env.VITE_APP_BASE_API || ''
  return base + url
}

function getList() {
  loading.value = true
  listRunner(queryParams.value).then(res => {
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
  queryParams.value.pageNum = 1
  getList()
}

function handleAdd() {
  editTitle.value = '新增接单员'
  form.value = { id: undefined, userId: undefined, isRunner: 1, onlineStatus: 0, maxConcurrent: 3, weightBoost: 0, isAllCommunity: 0, skills: '' }
  openEdit.value = true
}

function handleEdit(row) {
  getRunner(row.userId).then(res => {
    const p = (res.data && res.data.profile) || {}
    form.value = {
      id: p.id,
      userId: p.userId,
      isRunner: p.isRunner ?? 1,
      onlineStatus: p.onlineStatus ?? 0,
      maxConcurrent: p.maxConcurrent ?? 3,
      weightBoost: p.weightBoost ?? 0,
      isAllCommunity: p.isAllCommunity ?? 0,
      skills: p.skills || ''
    }
    editTitle.value = '修改接单员'
    openEdit.value = true
  })
}

function submitForm() {
  formRef.value.validate(valid => {
    if (!valid) return
    const payload = {
      userId: Number(form.value.userId),
      isRunner: form.value.isRunner,
      onlineStatus: form.value.onlineStatus,
      maxConcurrent: form.value.maxConcurrent,
      weightBoost: form.value.weightBoost,
      isAllCommunity: form.value.isAllCommunity,
      skills: form.value.skills
    }
    saveRunner(payload).then(() => {
      proxy.$modal.msgSuccess('保存成功')
      openEdit.value = false
      getList()
    })
  })
}

function handleToggleStatus(row) {
  const next = row.isRunner === 1 ? 0 : 1
  const action = next === 1 ? '启用' : '停用'
  proxy.$modal.confirm(`确认${action}接单员「${row.nickname || row.userId}」?`).then(() => {
    return changeRunnerStatus({ userId: row.userId, isRunner: next })
  }).then(() => {
    proxy.$modal.msgSuccess(action + '成功')
    getList()
  }).catch(() => {})
}

function loadCommunityOptions() {
  return listCommunity({ pageNum: 1, pageSize: 1000, isEnabled: 1 }).then(res => {
    communityOptions.value = res.rows || []
  })
}

function openCommunity(row) {
  currentRunner.value = row
  Promise.all([loadCommunityOptions(), getRunner(row.userId)]).then(([, res]) => {
    communityIds.value = (res.data && res.data.communityIds) || []
    openCommunityDlg.value = true
  })
}

function submitCommunity() {
  setRunnerCommunity({
    userId: currentRunner.value.userId,
    communityIds: communityIds.value
  }).then(() => {
    proxy.$modal.msgSuccess('保存成功')
    openCommunityDlg.value = false
    getList()
  })
}

getList()
</script>

<style scoped>
.mb8 { margin-bottom: 8px; }
.runner-cell { display: flex; align-items: center; gap: 8px; }
.runner-meta { display: flex; flex-direction: column; line-height: 1.3; }
.runner-meta .nick { color: #1A1A1A; font-weight: 500; }
.runner-meta .uid { color: #909399; font-size: 12px; }
.all-community { color: #1677FF; }
.tip { margin-left: 10px; color: #909399; font-size: 12px; }
</style>
