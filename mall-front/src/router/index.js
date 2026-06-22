import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    children: [
      { path: '', name: 'Home', component: () => import('../views/home/Home.vue') },
      { path: 'product/:id', name: 'ProductDetail', component: () => import('../views/product/ProductDetail.vue') },
      { path: 'cart', name: 'Cart', component: () => import('../views/cart/Cart.vue'), meta: { requireAuth: true } },
      { path: 'order', name: 'OrderList', component: () => import('../views/order/OrderList.vue'), meta: { requireAuth: true } },
      { path: 'order/:id', name: 'OrderDetail', component: () => import('../views/order/OrderDetail.vue'), meta: { requireAuth: true } },
      { path: 'user', name: 'UserCenter', component: () => import('../views/user/UserCenter.vue'), meta: { requireAuth: true } },
    ]
  },
  { path: '/login', name: 'Login', component: () => import('../views/user/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('../views/user/Register.vue') },
  {
    path: '/admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    meta: { requireAuth: true, requireAdmin: true },
    children: [
      { path: '', name: 'AdminDashboard', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'products', name: 'AdminProducts', component: () => import('../views/admin/ProductManage.vue') },
      { path: 'orders', name: 'AdminOrders', component: () => import('../views/admin/OrderManage.vue') },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')

  if (to.meta.requireAuth && !token) {
    next('/login')
  } else if (to.meta.requireAdmin && userInfo.role !== 1) {
    next('/')
  } else {
    next()
  }
})

export default router
