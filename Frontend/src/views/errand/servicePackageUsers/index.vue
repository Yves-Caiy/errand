<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="Refresh" @click="getList">刷新</el-button>
      </el-col>
    </el-row>
    <el-empty v-if="loading === false && list.length === 0" description="暂无数据" />
    <el-card v-for="(item, idx) in list" :key="idx" class="user-card">
      <div class="user-header">
        <el-avatar :size="48" :src="item.user?.avatarUrl"></el-avatar>
        <div class="user-meta">
          <div class="nickname">{{ item.user?.nickname || ('用户' + (item.user?.id || '')) }}</div>
          <div class="phone">{{ item.user?.phone || '-' }}</div>
        </div>
      </div>
      <el-table :data="item.packages || []" size="small" class="pkg-table">
        <el-table-column label="套餐名称" prop="packageName" align="center" />
        <el-table-column label="购买时间" align="center">
          <template #default="scope">
            <span>{{ parseTime(scope.row.buyTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="过期时间" align="center">
          <template #default="scope">
            <span>{{ parseTime(scope.row.expireTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="有效期(天)" prop="durationDays" align="center" />
        <el-table-column label="是否在有效期内" align="center">
          <template #default="scope">
            <el-tag :type="isValid(scope.row) ? 'success' : 'info'">{{ isValid(scope.row) ? '生效中' : '已过期' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup name="ServicePackageUsers">
import { listValidPackageUsers } from '@/api/errand/servicePackage'

const { proxy } = getCurrentInstance()
const list = ref([])
const loading = ref(false)

function getList() {
  loading.value = true
  listValidPackageUsers().then(res => {
    list.value = Array.isArray(res.data) ? res.data : []
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

function isValid(row) {
  const now = Date.now()
  const expire = new Date(row.expireTime).getTime()
  return expire > now
}

getList()
</script>

<style scoped>
.mb8 { margin-bottom: 8px; }
.user-card { margin-bottom: 12px; }
.user-header { display: flex; align-items: center; margin-bottom: 12px; }
.user-meta { margin-left: 12px; }
.nickname { font-size: 16px; font-weight: 600; }
.phone { color: #909399; font-size: 13px; }
.pkg-table { margin-top: 8px; }
</style>
