import request from '@/utils/request'

export function listServicePackage(query) {
  return request({
    url: '/errand/servicePackage/list',
    method: 'get',
    params: query
  })
}

export function getServicePackage(id) {
  return request({
    url: '/errand/servicePackage/' + id,
    method: 'get'
  })
}

export function addServicePackage(data) {
  const payload = { ...data }
  ;['price','firstMonthPrice'].forEach(k => {
    const v = payload[k]
    if (v !== undefined && v !== null && !Number.isNaN(Number(v))) {
      payload[k] = Math.round(Number(v) * 100)
    }
  })
  return request({
    url: '/errand/servicePackage',
    method: 'post',
    data: payload
  })
}

export function updateServicePackage(data) {
  const payload = { ...data }
  ;['price','firstMonthPrice'].forEach(k => {
    const v = payload[k]
    if (v !== undefined && v !== null && !Number.isNaN(Number(v))) {
      payload[k] = Math.round(Number(v) * 100)
    }
  })
  return request({
    url: '/errand/servicePackage',
    method: 'put',
    data: payload
  })
}

export function delServicePackage(ids) {
  const target = Array.isArray(ids) ? ids.join(',') : ids
  return request({
    url: '/errand/servicePackage/' + target,
    method: 'delete'
  })
}

export function listValidPackageUsers() {
  return request({
    url: '/errand/servicePackage/validUsers',
    method: 'get'
  })
}
