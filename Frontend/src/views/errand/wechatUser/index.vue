<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="小区" prop="communityId">
        <el-select v-model="queryParams.communityId" placeholder="请选择小区" clearable filterable style="width: 240px;">
          <el-option v-for="c in communityOptions" :key="c.id" :label="c.communityName" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="queryParams.nickname" placeholder="请输入昵称" clearable style="width: 240px;" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable style="width: 240px;" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="isEnabled">
        <el-select v-model="queryParams.isEnabled" placeholder="启用状态" clearable style="width: 240px;">
          <el-option v-for="d in sys_normal_disable" :key="d.value" :label="d.label" :value="Number(d.value) === 0 ? 1 : 0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" plain icon="CircleCheck" :disabled="multiple" @click="batchEnable(1)">批量启用</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="CircleClose" :disabled="multiple" @click="batchEnable(0)">批量禁用</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table ref="tableRef" v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="头像" align="center" width="80">
        <template #default="scope">
          <el-avatar :src="scope.row.avatarUrl" size="small" />
        </template>
      </el-table-column>
      <el-table-column label="OpenID" align="center" prop="openid" :show-overflow-tooltip="true" />
      <el-table-column label="昵称" align="center" prop="nickname" :show-overflow-tooltip="true" />
      <el-table-column label="所属小区" align="center" prop="communityName" :show-overflow-tooltip="true" />
      <el-table-column label="手机号" align="center" prop="phone" :show-overflow-tooltip="true" />
      <el-table-column label="地址" align="center" prop="detailAddress" :show-overflow-tooltip="true" />
      <el-table-column label="累计佣金" align="center" prop="totalCommission" />
      <el-table-column label="待结算佣金" align="center" prop="pendingCommission">
        <template #default="scope">
          <span>{{ fmtAmount(scope.row.pendingCommission) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="已结算佣金" align="center" prop="settledCommission">
        <template #default="scope">
          <span>{{ fmtAmount(scope.row.settledCommission) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="isEnabled">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.isEnabled === 1 ? 0 : 1" />
        </template>
      </el-table-column>
      <el-table-column label="是否管理员" align="center" prop="isAdmin">
        <template #default="scope">
          <el-tag type="success" v-if="scope.row.isAdmin === 1">是</el-tag>
          <el-tag type="info" v-else>否</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="220">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)">查看</el-button>
          <el-button link type="success" icon="CircleCheck" @click="toggleEnabled(scope.row, 1)" v-if="scope.row.isEnabled !== 1">启用</el-button>
          <el-button link type="warning" icon="CircleClose" @click="toggleEnabled(scope.row, 0)" v-else>禁用</el-button>
          <el-button link type="primary" icon="User" @click="toggleAdmin(scope.row, 1)" v-if="scope.row.isAdmin !== 1">设为管理员</el-button>
          <el-button link type="danger" icon="User" @click="toggleAdmin(scope.row, 0)" v-else>取消管理员</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog v-model="open" title="用户详情" width="520px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="OpenID">{{ current?.openid }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ current?.nickname }}</el-descriptions-item>
        <el-descriptions-item label="所属小区">{{ current?.communityName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ current?.phone }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ current?.detailAddress }}</el-descriptions-item>
        <el-descriptions-item label="累计佣金">{{ current?.totalCommission }}</el-descriptions-item>
        <el-descriptions-item label="待结算佣金">{{ fmtAmount(current?.pendingCommission) }}</el-descriptions-item>
        <el-descriptions-item label="已结算佣金">{{ fmtAmount(current?.settledCommission) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <dict-tag :options="sys_normal_disable" :value="(current?.isEnabled || 0) === 1 ? 0 : 1" />
        </el-descriptions-item>
        <el-descriptions-item label="是否管理员">
          <el-tag type="success" v-if="current?.isAdmin === 1">是</el-tag>
          <el-tag type="info" v-else>否</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(current?.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(current?.updateTime) }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="open = false">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
  
</template>

<script setup name="WechatUser">
import { listWechatUser, getWechatUser, changeEnabled, changeAdminStatus } from '@/api/errand/wechatUser'
import { listCommunity } from '@/api/errand/community'

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')

const list = ref([])
const loading = ref(false)
const showSearch = ref(true)
const ids = ref([])
const multiple = ref(true)
const total = ref(0)
const open = ref(false)
const current = ref(null)
const communityOptions = ref([])

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  communityId: undefined,
  nickname: undefined,
  phone: undefined,
  isEnabled: undefined
})

function getList() {
  loading.value = true
  listWechatUser(queryParams.value).then(res => {
    list.value = res.rows
    total.value = res.total
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
  multiple.value = !selection.length
}

function handleView(row) {
  getWechatUser(row.id).then(res => {
    current.value = res.data
    open.value = true
  })
}

function toggleEnabled(row, status) {
  const text = status === 1 ? '启用' : '禁用'
  proxy.$modal.confirm('确认要"' + text + '""' + row.nickname + '"用户吗?').then(function() {
    return changeEnabled(status, [row.id])
  }).then(() => {
    proxy.$modal.msgSuccess(text + '成功')
    getList()
  }).catch(() => {})
}

function toggleAdmin(row, isAdmin) {
  const text = isAdmin === 1 ? '设为管理员' : '取消管理员'
  proxy.$modal.confirm('确认要将"' + row.nickname + '"' + text + '吗?').then(function() {
    return changeAdminStatus(isAdmin, [row.id])
  }).then(() => {
    proxy.$modal.msgSuccess(text + '成功')
    getList()
  }).catch(() => {})
}

function batchEnable(enabled) {
  const sel = ids.value
  if (!sel.length) return
  proxy.$modal.confirm('确认要批量' + (enabled === 1 ? '启用' : '禁用') + '选中用户吗？').then(() => {
    return changeEnabled(enabled, sel)
  }).then(() => {
    proxy.$modal.msgSuccess('操作成功')
    getList()
  }).catch(() => {})
}

function loadCommunities() {
  listCommunity({ pageNum: 1, pageSize: 1000, isEnabled: 1 }).then(res => {
    communityOptions.value = res.rows || []
  })
}

function fmtAmount(v) {
  if (v == null) return v
  return (Number(v) || 0) / 100
}

loadCommunities()
getList()
</script>

<style scoped>
.mb8 { margin-bottom: 8px; }
</style>
