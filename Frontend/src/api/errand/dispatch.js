import request from '@/utils/request'

// 候选与打分预览（不写库）
export function listDispatchCandidates(orderId) {
  return request({
    url: '/errand/dispatch/candidates',
    method: 'get',
    params: { orderId }
  })
}

// 手动触发派单（重新走第 1 轮推送）
export function triggerDispatch(orderId) {
  return request({
    url: '/errand/dispatch/trigger',
    method: 'post',
    params: { orderId }
  })
}

// 派单记录列表
export function listDispatchLog(query) {
  return request({
    url: '/errand/dispatch/log/list',
    method: 'get',
    params: query
  })
}

// 某订单的完整派单轨迹
export function listDispatchLogByOrder(orderId) {
  return request({
    url: '/errand/dispatch/log/' + orderId,
    method: 'get'
  })
}
