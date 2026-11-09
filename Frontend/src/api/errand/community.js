import request from '@/utils/request'

export function listCommunity(query) {
  return request({
    url: '/errand/community/list',
    method: 'get',
    params: query
  })
}

export function getCommunity(id) {
  return request({
    url: '/errand/community/' + id,
    method: 'get'
  })
}

export function addCommunity(data) {
  return request({
    url: '/errand/community',
    method: 'post',
    data: data
  })
}

export function updateCommunity(data) {
  return request({
    url: '/errand/community',
    method: 'put',
    data: data
  })
}

export function delCommunity(ids) {
  const target = Array.isArray(ids) ? ids.join(',') : ids
  return request({
    url: '/errand/community/' + target,
    method: 'delete'
  })
}
