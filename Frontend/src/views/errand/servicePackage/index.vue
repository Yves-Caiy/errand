<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="服务类型" prop="typeId">
        <el-select v-model="queryParams.typeId" placeholder="请选择类型" clearable filterable style="width: 240px;">
          <el-option v-for="t in typeOptions" :key="t.id" :label="t.typeName" :value="t.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="套餐名称" prop="packageName">
        <el-input v-model="queryParams.packageName" placeholder="请输入套餐名称" clearable style="width: 240px;" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="时长类型" prop="durationType">
        <el-select v-model="queryParams.durationType" placeholder="请选择时长类型" clearable style="width: 240px;">
          <el-option v-for="d in durationTypeOptions" :key="d.value" :label="d.label" :value="d.value" />
        </el-select>
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleEdit">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table ref="tableRef" v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="套餐名称" align="center" prop="packageName" :show-overflow-tooltip="true" />
      <el-table-column label="服务类型" align="center" prop="typeId">
        <template #default="scope">
          <span>{{ typeName(scope.row.typeId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="价格(元)" align="center" prop="price">
        <template #default="scope">
          <span>{{ (scope.row.price || 0) / 100 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="首月价格(元)" align="center" prop="firstMonthPrice">
        <template #default="scope">
          <span>{{ (scope.row.firstMonthPrice || 0) / 100 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="时长类型" align="center" prop="durationType">
        <template #default="scope">
          <span>{{ durationTypeLabel(scope.row.durationType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="时长天数" align="center" prop="durationDays" />
      <el-table-column label="包含服务" align="center" prop="includeService" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="isEnabled">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.isEnabled === 1 ? 0 : 1" />
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
          <el-button link type="success" icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog v-model="openView" title="套餐详情" width="680px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="套餐名称">{{ current?.packageName }}</el-descriptions-item>
        <el-descriptions-item label="服务类型">{{ typeName(current?.typeId) }}</el-descriptions-item>
        <el-descriptions-item label="价格(元)">{{ (current?.price || 0)/100 }}</el-descriptions-item>
        <el-descriptions-item label="首月价格(元)">{{ (current?.firstMonthPrice || 0)/100 }}</el-descriptions-item>
        <el-descriptions-item label="时长类型">{{ durationTypeLabel(current?.durationType) }}</el-descriptions-item>
        <el-descriptions-item label="时长天数">{{ current?.durationDays }}</el-descriptions-item>
        <el-descriptions-item label="包含服务">{{ current?.includeService }}</el-descriptions-item>
        <el-descriptions-item label="状态"><dict-tag :options="sys_normal_disable" :value="(current?.isEnabled || 0) === 1 ? 0 : 1" /></el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(current?.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(current?.updateTime) }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="openView = false">关 闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="openEdit" :title="editTitle" width="720px" append-to-body>
      <el-form :model="form" ref="formRef" :rules="rules" label-width="110px">
        <el-form-item label="套餐名称" prop="packageName">
          <el-input v-model="form.packageName" placeholder="请输入套餐名称" />
        </el-form-item>
        <el-form-item label="服务类型" prop="typeId">
          <el-select v-model="form.typeId" placeholder="请选择服务类型" filterable style="width: 240px;">
            <el-option v-for="t in typeOptions" :key="t.id" :label="t.typeName" :value="t.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格(元)" prop="price">
          <el-input v-model.number="form.price" type="number" placeholder="请输入价格(元)" />
        </el-form-item>
        <el-form-item label="首月价格(元)" prop="firstMonthPrice">
          <el-input v-model.number="form.firstMonthPrice" type="number" placeholder="请输入首月价格(元)" />
        </el-form-item>
        <el-form-item label="时长类型" prop="durationType">
          <el-select v-model="form.durationType" placeholder="请选择时长类型">
            <el-option v-for="d in durationTypeOptions" :key="d.value" :label="d.label" :value="d.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="时长天数" prop="durationDays">
          <el-input-number v-model="form.durationDays" :min="0" />
        </el-form-item>
        <el-form-item label="包含服务" prop="includeService">
          <el-input v-model="form.includeService" type="textarea" placeholder="请输入包含服务描述" />
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-select v-model="form.isEnabled" placeholder="请选择">
            <el-option v-for="d in sys_normal_disable" :key="d.value" :label="d.label" :value="Number(d.value) === 0 ? 1 : 0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitForm">保 存</el-button>
        <el-button @click="cancel">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ServicePackage">
import { listServicePackage, getServicePackage, addServicePackage, updateServicePackage, delServicePackage } from '@/api/errand/servicePackage'
import { listProductType } from '@/api/errand/productType'

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')

const durationTypeOptions = [
  { value: 1, label: '包月' },
  { value: 2, label: '包年' }
]

const typeOptions = ref([])
function loadTypes() {
  listProductType({ pageNum: 1, pageSize: 1000, isEnabled: 1 }).then(res => {
    typeOptions.value = res.rows || []
  })
}

const list = ref([])
const loading = ref(false)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const openView = ref(false)
const openEdit = ref(false)
const editTitle = ref('')
const current = ref(null)
const formRef = ref(null)

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  typeId: undefined,
  packageName: undefined,
  durationType: undefined,
  isEnabled: undefined
})

const form = ref({
  id: undefined,
  typeId: undefined,
  packageName: undefined,
  price: undefined,
  firstMonthPrice: undefined,
  durationType: undefined,
  durationDays: undefined,
  includeService: undefined,
  isEnabled: 1
})

const rules = {
  packageName: [{ required: true, message: '请输入套餐名称', trigger: 'blur' }],
  typeId: [{ required: true, message: '请选择服务类型', trigger: 'change' }]
}

function getList() {
  loading.value = true
  listServicePackage(queryParams.value).then(res => {
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
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

function handleView(row) {
  getServicePackage(row.id).then(res => {
    current.value = res.data
    openView.value = true
  })
}

function handleAdd() {
  editTitle.value = '新增套餐'
  form.value = { id: undefined, typeId: undefined, packageName: undefined, price: undefined, firstMonthPrice: undefined, durationType: undefined, durationDays: undefined, includeService: undefined, isEnabled: 1 }
  openEdit.value = true
}

function handleEdit(row) {
  if (!row) {
    if (!ids.value.length) return
    getServicePackage(ids.value[0]).then(res => {
      const d = res.data || {}
      const y = { ...d }
      y.price = d.price != null ? d.price / 100 : d.price
      y.firstMonthPrice = d.firstMonthPrice != null ? d.firstMonthPrice / 100 : d.firstMonthPrice
      form.value = { ...y }
      editTitle.value = '修改套餐'
      openEdit.value = true
    })
  } else {
    getServicePackage(row.id).then(res => {
      const d = res.data || {}
      const y = { ...d }
      y.price = d.price != null ? d.price / 100 : d.price
      y.firstMonthPrice = d.firstMonthPrice != null ? d.firstMonthPrice / 100 : d.firstMonthPrice
      form.value = { ...y }
      editTitle.value = '修改套餐'
      openEdit.value = true
    })
  }
}

function submitForm() {
  formRef.value.validate(valid => {
    if (valid) {
      const payload = { ...form.value }
      if (payload.id) {
        updateServicePackage(payload).then(() => {
          proxy.$modal.msgSuccess('修改成功')
          openEdit.value = false
          getList()
        })
      } else {
        addServicePackage(payload).then(res => {
          if (res.code === 500) {
            proxy.$modal.msgError(res.msg || '新增失败')
            return
          }
          proxy.$modal.msgSuccess('新增成功')
          openEdit.value = false
          getList()
        })
      }
    }
  })
}

function cancel() {
  openEdit.value = false
}

function handleDelete(row) {
  const delIds = row && row.id ? [row.id] : ids.value
  if (!delIds.length) return
  proxy.$modal.confirm('是否确认删除选中数据项?').then(() => {
    return delServicePackage(delIds)
  }).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    getList()
  }).catch(() => {})
}

function typeName(id) {
  const t = typeOptions.value.find(i => i.id === id)
  return t ? t.typeName : id
}

function durationTypeLabel(v) {
  const val = Number(v)
  const o = durationTypeOptions.find(i => i.value === val)
  return o ? o.label : v
}

loadTypes()
getList()
</script>

<style scoped>
.mb8 { margin-bottom: 8px; }
.ml8 { margin-left: 8px; }
</style>
