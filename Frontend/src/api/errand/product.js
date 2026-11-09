import request from '@/utils/request'

export function listProduct(query) {
  const q = { ...query }
  if (q && q.isUniversalCustom !== undefined && q.isUniversalCustom !== null) {
    if (q.isUniversalCustom === 'Y' || q.isUniversalCustom === '1') q.isUniversalCustom = 1
    else if (q.isUniversalCustom === 'N' || q.isUniversalCustom === '0') q.isUniversalCustom = 0
  }
  return request({
    url: '/errand/product/list',
    method: 'get',
    params: q
  })
}

export function getProduct(id) {
  return request({
    url: '/errand/product/' + id,
    method: 'get'
  })
}

export function addProduct(data) {
  const payload = { ...data }
  if (typeof payload.isUniversalCustom === 'string') {
    payload.isUniversalCustom = payload.isUniversalCustom === 'Y' ? 1 : 0
  }
  ;['priceMin','priceMax','amountMin','amountMax'].forEach(k => {
    const v = payload[k]
    if (v !== undefined && v !== null && !Number.isNaN(Number(v))) {
      payload[k] = Math.round(Number(v) * 100)
    }
  })
  return request({
    url: '/errand/product',
    method: 'post',
    data: payload
  })
}

export function updateProduct(data) {
  const payload = { ...data }
  if (typeof payload.isUniversalCustom === 'string') {
    payload.isUniversalCustom = payload.isUniversalCustom === 'Y' ? 1 : 0
  }
  ;['priceMin','priceMax','amountMin','amountMax'].forEach(k => {
    const v = payload[k]
    if (v !== undefined && v !== null && !Number.isNaN(Number(v))) {
      payload[k] = Math.round(Number(v) * 100)
    }
  })
  return request({
    url: '/errand/product',
    method: 'put',
    data: payload
  })
}

export function delProduct(ids) {
  const target = Array.isArray(ids) ? ids.join(',') : ids
  return request({
    url: '/errand/product/' + target,
    method: 'delete'
  })
}
