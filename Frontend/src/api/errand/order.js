import request from '@/utils/request'

export function listOrder(query) {
  return request({
    url: '/errand/order/list',
    method: 'get',
    params: query
  })
}

export function getOrder(id) {
  return request({
    url: '/errand/order/' + id,
    method: 'get'
  })
}

export function applyRefund(data) {
  return request({
    url: '/errand/order/refund/apply',
    method: 'post',
    data: data
  })
}
