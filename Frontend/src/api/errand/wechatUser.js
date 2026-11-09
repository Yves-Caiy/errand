import request from '@/utils/request'

export function listWechatUser(query) {
  return request({
    url: '/errand/wechatUser/list',
    method: 'get',
    params: query
  })
}

export function getWechatUser(id) {
  return request({
    url: '/errand/wechatUser/' + id,
    method: 'get'
  })
}

export function changeEnabled(enabled, ids) {
  const path = '/errand/wechatUser/status/' + enabled + '/' + ids.join(',')
  return request({
    url: path,
    method: 'put'
  })
}

export function changeAdminStatus(isAdmin, ids) {
  const path = '/errand/wechatUser/admin/' + isAdmin + '/' + ids.join(',')
  return request({
    url: path,
    method: 'put'
  })
}
