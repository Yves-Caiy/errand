import request from '@/utils/request'

export function getMiniappConfig() {
  return request({
    url: '/errand/miniappConfig/current',
    method: 'get'
  })
}

export function updateMiniappConfig(data) {
  return request({
    url: '/errand/miniappConfig/current',
    method: 'put',
    data
  })
}

