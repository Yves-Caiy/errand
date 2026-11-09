import request from '@/utils/request'

export function listProductType(query) {
  const q = { ...query }
  if (q && q.isManageable !== undefined && q.isManageable !== null) {
    if (q.isManageable === 'Y' || q.isManageable === '1') q.isManageable = 1
    else if (q.isManageable === 'N' || q.isManageable === '0') q.isManageable = 0
  }
  return request({
    url: '/errand/productType/list',
    method: 'get',
    params: q
  })
}

export function getProductType(id) {
  return request({
    url: '/errand/productType/' + id,
    method: 'get'
  })
}

export function addProductType(data) {
  const payload = { ...data }
  if (typeof payload.isManageable === 'string') {
    payload.isManageable = payload.isManageable === 'Y' ? 1 : 0
  }
  return request({
    url: '/errand/productType',
    method: 'post',
    data: payload
  })
}

export function updateProductType(data) {
  const payload = { ...data }
  if (typeof payload.isManageable === 'string') {
    payload.isManageable = payload.isManageable === 'Y' ? 1 : 0
  }
  return request({
    url: '/errand/productType',
    method: 'put',
    data: payload
  })
}

export function delProductType(ids) {
  const target = Array.isArray(ids) ? ids.join(',') : ids
  return request({
    url: '/errand/productType/' + target,
    method: 'delete'
  })
}
