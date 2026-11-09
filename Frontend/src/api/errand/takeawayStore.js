import request from '@/utils/request'

export function listTakeawayStore(query) {
  return request({
    url: '/errand/takeawayStore/list',
    method: 'get',
    params: query
  })
}

export function getTakeawayStore(id) {
  return request({
    url: '/errand/takeawayStore/' + id,
    method: 'get'
  })
}

export function addTakeawayStore(data) {
  return request({
    url: '/errand/takeawayStore',
    method: 'post',
    data: data
  })
}

export function updateTakeawayStore(data) {
  return request({
    url: '/errand/takeawayStore',
    method: 'put',
    data: data
  })
}

export function delTakeawayStore(ids) {
  const target = Array.isArray(ids) ? ids.join(',') : ids
  return request({
    url: '/errand/takeawayStore/' + target,
    method: 'delete'
  })
}
