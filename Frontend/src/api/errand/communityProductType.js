import request from '@/utils/request'

export function listCommunityProductType(query) {
  return request({
    url: '/errand/communityProductType/list',
    method: 'get',
    params: query
  })
}

export function getCommunityProductType(id) {
  return request({
    url: '/errand/communityProductType/' + id,
    method: 'get'
  })
}

export function addCommunityProductType(data) {
  return request({
    url: '/errand/communityProductType',
    method: 'post',
    data: data
  })
}

export function updateCommunityProductType(data) {
  return request({
    url: '/errand/communityProductType',
    method: 'put',
    data: data
  })
}

export function delCommunityProductType(ids) {
  const target = Array.isArray(ids) ? ids.join(',') : ids
  return request({
    url: '/errand/communityProductType/' + target,
    method: 'delete'
  })
}
