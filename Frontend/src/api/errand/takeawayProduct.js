import request from '@/utils/request'

export function listTakeawayProduct(query) {
  return request({
    url: '/errand/takeawayProduct/list',
    method: 'get',
    params: query
  })
}

export function getTakeawayProduct(id) {
  return request({
    url: '/errand/takeawayProduct/' + id,
    method: 'get'
  })
}

export function addTakeawayProduct(data) {
  const payload = { ...data }
  if (payload.price !== undefined && payload.price !== null && !Number.isNaN(Number(payload.price))) {
    payload.price = Math.round(Number(payload.price) * 100)
  }
  return request({
    url: '/errand/takeawayProduct',
    method: 'post',
    data: payload
  })
}

export function updateTakeawayProduct(data) {
  const payload = { ...data }
  if (payload.price !== undefined && payload.price !== null && !Number.isNaN(Number(payload.price))) {
    payload.price = Math.round(Number(payload.price) * 100)
  }
  return request({
    url: '/errand/takeawayProduct',
    method: 'put',
    data: payload
  })
}

export function delTakeawayProduct(ids) {
  const target = Array.isArray(ids) ? ids.join(',') : ids
  return request({
    url: '/errand/takeawayProduct/' + target,
    method: 'delete'
  })
}
