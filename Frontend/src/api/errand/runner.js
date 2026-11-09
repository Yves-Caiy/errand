import request from '@/utils/request'

// 接单员列表
export function listRunner(query) {
  return request({
    url: '/errand/runner/list',
    method: 'get',
    params: query
  })
}

// 接单员详情（含服务小区）
export function getRunner(userId) {
  return request({
    url: '/errand/runner/' + userId,
    method: 'get'
  })
}

// 新增或修改接单员档案
export function saveRunner(data) {
  return request({
    url: '/errand/runner',
    method: 'post',
    data: data
  })
}

// 设置接单员服务小区
export function setRunnerCommunity(data) {
  return request({
    url: '/errand/runner/community',
    method: 'post',
    data: data
  })
}

// 启用/停用接单员
export function changeRunnerStatus(data) {
  return request({
    url: '/errand/runner/status',
    method: 'put',
    data: data
  })
}
