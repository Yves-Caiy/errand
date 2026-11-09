import auth from '@/plugins/auth'
import router, { constantRoutes, dynamicRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView'
import InnerLink from '@/layout/components/InnerLink'

// 匹配views里面所有的.vue文件
const modules = import.meta.glob('./../../views/**/*.vue')

const usePermissionStore = defineStore(
  'permission',
  {
    state: () => ({
      routes: [],
      addRoutes: [],
      defaultRoutes: [],
      topbarRouters: [],
      sidebarRouters: []
    }),
    actions: {
      setRoutes(routes) {
        this.addRoutes = routes
        this.routes = constantRoutes.concat(routes)
      },
      setDefaultRoutes(routes) {
        this.defaultRoutes = constantRoutes.concat(routes)
      },
      setTopbarRoutes(routes) {
        const merged = mergeTopbarRoutes(routes)
        this.topbarRouters = sortTopbarRoutes(merged)
      },
      setSidebarRouters(routes) {
        this.sidebarRouters = routes
      },
      generateRoutes(roles) {
        return new Promise(resolve => {
          // 向后端请求路由数据
          getRouters().then(res => {
            const sdata = JSON.parse(JSON.stringify(res.data))
            const rdata = JSON.parse(JSON.stringify(res.data))
            const defaultData = JSON.parse(JSON.stringify(res.data))
            const sidebarRoutes = filterAsyncRouter(sdata)
            const rewriteRoutes = filterAsyncRouter(rdata, false, true)
            const defaultRoutes = filterAsyncRouter(defaultData)
            const asyncRoutes = filterDynamicRoutes(dynamicRoutes)
            asyncRoutes.forEach(route => { router.addRoute(route) })
            this.setRoutes(rewriteRoutes)
            this.setSidebarRouters(constantRoutes.concat(sidebarRoutes))
            this.setDefaultRoutes(sidebarRoutes)
            this.setTopbarRoutes(defaultRoutes)
            resolve(rewriteRoutes)
          })
        })
      }
    }
  })

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach(el => {
    el.path = lastRouter ? lastRouter.path + '/' + el.path : el.path
    if (el.children && el.children.length && el.component === 'ParentView') {
      children = children.concat(filterChildren(el.children, el))
    } else {
      children.push(el)
    }
  })
  return children
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes) {
  const res = []
  routes.forEach(route => {
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        res.push(route)
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        res.push(route)
      }
    }
  })
  return res
}

export const loadView = (view) => {
  let res
  for (const path in modules) {
    const dir = path.split('views/')[1].split('.vue')[0]
    if (dir === view) {
      res = () => modules[path]()
    }
  }
  return res
}

// 将静态路由（constantRoutes）中的指定模块合并到顶部菜单
function mergeTopbarRoutes(dynamicTopbars) {
  const map = new Map()
  ;(dynamicTopbars || []).forEach(r => {
    map.set(r.path, r)
  })
  const statics = buildStaticTopbars()
  statics.forEach(r => {
    if (!map.has(r.path)) {
      map.set(r.path, r)
    }
  })
  return Array.from(map.values())
}

// 构建静态顶部菜单条目（从 constantRoutes 中提取）
function buildStaticTopbars() {
  const wanted = new Set([
    '/miniappConfig',
    '/wechatUser',
    '/community',
    '/serviceType',
    '/servicePackage',
    '/takeawayStore',
    '/convenienceProduct',
    '/servicePackageUsers',
    '/order',
    '/withdrawApply'
  ])
  const result = []
  ;(constantRoutes || []).forEach(route => {
    if (wanted.has(route.path) && Array.isArray(route.children) && route.children.length > 0) {
      const child = route.children[0]
      const meta = child.meta || {}
      result.push({
        path: route.path,
        component: route.component,
        children: route.children,
        meta: { ...meta }
      })
    }
  })
  return result
}

// 顶部菜单排序（按需求顺序）
function sortTopbarRoutes(routes) {
  const desired = [
    '/index',
    '/system',
    '/monitor',
    '/tool',
    '/miniappConfig',
    '/wechatUser',
    '/community',
    '/serviceType',
    '/servicePackage',
    '/takeawayStore',
    '/convenienceProduct',
    '/servicePackageUsers',
    '/order'
  ]
  const order = new Map()
  desired.forEach((p, i) => order.set(p, i))
  return (routes || []).slice().sort((a, b) => {
    const ai = order.has(a.path) ? order.get(a.path) : Number.MAX_SAFE_INTEGER
    const bi = order.has(b.path) ? order.get(b.path) : Number.MAX_SAFE_INTEGER
    if (ai !== bi) return ai - bi
    return 0
  })
}

export default usePermissionStore
