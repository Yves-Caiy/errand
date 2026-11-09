import request from '@/utils/request'

export function listConvenienceProduct(query) {
  return request({
    url: '/errand/convenienceProduct/list',
    method: 'get',
    params: query
  })
}

export function getConvenienceProduct(id) {
  return request({
    url: '/errand/convenienceProduct/' + id,
    method: 'get'
  })
}

export function addConvenienceProduct(data) {
  const payload = { ...data }
  if (payload.price !== undefined && payload.price !== null && !Number.isNaN(Number(payload.price))) {
    payload.price = Math.round(Number(payload.price) * 100)
  }
  return request({
    url: '/errand/convenienceProduct',
    method: 'post',
    data: payload
  })
}

export function updateConvenienceProduct(data) {
  const payload = { ...data }
  if (payload.price !== undefined && payload.price !== null && !Number.isNaN(Number(payload.price))) {
    payload.price = Math.round(Number(payload.price) * 100)
  }
  return request({
    url: '/errand/convenienceProduct',
    method: 'put',
    data: payload
  })
}

export function delConvenienceProduct(ids) {
  const target = Array.isArray(ids) ? ids.join(',') : ids
  return request({
    url: '/errand/convenienceProduct/' + target,
    method: 'delete'
  })
}
