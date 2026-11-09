<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="小区" prop="communityId">
        <el-select v-model="queryParams.communityId" placeholder="请选择小区" clearable filterable style="width: 240px;">
          <el-option v-for="c in communityOptions" :key="c.id" :label="c.communityName" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="店铺名称" prop="storeName">
        <el-input v-model="queryParams.storeName" placeholder="请输入店铺名称" clearable style="width: 240px;" @keyup.enter="handleQuery" />
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
      <el-table-column label="店铺名称" align="center" prop="storeName" :show-overflow-tooltip="true" />
      <el-table-column label="头像" align="center" prop="avatarUrl" width="120">
        <template #default="scope">
          <el-image v-if="scope.row.avatarUrl" :src="toFullUrl(scope.row.avatarUrl)" style="width: 32px; height: 32px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column label="小区" align="center" prop="communityId">
        <template #default="scope">
          <span>{{ communityName(scope.row.communityId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="地址" align="center" prop="address" :show-overflow-tooltip="true" />
      <el-table-column label="联系方式" align="center" prop="contact" :show-overflow-tooltip="true" />
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
          <el-button link type="primary" icon="List" @click="openProductManager(scope.row)">菜品管理</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog v-model="openView" title="店铺详情" width="520px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="店铺名称">{{ current?.storeName }}</el-descriptions-item>
        <el-descriptions-item label="头像">
          <el-image v-if="current?.avatarUrl" :src="toFullUrl(current?.avatarUrl)" style="width: 48px; height: 48px" fit="cover" />
        </el-descriptions-item>
        <el-descriptions-item label="小区">{{ communityName(current?.communityId) }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ current?.address }}</el-descriptions-item>
        <el-descriptions-item label="联系方式">{{ current?.contact }}</el-descriptions-item>
        <el-descriptions-item label="状态"><dict-tag :options="sys_normal_disable" :value="(current?.isEnabled || 0) === 1 ? 0 : 1" /></el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(current?.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(current?.updateTime) }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="openView = false">关 闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="openEdit" :title="editTitle" width="620px" append-to-body>
      <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
        <el-form-item label="店铺名称" prop="storeName">
          <el-input v-model="form.storeName" placeholder="请输入店铺名称" />
        </el-form-item>
        <el-form-item label="头像" prop="avatarUrl">
          <ImageUpload v-model="form.avatarUrl" :limit="1" :file-type="['png','jpg','jpeg']" :file-size="2" :drag="false" />
        </el-form-item>
        <el-form-item label="小区" prop="communityId">
          <el-select v-model="form.communityId" placeholder="请选择小区" filterable style="width: 240px;">
            <el-option v-for="c in communityOptions" :key="c.id" :label="c.communityName" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="form.contact" placeholder="请输入联系方式" />
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
    <el-dialog v-model="openProd" title="菜品管理" width="920px" append-to-body>
      <el-form :model="prodQuery" ref="prodQueryRef" :inline="true" v-show="prodShowSearch" label-width="90px">
        <el-form-item label="菜品名称" prop="productName">
          <el-input v-model="prodQuery.productName" placeholder="请输入菜品名称" clearable style="width: 240px;" @keyup.enter="prodHandleQuery" />
        </el-form-item>
        <el-form-item label="类型" prop="productType">
          <el-input v-model="prodQuery.productType" placeholder="请输入类型" clearable style="width: 240px;" @keyup.enter="prodHandleQuery" />
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
          <el-button type="primary" plain icon="Plus" @click="prodHandleAdd">新增菜品</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="Delete" :disabled="prodMultiple" @click="prodHandleDelete">删除菜品</el-button>
        </el-col>
        <right-toolbar v-model:showSearch="prodShowSearch" @queryTable="prodGetList"></right-toolbar>
      </el-row>

      <el-table ref="prodTableRef" v-loading="prodLoading" :data="prodList" @selection-change="prodHandleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="菜品名称" align="center" prop="productName" :show-overflow-tooltip="true" />
        <el-table-column label="价格(元)" align="center" prop="price">
          <template #default="scope">
            <span>{{ (scope.row.price || 0) / 100 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="类型" align="center" prop="productType" />
        <el-table-column label="图片" align="center" prop="imageUrl" width="120">
          <template #default="scope">
            <el-image v-if="scope.row.imageUrl" :src="toFullUrl(scope.row.imageUrl)" style="width: 32px; height: 32px" fit="cover" />
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

    <el-dialog v-model="openProdEdit" :title="prodEditTitle" width="720px" append-to-body>
      <el-form :model="prodForm" ref="prodFormRef" :rules="prodRules" label-width="110px">
        <el-form-item label="菜品名称" prop="productName">
          <el-input v-model="prodForm.productName" placeholder="请输入菜品名称" />
        </el-form-item>
        <el-form-item label="价格(元)" prop="price">
          <el-input v-model.number="prodForm.price" type="number" placeholder="请输入价格(元)" />
        </el-form-item>
        <el-form-item label="图片" prop="imageUrl">
          <ImageUpload v-model="prodForm.imageUrl" :limit="1" :file-type="['png','jpg','jpeg']" :file-size="2" :drag="false" />
        </el-form-item>
        <el-form-item label="类型" prop="productType">
          <el-input v-model="prodForm.productType" placeholder="请输入类型" />
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
  </div>
  
</template>

<script setup name="TakeawayStore">
import { listTakeawayStore, getTakeawayStore, addTakeawayStore, updateTakeawayStore, delTakeawayStore } from '@/api/errand/takeawayStore'
import { listCommunity } from '@/api/errand/community'
import ImageUpload from '@/components/ImageUpload'
import { listTakeawayProduct, getTakeawayProduct, addTakeawayProduct, updateTakeawayProduct, delTakeawayProduct } from '@/api/errand/takeawayProduct'

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')

const communityOptions = ref([])
function loadCommunities() {
  listCommunity({ pageNum: 1, pageSize: 1000, isEnabled: 1 }).then(res => {
    communityOptions.value = res.rows || []
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
const baseUrl = import.meta.env.VITE_APP_BASE_API

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  communityId: undefined,
  storeName: undefined,
  isEnabled: undefined
})

const form = ref({
  id: undefined,
  communityId: undefined,
  storeName: undefined,
  avatarUrl: undefined,
  address: undefined,
  contact: undefined,
  isEnabled: 1
})

const rules = {
  storeName: [{ required: true, message: '请输入店铺名称', trigger: 'blur' }],
  communityId: [{ required: true, message: '请选择小区', trigger: 'change' }]
}

function getList() {
  loading.value = true
  listTakeawayStore(queryParams.value).then(res => {
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
  getTakeawayStore(row.id).then(res => {
    current.value = res.data
    openView.value = true
  })
}

function handleAdd() {
  editTitle.value = '新增店铺'
  form.value = { id: undefined, communityId: undefined, storeName: undefined, avatarUrl: undefined, address: undefined, contact: undefined, isEnabled: 1 }
  openEdit.value = true
}

function handleEdit(row) {
  if (!row) {
    if (!ids.value.length) return
    getTakeawayStore(ids.value[0]).then(res => {
      form.value = { ...res.data }
      editTitle.value = '修改店铺'
      openEdit.value = true
    })
  } else {
    getTakeawayStore(row.id).then(res => {
      form.value = { ...res.data }
      editTitle.value = '修改店铺'
      openEdit.value = true
    })
  }
}

function submitForm() {
  formRef.value.validate(valid => {
    if (valid) {
      const payload = { ...form.value }
      if (payload.id) {
        updateTakeawayStore(payload).then(() => {
          proxy.$modal.msgSuccess('修改成功')
          openEdit.value = false
          getList()
        })
      } else {
        addTakeawayStore(payload).then(res => {
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
    return delTakeawayStore(delIds)
  }).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    getList()
  }).catch(() => {})
}

function toFullUrl(u) {
  if (!u) return ''
  // 已是完整地址（http/https）直接用；相对路径（/profile/upload/...）拼上接口前缀，
  // 由 vite 开发代理 / nginx 转发到后端，否则会被当成前端域名下的路径而 404
  if (/^https?:\/\//i.test(u)) return u
  return import.meta.env.VITE_APP_BASE_API + u
}

function communityName(id) {
  const c = communityOptions.value.find(i => i.id === id)
  return c ? c.communityName : id
}

loadCommunities()
getList()

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
  storeId: undefined,
  productName: undefined,
  productType: undefined,
  isEnabled: undefined
})

const openProdEdit = ref(false)
const prodEditTitle = ref('')
const prodFormRef = ref(null)
const prodForm = ref({
  id: undefined,
  storeId: undefined,
  productName: undefined,
  price: undefined,
  imageUrl: undefined,
  productType: undefined,
  isEnabled: 1
})

const prodRules = {
  productName: [{ required: true, message: '请输入菜品名称', trigger: 'blur' }],
  storeId: [{ required: true, message: '请选择店铺', trigger: 'change' }]
}

function openProductManager(row) {
  prodQuery.value.storeId = row.id
  prodQuery.value.pageNum = 1
  prodGetList()
  openProd.value = true
}

function prodGetList() {
  prodLoading.value = true
  listTakeawayProduct(prodQuery.value).then(res => {
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
  prodEditTitle.value = '新增菜品'
  prodForm.value = { id: undefined, storeId: prodQuery.value.storeId, productName: undefined, price: undefined, imageUrl: undefined, productType: undefined, isEnabled: 1 }
  openProdEdit.value = true
}

function prodHandleEdit(row) {
  getTakeawayProduct(row.id).then(res => {
    const d = res.data || {}
    const y = { ...d }
    y.price = d.price != null ? d.price / 100 : d.price
    prodForm.value = { ...y }
    prodEditTitle.value = '修改菜品'
    openProdEdit.value = true
  })
}

function prodSubmitForm() {
  prodFormRef.value.validate(valid => {
    if (valid) {
      const payload = { ...prodForm.value }
      if (payload.id) {
        updateTakeawayProduct(payload).then(() => {
          proxy.$modal.msgSuccess('修改成功')
          openProdEdit.value = false
          prodGetList()
        })
      } else {
        addTakeawayProduct(payload).then(res => {
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

function prodCancel() {
  openProdEdit.value = false
}

function prodHandleDelete(row) {
  const delIds = row && row.id ? [row.id] : prodIds.value
  if (!delIds.length) return
  proxy.$modal.confirm('是否确认删除选中菜品?').then(() => {
    return delTakeawayProduct(delIds)
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
