/**
 * 路由配置文件
 * 定义应用的所有页面路由和导航守卫
 */
import { createRouter, createWebHistory } from 'vue-router'

// ==================== 路由表 ====================
const routes = [
  // 前台主布局（包含顶部导航和底部）
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    children: [
      { path: '', name: 'Home', component: () => import('../views/home/Home.vue') },                          // 首页
      { path: 'product/:id', name: 'ProductDetail', component: () => import('../views/product/ProductDetail.vue') },  // 商品详情
      { path: 'cart', name: 'Cart', component: () => import('../views/cart/Cart.vue'), meta: { requireAuth: true } },  // 购物车（需登录）
      { path: 'order', name: 'OrderList', component: () => import('../views/order/OrderList.vue'), meta: { requireAuth: true } },  // 订单列表
      { path: 'order/:id', name: 'OrderDetail', component: () => import('../views/order/OrderDetail.vue'), meta: { requireAuth: true } },  // 订单详情
      { path: 'user', name: 'UserCenter', component: () => import('../views/user/UserCenter.vue'), meta: { requireAuth: true } },  // 个人中心
    ]
  },

  // 登录/注册页面（独立布局）
  { path: '/login', name: 'Login', component: () => import('../views/user/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('../views/user/Register.vue') },

  // 后台管理（需登录 + 管理员权限）
  {
    path: '/admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    meta: { requireAuth: true, requireAdmin: true },
    children: [
      { path: '', name: 'AdminDashboard', component: () => import('../views/admin/Dashboard.vue') },            // 仪表盘
      { path: 'products', name: 'AdminProducts', component: () => import('../views/admin/ProductManage.vue') },  // 商品管理
      { path: 'categories', name: 'AdminCategories', component: () => import('../views/admin/CategoryManage.vue') },  // 分类管理
      { path: 'banners', name: 'AdminBanners', component: () => import('../views/admin/BannerManage.vue') },     // 公告管理
      { path: 'orders', name: 'AdminOrders', component: () => import('../views/admin/OrderManage.vue') },        // 订单管理
    ]
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),  // 使用 HTML5 History 模式
  routes
})

/**
 * 全局前置路由守卫
 * 处理页面访问权限控制
 */
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')

  // 需要登录但未登录 -> 跳转登录页
  if (to.meta.requireAuth && !token) {
    next('/login')
  }
  // 需要管理员权限但不是管理员 -> 跳转首页
  else if (to.meta.requireAdmin && userInfo.role !== 1) {
    next('/')
  }
  // 正常放行
  else {
    next()
  }
})

export default router
