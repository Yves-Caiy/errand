<template>
  <div class="app-container">
    <el-card>
      <el-form :model="form" ref="formRef" :rules="rules" label-width="120px">
        <el-form-item label="提现比率(%)" prop="withdrawRate">
          <el-input-number v-model="form.withdrawRate" :min="0" :max="100" />
        </el-form-item>

        <el-form-item label="启用状态" prop="isEnabled">
          <el-select v-model="form.isEnabled" placeholder="请选择">
            <el-option v-for="d in sys_normal_disable" :key="d.value" :label="d.label" :value="Number(d.value) === 0 ? 1 : 0" />
          </el-select>
        </el-form-item>

        <el-form-item label="测试模式" prop="testMode">
          <el-switch v-model="form.testMode" :active-value="1" :inactive-value="0" />
          <span style="margin-left: 12px; color: #909399; font-size: 12px">
            开启后接单不作限制，可以接自己的订单；关闭后恢复正常的接单校验（仅用于测试）
          </span>
        </el-form-item>

        <el-divider content-position="left">
          <span class="section-title">智能派单（仅外卖）</span>
        </el-divider>

        <el-form-item label="派单开关" prop="dispatchEnabled">
          <el-switch v-model="form.dispatchEnabled" :active-value="1" :inactive-value="0" />
          <span style="margin-left: 12px; color: #909399; font-size: 12px">
            开启后外卖订单按下述参数自动派单；测试模式开启时派单不生效
          </span>
        </el-form-item>

        <el-form-item label="启用订单类型">
          <el-checkbox-group v-model="dispatchOrderTypes">
            <el-checkbox :value="1">外卖</el-checkbox>
            <el-checkbox :value="2" disabled>便民超市（暂不支持）</el-checkbox>
            <el-checkbox :value="3" disabled>服务套餐（暂不支持）</el-checkbox>
            <el-checkbox :value="4" disabled>其他服务（暂不支持）</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="推送人数" prop="dispatchPushCount">
          <el-input-number v-model="form.dispatchPushCount" :min="1" :max="20" />
          <span style="margin-left: 12px; color: #909399; font-size: 12px">每轮推送给分数最高的 N 名接单员</span>
        </el-form-item>

        <el-form-item label="超时秒数" prop="dispatchTimeoutSec">
          <el-input-number v-model="form.dispatchTimeoutSec" :min="10" :max="600" />
          <span style="margin-left: 12px; color: #909399; font-size: 12px">推送后超过该时间无人接单，则进入下一轮</span>
        </el-form-item>

        <el-form-item label="最大轮次" prop="dispatchMaxRound">
          <el-input-number v-model="form.dispatchMaxRound" :min="1" :max="10" />
          <span style="margin-left: 12px; color: #909399; font-size: 12px">轮次用尽后自动指派给当前分数最高的接单员</span>
        </el-form-item>

        <el-form-item label="权重配置" prop="dispatchWeightJson">
          <el-input
            v-model="form.dispatchWeightJson"
            type="textarea"
            :rows="3"
            placeholder='留空使用默认权重：{"comm":25,"store":15,"load":25,"deliver":20,"qual":10,"active":5}'
          />
        </el-form-item>

        <el-form-item label="首页轮播图">
          <div style="width: 100%">
            <el-button type="primary" plain icon="Plus" @click="addCarouselItem">添加轮播图</el-button>
            <el-button plain style="margin-left: 8px" @click="showLink = !showLink">{{ showLink ? '隐藏跳转链接' : '显示跳转链接' }}</el-button>
            <el-table :data="carouselItems" style="width: 100%; margin-top: 12px" size="small">
              <el-table-column label="图片" width="220" align="center">
                <template #default="scope">
                  <ImageUpload v-model="scope.row.imageUrl" :limit="1" :file-type="['png','jpg','jpeg']" :file-size="5" :drag="false" />
                </template>
              </el-table-column>
              <el-table-column v-if="showLink" label="跳转链接" align="center" width="280">
                <template #default="scope">
                  <el-input v-model="scope.row.linkUrl" placeholder="请输入跳转链接(可选)" style="width: 220px" />
                </template>
              </el-table-column>
              <el-table-column label="排序" width="200" align="center">
                <template #default="scope">
                  <el-input-number v-model="scope.row.sortOrder" :min="0" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120" align="center">
                <template #default="scope">
                  <el-button link type="danger" icon="Delete" @click="removeCarouselItem(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-form-item>

        <el-form-item label="公告通知" prop="noticeContent">
          <el-input v-model="form.noticeContent" type="textarea" :rows="4" placeholder="请输入公告通知内容" />
        </el-form-item>

        <el-form-item label="隐私政策" prop="privacyPolicy">
          <editor v-model="form.privacyPolicy" :min-height="240" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="Check" @click="submitForm">保 存</el-button>
          <el-button icon="Refresh" @click="loadConfig">重 载</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
  </template>

<script setup name="MiniappConfig">
import { getMiniappConfig, updateMiniappConfig } from '@/api/errand/miniappConfig'
import ImageUpload from '@/components/ImageUpload'

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')

const formRef = ref(null)
const form = ref({
  withdrawRate: 10,
  isEnabled: 1,
  testMode: 0,
  privacyPolicy: '',
  noticeContent: '',
  dispatchEnabled: 1,
  dispatchPushCount: 3,
  dispatchTimeoutSec: 30,
  dispatchMaxRound: 3,
  dispatchWeightJson: ''
})

// 派单启用订单类型（仅外卖）
const dispatchOrderTypes = ref([1])

const carouselItems = ref([])
const showLink = ref(false)

const rules = {
  withdrawRate: [{ required: true, message: '请输入提现比率', trigger: 'blur' }]
}

function addCarouselItem() {
  const next = (carouselItems.value.length || 0) + 1
  carouselItems.value.push({ imageUrl: undefined, linkUrl: undefined, sortOrder: next })
}

function removeCarouselItem(idx) {
  carouselItems.value.splice(idx, 1)
}

function loadConfig() {
  getMiniappConfig().then(res => {
    const d = res.data || {}
    form.value.withdrawRate = d.withdrawRate ?? 10
    form.value.isEnabled = d.isEnabled ?? 1
    form.value.testMode = d.testMode ?? 0
    form.value.dispatchEnabled = d.dispatchEnabled ?? 1
    form.value.dispatchPushCount = d.dispatchPushCount ?? 3
    form.value.dispatchTimeoutSec = d.dispatchTimeoutSec ?? 30
    form.value.dispatchMaxRound = d.dispatchMaxRound ?? 3
    form.value.dispatchWeightJson = d.dispatchWeightJson ?? ''
    dispatchOrderTypes.value = (d.dispatchOrderTypes || '1')
      .split(',')
      .map(x => Number(String(x).trim()))
      .filter(x => !isNaN(x))
    form.value.privacyPolicy = d.privacyPolicy ?? ''
    form.value.noticeContent = d.noticeContent ?? ''
    try {
      const arr = d.carouselImages ? JSON.parse(d.carouselImages) : []
      carouselItems.value = Array.isArray(arr) ? arr.map(x => ({ imageUrl: x.imageUrl, linkUrl: x.linkUrl, sortOrder: x.sortOrder })) : []
    } catch (e) {
      carouselItems.value = []
    }
    if (!carouselItems.value.length) addCarouselItem()
  })
}

function submitForm() {
  formRef.value.validate(valid => {
    if (!valid) return
    const imgs = carouselItems.value
      .filter(i => i.imageUrl)
      .map(i => ({ imageUrl: i.imageUrl, linkUrl: i.linkUrl, sortOrder: Number(i.sortOrder) || 0 }))
    const payload = {
      withdrawRate: Number(form.value.withdrawRate) || 0,
      isEnabled: form.value.isEnabled,
      testMode: form.value.testMode,
      dispatchEnabled: form.value.dispatchEnabled,
      dispatchOrderTypes: dispatchOrderTypes.value.length ? dispatchOrderTypes.value.join(',') : '1',
      dispatchPushCount: Number(form.value.dispatchPushCount) || 3,
      dispatchTimeoutSec: Number(form.value.dispatchTimeoutSec) || 30,
      dispatchMaxRound: Number(form.value.dispatchMaxRound) || 3,
      dispatchWeightJson: form.value.dispatchWeightJson || null,
      privacyPolicy: form.value.privacyPolicy,
      noticeContent: form.value.noticeContent,
      carouselImages: JSON.stringify(imgs)
    }
    updateMiniappConfig(payload).then(() => {
      proxy.$modal.msgSuccess('保存成功')
      loadConfig()
    })
  })
}

loadConfig()
</script>

<style scoped>
.app-container { padding-bottom: 18px; }
.section-title { color: #1A1A1A; font-weight: 600; }
</style>
