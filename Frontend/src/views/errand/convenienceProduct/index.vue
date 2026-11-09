<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="商品名称" prop="productName">
        <el-input v-model="queryParams.productName" placeholder="请输入商品名称" clearable style="width: 240px;" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="类型" prop="productType">
        <el-input v-model="queryParams.productType" placeholder="请输入类型" clearable style="width: 240px;" @keyup.enter="handleQuery" />
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
      <el-table-column label="商品名称" align="center" prop="productName" :show-overflow-tooltip="true" />
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
      <el-table-column label="操作" align="center" width="220">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)">查看</el-button>
          <el-button link type="success" icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog v-model="openView" title="商品详情" width="520px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="商品名称">{{ current?.productName }}</el-descriptions-item>
        <el-descriptions-item label="价格(元)">{{ (current?.price || 0)/100 }}</el-descriptions-item>
        <el-descriptions-item label="图片">
          <el-image v-if="current?.imageUrl" :src="toFullUrl(current?.imageUrl)" style="width: 48px; height: 48px" fit="cover" />
        </el-descriptions-item>
        <el-descriptions-item label="类型">{{ current?.productType }}</el-descriptions-item>
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
        <el-form-item label="商品名称" prop="productName">
          <el-input v-model="form.productName" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="价格(元)" prop="price">
          <el-input v-model.number="form.price" type="number" placeholder="请输入价格(元)" />
        </el-form-item>
        <el-form-item label="图片" prop="imageUrl">
          <ImageUpload v-model="form.imageUrl" :limit="1" :file-type="['png','jpg','jpeg']" :file-size="2" :drag="false" />
        </el-form-item>
        <el-form-item label="类型" prop="productType">
          <el-input v-model="form.productType" placeholder="请输入类型" />
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

<script setup name="ConvenienceProduct">
import { listConvenienceProduct, getConvenienceProduct, addConvenienceProduct, updateConvenienceProduct, delConvenienceProduct } from '@/api/errand/convenienceProduct'
import ImageUpload from '@/components/ImageUpload'

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
const baseUrl = import.meta.env.VITE_APP_BASE_API

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  productName: undefined,
  productType: undefined,
  isEnabled: undefined
})

const form = ref({
  id: undefined,
  productName: undefined,
  price: undefined,
  imageUrl: undefined,
  productType: undefined,
  isEnabled: 1
})

const rules = {
  productName: [{ required: true, message: '请输入商品名称', trigger: 'blur' }]
}

function getList() {
  loading.value = true
  listConvenienceProduct(queryParams.value).then(res => {
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
  getConvenienceProduct(row.id).then(res => {
    current.value = res.data
    openView.value = true
  })
}

function handleAdd() {
  editTitle.value = '新增商品'
  form.value = { id: undefined, productName: undefined, price: undefined, imageUrl: undefined, productType: undefined, isEnabled: 1 }
  openEdit.value = true
}

function handleEdit(row) {
  if (!row) {
    if (!ids.value.length) return
    getConvenienceProduct(ids.value[0]).then(res => {
      const d = res.data || {}
      const y = { ...d }
      y.price = d.price != null ? d.price / 100 : d.price
      form.value = { ...y }
      editTitle.value = '修改商品'
      openEdit.value = true
    })
  } else {
    getConvenienceProduct(row.id).then(res => {
      const d = res.data || {}
      const y = { ...d }
      y.price = d.price != null ? d.price / 100 : d.price
      form.value = { ...y }
      editTitle.value = '修改商品'
      openEdit.value = true
    })
  }
}

function submitForm() {
  formRef.value.validate(valid => {
    if (valid) {
      const payload = { ...form.value }
      if (payload.id) {
        updateConvenienceProduct(payload).then(() => {
          proxy.$modal.msgSuccess('修改成功')
          openEdit.value = false
          getList()
        })
      } else {
        addConvenienceProduct(payload).then(res => {
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
    return delConvenienceProduct(delIds)
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

getList()
</script>

<style scoped>
.mb8 { margin-bottom: 8px; }
.ml8 { margin-left: 8px; }
</style>
