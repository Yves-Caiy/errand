<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="类型名称" prop="typeName">
        <el-input v-model="queryParams.typeName" placeholder="请输入类型名称" clearable style="width: 240px;" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="可管理" prop="isManageable">
        <el-select v-model="queryParams.isManageable" placeholder="请选择" clearable style="width: 240px;">
          <el-option v-for="d in sys_yes_no" :key="d.value" :label="d.label" :value="d.value" />
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
      <el-table-column label="类型名称" align="center" prop="typeName" :show-overflow-tooltip="true" />
      <el-table-column label="类型描述" align="center" prop="typeDesc" :show-overflow-tooltip="true" />
      <el-table-column label="图标" align="center" prop="iconUrl" width="120">
        <template #default="scope">
          <el-image v-if="scope.row.iconUrl" :src="toFullUrl(scope.row.iconUrl)" style="width: 24px; height: 24px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column label="可管理" align="center" prop="isManageable">
        <template #default="scope">
          <dict-tag :options="sys_yes_no" :value="scope.row.isManageable === 1 ? 'Y' : 'N'" />
        </template>
      </el-table-column>
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
          <el-button link type="primary" icon="List" @click="openProdManager(scope.row)">服务规格</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog v-model="openView" title="服务类型详情" width="520px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="类型名称">{{ current?.typeName }}</el-descriptions-item>
        <el-descriptions-item label="类型描述">{{ current?.typeDesc }}</el-descriptions-item>
        <el-descriptions-item label="图标">
          <el-image v-if="current?.iconUrl" :src="toFullUrl(current?.iconUrl)" style="width: 48px; height: 48px" fit="cover" />
        </el-descriptions-item>
        <el-descriptions-item label="可管理">
          <dict-tag :options="sys_yes_no" :value="current?.isManageable === 1 ? 'Y' : 'N'" />
        </el-descriptions-item>
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

    <!-- 服务规格管理 -->
    <el-dialog v-model="openProd" title="服务规格管理" width="920px" append-to-body>
      <el-form :model="prodQuery" ref="prodQueryRef" :inline="true" v-show="prodShowSearch" label-width="90px">
        <el-form-item label="规格名称" prop="specName">
          <el-input v-model="prodQuery.specName" placeholder="请输入规格名称" clearable style="width: 240px;" @keyup.enter="prodHandleQuery" />
        </el-form-item>
        <el-form-item label="通用定制" prop="isUniversalCustom">
          <el-select v-model="prodQuery.isUniversalCustom" placeholder="请选择" clearable style="width: 240px;">
            <el-option v-for="d in sys_yes_no" :key="d.value" :label="d.label" :value="d.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-select v-model="prodQuery.isEnabled" placeholder="启用状态" clearable style="width: 240px;">
            <el-option v-for="d in sys_normal_disable" :key="d.value" :label="d.label" :value="Number(d.value) === 0 ? 1 : 0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="prodHandleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="prodResetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="Plus" @click="prodHandleAdd">新增服务规格</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="Delete" :disabled="prodMultiple" @click="prodHandleDelete">删除服务规格</el-button>
        </el-col>
        <right-toolbar v-model:showSearch="prodShowSearch" @queryTable="prodGetList"></right-toolbar>
      </el-row>

      <el-table ref="prodTableRef" v-loading="prodLoading" :data="prodList" @selection-change="prodHandleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="规格名称" align="center" prop="specName" :show-overflow-tooltip="true" />
        <el-table-column label="重量" align="center" prop="weightMax">
          <template #default="scope">
            <span>{{ scope.row.weightMax }}</span>
          </template>
        </el-table-column>
        <el-table-column label="距离" align="center" prop="distance" />
        <el-table-column label="价格" align="center" prop="priceMax">
          <template #default="scope">
            <span>{{ fmtAmount(scope.row.priceMax) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="通用定制" align="center" prop="isUniversalCustom">
          <template #default="scope">
            <dict-tag :options="sys_yes_no" :value="scope.row.isUniversalCustom === 1 ? 'Y' : 'N'" />
          </template>
        </el-table-column>
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
        <el-table-column label="操作" align="center" width="160">
          <template #default="scope">
            <el-button link type="success" icon="Edit" @click="prodHandleEdit(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="prodTotal > 0" :total="prodTotal" v-model:page="prodQuery.pageNum" v-model:limit="prodQuery.pageSize" @pagination="prodGetList" />

      <template #footer>
        <el-button @click="openProd = false">关 闭</el-button>
      </template>
    </el-dialog>

    <!-- 服务规格编辑弹窗 -->
    <el-dialog v-model="openProdEdit" :title="prodEditTitle" width="720px" append-to-body>
      <el-form :model="prodForm" ref="prodFormRef" :rules="prodRules" label-width="110px">
        <el-form-item label="规格名称" prop="specName">
          <el-input v-model="prodForm.specName" placeholder="请输入规格名称" />
        </el-form-item>
        <el-form-item label="重量" prop="weightMax">
          <el-input v-model.number="prodForm.weightMax" type="number" placeholder="请输入重量最大" />
        </el-form-item>
        <el-form-item label="距离" prop="distance">
          <el-input v-model.number="prodForm.distance" type="number" placeholder="请输入距离" />
        </el-form-item>
        <el-form-item label="价格" prop="priceMax">
          <el-input v-model.number="prodForm.priceMax" type="number" placeholder="请输入价格最大" />
        </el-form-item>
        <el-form-item label="通用定制" prop="isUniversalCustom">
          <el-select v-model="prodForm.isUniversalCustom" placeholder="请选择">
            <el-option v-for="d in sys_yes_no" :key="d.value" :label="d.label" :value="d.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-select v-model="prodForm.isEnabled" placeholder="请选择">
            <el-option v-for="d in sys_normal_disable" :key="d.value" :label="d.label" :value="Number(d.value) === 0 ? 1 : 0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="prodSubmitForm">保 存</el-button>
        <el-button @click="prodCancel">取 消</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="openEdit" :title="editTitle" width="520px" append-to-body>
      <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
        <el-form-item label="类型名称" prop="typeName">
          <el-input v-model="form.typeName" placeholder="请输入类型名称" />
        </el-form-item>
        <el-form-item label="类型描述" prop="typeDesc">
          <el-input v-model="form.typeDesc" placeholder="请输入类型描述" />
        </el-form-item>
        <el-form-item label="图标地址" prop="iconUrl">
          <ImageUpload v-model="form.iconUrl" :limit="1" :file-type="['png','jpg','jpeg']" :file-size="2" :drag="false" />
        </el-form-item>
        <el-form-item label="可管理" prop="isManageable">
          <el-select v-model="form.isManageable" placeholder="请选择">
            <el-option v-for="d in sys_yes_no" :key="d.value" :label="d.label" :value="d.value" />
          </el-select>
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

<script setup name="ServiceType">
import { listProductType, getProductType, addProductType, updateProductType, delProductType } from '@/api/errand/productType'
import { listProduct, getProduct, addProduct, updateProduct, delProduct } from '@/api/errand/product'
import ImageUpload from '@/components/ImageUpload'

const { proxy } = getCurrentInstance()
const { sys_normal_disable, sys_yes_no } = proxy.useDict('sys_normal_disable', 'sys_yes_no')

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
const baseUrl = import.meta.env.VITE_APP_BASE_API

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  typeName: undefined,
  isManageable: undefined,
  isEnabled: undefined
})

const form = ref({
  id: undefined,
  typeName: undefined,
  typeDesc: undefined,
  iconUrl: undefined,
  isManageable: 'Y',
  isEnabled: 1
})

const rules = {
  typeName: [{ required: true, message: '请输入类型名称', trigger: 'blur' }],
  isManageable: [{ required: true, message: '请选择是否可管理', trigger: 'change' }],
  isEnabled: [{ required: true, message: '请选择启用状态', trigger: 'change' }]
}

function getList() {
  loading.value = true
  const query = { ...queryParams.value }
  if (query.isManageable !== undefined && query.isManageable !== null) {
    query.isManageable = query.isManageable === 'Y' ? 1 : 0
  }
  listProductType(query).then(res => {
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
  getProductType(row.id).then(res => {
    current.value = res.data
    openView.value = true
  })
}

function handleAdd() {
  editTitle.value = '新增服务类型'
  form.value = { id: undefined, typeName: undefined, typeDesc: undefined, iconUrl: undefined, isManageable: 'Y', isEnabled: 1 }
  openEdit.value = true
}

function handleEdit(row) {
  if (!row) {
    if (!ids.value.length) return
    getProductType(ids.value[0]).then(res => {
      form.value = { ...res.data, isManageable: res.data.isManageable === 1 ? 'Y' : 'N' }
      editTitle.value = '修改服务类型'
      openEdit.value = true
    })
  } else {
    getProductType(row.id).then(res => {
      form.value = { ...res.data, isManageable: res.data.isManageable === 1 ? 'Y' : 'N' }
      editTitle.value = '修改服务类型'
      openEdit.value = true
    })
  }
}

function submitForm() {
  formRef.value.validate(valid => {
    if (valid) {
      const payload = { ...form.value, isManageable: form.value.isManageable === 'Y' ? 1 : 0 }
      if (payload.id) {
        updateProductType(payload).then(() => {
          proxy.$modal.msgSuccess('修改成功')
          openEdit.value = false
          getList()
        })
      } else {
        addProductType(payload).then(res => {
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
    return delProductType(delIds)
  }).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    getList()
  }).catch(() => {})
}

getList()

function toFullUrl(u) {
  if (!u) return ''
  // 已是完整地址（http/https）直接用；相对路径（/profile/upload/...）拼上接口前缀，
  // 由 vite 开发代理 / nginx 转发到后端，否则会被当成前端域名下的路径而 404
  if (/^https?:\/\//i.test(u)) return u
  return import.meta.env.VITE_APP_BASE_API + u
}

// -------- 服务规格管理 --------
const openProd = ref(false)
const prodShowSearch = ref(true)
const prodLoading = ref(false)
const prodList = ref([])
const prodTotal = ref(0)
const prodIds = ref([])
const prodMultiple = ref(true)
const prodQuery = ref({
  pageNum: 1,
  pageSize: 10,
  typeId: undefined,
  specName: undefined,
  isUniversalCustom: undefined,
  isEnabled: undefined
})

const openProdEdit = ref(false)
const prodEditTitle = ref('')
const prodFormRef = ref(null)
const prodForm = ref({
  id: undefined,
  typeId: undefined,
  specName: undefined,
  weightMin: undefined,
  weightMax: undefined,
  distance: undefined,
  amountMin: undefined,
  amountMax: undefined,
  duration: undefined,
  durationUnit: undefined,
  priceMin: undefined,
  priceMax: undefined,
  isUniversalCustom: 'N',
  isEnabled: 1
})

  const prodRules = {
    specName: [{ required: true, message: '请输入规格名称', trigger: 'blur' }]
  }

  function openProdManager(row) {
    prodQuery.value.typeId = row.id
    prodQuery.value.pageNum = 1
    prodGetList()
    openProd.value = true
  }

function prodGetList() {
  prodLoading.value = true
  const query = { ...prodQuery.value }
  if (query.isUniversalCustom !== undefined && query.isUniversalCustom !== null) {
    query.isUniversalCustom = query.isUniversalCustom === 'Y' ? 1 : 0
  }
  listProduct(query).then(res => {
    prodList.value = res.rows
    prodTotal.value = res.total
    prodLoading.value = false
  })
}

function prodHandleQuery() {
  prodQuery.value.pageNum = 1
  prodGetList()
}

function prodResetQuery() {
  proxy.resetForm('prodQueryRef')
  prodQuery.value.pageNum = 1
  prodGetList()
}

function prodHandleSelectionChange(selection) {
  prodIds.value = selection.map(i => i.id)
  prodMultiple.value = !selection.length
}

function prodHandleAdd() {
  prodEditTitle.value = '新增服务规格'
  prodForm.value = { id: undefined, typeId: prodQuery.value.typeId, specName: undefined, weightMin: 0, weightMax: undefined, distance: undefined, amountMin: 0, amountMax: 0, duration: 0, durationUnit: 0, priceMin: 0, priceMax: undefined, isUniversalCustom: 'N', isEnabled: 1 }
  openProdEdit.value = true
}

function prodHandleEdit(row) {
  if (!row) {
    if (!prodIds.value.length) return
    getProduct(prodIds.value[0]).then(res => {
      const d = res.data || {}
      const y = { ...d }
      y.priceMin = d.priceMin != null ? d.priceMin / 100 : d.priceMin
      y.priceMax = d.priceMax != null ? d.priceMax / 100 : d.priceMax
      y.amountMin = d.amountMin != null ? d.amountMin / 100 : d.amountMin
      y.amountMax = d.amountMax != null ? d.amountMax / 100 : d.amountMax
      prodForm.value = { ...y, isUniversalCustom: y.isUniversalCustom === 1 ? 'Y' : 'N' }
      prodEditTitle.value = '修改服务规格'
      openProdEdit.value = true
    })
  } else {
    getProduct(row.id).then(res => {
      const d = res.data || {}
      const y = { ...d }
      y.priceMin = d.priceMin != null ? d.priceMin / 100 : d.priceMin
      y.priceMax = d.priceMax != null ? d.priceMax / 100 : d.priceMax
      y.amountMin = d.amountMin != null ? d.amountMin / 100 : d.amountMin
      y.amountMax = d.amountMax != null ? d.amountMax / 100 : d.amountMax
      prodForm.value = { ...y, isUniversalCustom: y.isUniversalCustom === 1 ? 'Y' : 'N' }
      prodEditTitle.value = '修改服务规格'
      openProdEdit.value = true
    })
  }
}

  function prodSubmitForm() {
    prodFormRef.value.validate(valid => {
      if (valid) {
        const payload = { ...prodForm.value, isUniversalCustom: prodForm.value.isUniversalCustom === 'Y' ? 1 : 0 }
        payload.weightMin = 0
        payload.amountMin = 0
        payload.amountMax = 0
        payload.duration = 0
        payload.durationUnit = 0
        payload.priceMin = 0
        if (payload.id) {
          updateProduct(payload).then(() => {
            proxy.$modal.msgSuccess('修改成功')
            openProdEdit.value = false
            prodGetList()
          })
        } else {
          addProduct(payload).then(res => {
            if (res.code === 500) {
              proxy.$modal.msgError(res.msg || '新增失败')
              return
            }
            proxy.$modal.msgSuccess('新增成功')
            openProdEdit.value = false
            prodGetList()
          })
        }
      }
    })
  }

  function fmtAmount(v) {
    if (v == null) return v
    return (Number(v) || 0) / 100
  }

function prodCancel() {
  openProdEdit.value = false
}

function prodHandleDelete(row) {
  const delIds = row && row.id ? [row.id] : prodIds.value
  if (!delIds.length) return
  proxy.$modal.confirm('是否确认删除选中服务规格?').then(() => {
    return delProduct(delIds)
  }).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    prodGetList()
  }).catch(() => {})
}
</script>

<style scoped>
.mb8 { margin-bottom: 8px; }
.ml8 { margin-left: 8px; }
</style>
