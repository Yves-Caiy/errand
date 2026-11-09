import request from '@/utils/request'

export function listWithdrawApply(query) {
  return request({
    url: '/errand/withdrawApply/list',
    method: 'get',
    params: query
  })
}

export function approveWithdrawApply(id) {
  return request({
    url: `/errand/withdrawApply/${id}/approve`,
    method: 'put'
  })
}

export function rejectWithdrawApply(id) {
  return request({
    url: `/errand/withdrawApply/${id}/reject`,
    method: 'put'
  })
}

export function payWithdrawApply(id) {
  return request({
    url: `/errand/withdrawApply/${id}/pay`,
    method: 'put'
  })
}
