<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="小区名称" prop="communityName">
        <el-input v-model="queryParams.communityName" placeholder="请输入小区名称" clearable style="width: 240px;" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="queryParams.address" placeholder="请输入地址" clearable style="width: 240px;" @keyup.enter="handleQuery" />
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
      <el-table-column label="小区名称" align="center" prop="communityName" :show-overflow-tooltip="true" />
      <el-table-column label="地址" align="center" prop="address" :show-overflow-tooltip="true" />
      <el-table-column label="联系人" align="center" prop="contactPerson" :show-overflow-tooltip="true" />
      <el-table-column label="联系电话" align="center" prop="contactPhone" :show-overflow-tooltip="true" />
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
      <el-table-column label="操作" align="center" width="320">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)">查看</el-button>
          <el-button link type="success" icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          <el-button link type="primary" icon="List" @click="openCptManager(scope.row)">服务管理</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog v-model="openView" title="小区详情" width="520px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="小区名称">{{ current?.communityName }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ current?.address }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ current?.contactPerson }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ current?.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <dict-tag :options="sys_normal_disable" :value="(current?.isEnabled || 0) === 1 ? 0 : 1" />
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(current?.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(current?.updateTime) }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="openView = false">关 闭</el-button>
      </template>
  </el-dialog>

    <!-- 小区-服务类型关联管理 -->
    <el-dialog v-model="openCpt" title="类型关联管理" width="720px" append-to-body>
      <el-form :model="cptQuery" ref="cptQueryRef" :inline="true" v-show="cptShowSearch" label-width="90px">
        <el-form-item label="服务类型" prop="productTypeId">
          <el-select v-model="cptQuery.productTypeId" placeholder="请选择类型" clearable filterable style="width: 240px">
            <el-option v-for="t in productTypes" :key="t.id" :label="t.typeName" :value="t.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-select v-model="cptQuery.isEnabled" placeholder="启用状态" clearable style="width: 240px;">
            <el-option v-for="d in sys_normal_disable" :key="d.value" :label="d.label" :value="Number(d.value) === 0 ? 1 : 0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="cptHandleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="cptResetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="Plus" @click="cptHandleAdd">新增关联</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" plain icon="Edit" :disabled="cptSingle" @click="cptHandleEdit">修改关联</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="Delete" :disabled="cptMultiple" @click="cptHandleDelete">删除关联</el-button>
        </el-col>
        <right-toolbar v-model:showSearch="cptShowSearch" @queryTable="cptGetList"></right-toolbar>
      </el-row>

      <el-table ref="cptTableRef" v-loading="cptLoading" :data="cptList" @selection-change="cptHandleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="服务类型" align="center" prop="productTypeId">
          <template #default="scope">
            <span>{{ typeNameOf(scope.row.productTypeId) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="isEnabled">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.isEnabled === 1 ? 0 : 1" />
          </template>
        </el-table-column>
        <el-table-column label="排序" align="center" prop="sortOrder" />
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="cptTotal > 0" :total="cptTotal" v-model:page="cptQuery.pageNum" v-model:limit="cptQuery.pageSize" @pagination="cptGetList" />

      <template #footer>
        <el-button @click="openCpt = false">关 闭</el-button>
      </template>
    </el-dialog>

    <!-- 关联编辑弹窗 -->
    <el-dialog v-model="openCptEdit" :title="cptEditTitle" width="520px" append-to-body>
      <el-form :model="cptForm" ref="cptFormRef" :rules="cptRules" label-width="90px">
        <el-form-item label="服务类型" prop="productTypeId">
          <el-select v-model="cptForm.productTypeId" placeholder="请选择" filterable>
            <el-option v-for="t in productTypes" :key="t.id" :label="t.typeName" :value="t.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-select v-model="cptForm.isEnabled" placeholder="请选择">
            <el-option v-for="d in sys_normal_disable" :key="d.value" :label="d.label" :value="Number(d.value) === 0 ? 1 : 0" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="cptForm.sortOrder" :min="0" :max="999" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="cptSubmitForm">保 存</el-button>
        <el-button @click="cptCancel">取 消</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="openEdit" :title="editTitle" width="520px" append-to-body>
      <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
        <el-form-item label="小区名称" prop="communityName">
          <el-input v-model="form.communityName" placeholder="请输入小区名称" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
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

<script setup name="Community">
import { listCommunity, getCommunity, addCommunity, updateCommunity, delCommunity } from '@/api/errand/community'
import { listCommunityProductType, getCommunityProductType, addCommunityProductType, updateCommunityProductType, delCommunityProductType } from '@/api/errand/communityProductType'
import { listProductType } from '@/api/errand/productType'

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')

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
const cptFormRef = ref(null)

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  communityName: undefined,
  address: undefined,
  isEnabled: undefined
})

const form = ref({
  id: undefined,
  communityName: undefined,
  address: undefined,
  contactPerson: undefined,
  contactPhone: undefined,
  isEnabled: 1
})

const rules = {
  communityName: [{ required: true, message: '请输入小区名称', trigger: 'blur' }],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }]
}

function getList() {
  loading.value = true
  listCommunity(queryParams.value).then(res => {
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
  getCommunity(row.id).then(res => {
    current.value = res.data
    openView.value = true
  })
}

function handleAdd() {
  editTitle.value = '新增小区'
  form.value = { id: undefined, communityName: undefined, address: undefined, contactPerson: undefined, contactPhone: undefined, isEnabled: 1 }
  openEdit.value = true
}

function handleEdit(row) {
  if (!row) {
    if (!ids.value.length) return
    getCommunity(ids.value[0]).then(res => {
      form.value = { ...res.data }
      editTitle.value = '修改小区'
      openEdit.value = true
    })
  } else {
    getCommunity(row.id).then(res => {
      form.value = { ...res.data }
      editTitle.value = '修改小区'
      openEdit.value = true
    })
  }
}

function submitForm() {
  formRef.value.validate(valid => {
    if (valid) {
      if (form.value.id) {
        updateCommunity(form.value).then(() => {
          proxy.$modal.msgSuccess('修改成功')
          openEdit.value = false
          getList()
        })
      } else {
        addCommunity(form.value).then(() => {
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
    return delCommunity(delIds)
  }).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    getList()
  }).catch(() => {})
}

getList()

// -------- 小区-服务类型关联管理 --------
const openCpt = ref(false)
const cptShowSearch = ref(true)
const cptLoading = ref(false)
const cptList = ref([])
const cptTotal = ref(0)
const cptIds = ref([])
const cptSingle = ref(true)
const cptMultiple = ref(true)
const productTypes = ref([])
const cptQuery = ref({
  pageNum: 1,
  pageSize: 10,
  communityId: undefined,
  productTypeId: undefined,
  isEnabled: undefined
})

const openCptEdit = ref(false)
const cptEditTitle = ref('')
const cptForm = ref({
  id: undefined,
  communityId: undefined,
  productTypeId: undefined,
  isEnabled: 1,
  sortOrder: 0
})

const cptRules = {
  productTypeId: [{ required: true, message: '请选择服务类型', trigger: 'change' }],
  isEnabled: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

function typeNameOf(id) {
  const t = productTypes.value.find(x => x.id === id)
  return t ? t.typeName : id
}

function loadProductTypes() {
  return listProductType({ pageNum: 1, pageSize: 1000, isEnabled: 1, isManageable: 1 }).then(res => {
    productTypes.value = res.rows || []
  })
}

function openCptManager(row) {
  cptQuery.value.communityId = row.id
  cptQuery.value.pageNum = 1
  Promise.all([loadProductTypes()]).then(() => {
    cptGetList()
    openCpt.value = true
  })
}

function cptGetList() {
  cptLoading.value = true
  listCommunityProductType(cptQuery.value).then(res => {
    cptList.value = res.rows
    cptTotal.value = res.total
    cptLoading.value = false
  })
}

function cptHandleQuery() {
  cptQuery.value.pageNum = 1
  cptGetList()
}

function cptResetQuery() {
  proxy.resetForm('cptQueryRef')
  cptQuery.value.pageNum = 1
  cptGetList()
}

function cptHandleSelectionChange(selection) {
  cptIds.value = selection.map(i => i.id)
  cptSingle.value = selection.length !== 1
  cptMultiple.value = !selection.length
}

function cptHandleAdd() {
  cptEditTitle.value = '新增关联'
  cptForm.value = { id: undefined, communityId: cptQuery.value.communityId, productTypeId: undefined, isEnabled: 1, sortOrder: 0 }
  openCptEdit.value = true
}

function cptHandleEdit(row) {
  if (!row) {
    if (!cptIds.value.length) return
    getCommunityProductType(cptIds.value[0]).then(res => {
      cptForm.value = { ...res.data }
      cptEditTitle.value = '修改关联'
      openCptEdit.value = true
    })
  } else {
    getCommunityProductType(row.id).then(res => {
      cptForm.value = { ...res.data }
      cptEditTitle.value = '修改关联'
      openCptEdit.value = true
    })
  }
}

function cptSubmitForm() {
  cptFormRef.value.validate(valid => {
    if (valid) {
      if (cptForm.value.id) {
        updateCommunityProductType(cptForm.value).then(() => {
          proxy.$modal.msgSuccess('修改成功')
          openCptEdit.value = false
          cptGetList()
        })
      } else {
        addCommunityProductType(cptForm.value).then(res => {
          if (res.code === 500) {
            proxy.$modal.msgError(res.msg || '新增失败')
            return
          }
          proxy.$modal.msgSuccess('新增成功')
          openCptEdit.value = false
          cptGetList()
        })
      }
    }
  })
}

function cptCancel() {
  openCptEdit.value = false
}

function cptHandleDelete(row) {
  const delIds = row && row.id ? [row.id] : cptIds.value
  if (!delIds.length) return
  proxy.$modal.confirm('是否确认删除选中关联?').then(() => {
    return delCommunityProductType(delIds)
  }).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    cptGetList()
  }).catch(() => {})
}
</script>

<style scoped>
.mb8 { margin-bottom: 8px; }
</style>
