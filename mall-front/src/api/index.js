import request from '../utils/request'

// ==================== 用户相关 API ====================
export const userApi = {
  register: (data) => request.post('/user/register', data),
  login: (data) => request.post('/user/login', data),
  getInfo: () => request.get('/user/info'),
  updateInfo: (data) => request.put('/user/info', data),
  logout: () => request.post('/user/logout'),

  // 收货地址
  getAddressList: () => request.get('/user/address/list'),
  addAddress: (data) => request.post('/user/address', data),
  updateAddress: (id, data) => request.put(`/user/address/${id}`, data),
  deleteAddress: (id) => request.delete(`/user/address/${id}`)
}

// ==================== 商品相关 API ====================
export const productApi = {
  getList: (params) => request.get('/product/list', { params }),
  getDetail: (id) => request.get(`/product/${id}`),
  getHot: () => request.get('/product/hot'),
  getNew: () => request.get('/product/new'),

  // 分类
  getCategoryList: () => request.get('/product/category/list'),

  // 轮播图
  getBannerList: () => request.get('/banner/list')
}

// ==================== 购物车相关 API ====================
export const cartApi = {
  getList: () => request.get('/cart/list'),
  add: (productId, quantity = 1) => request.post('/cart/add', null, { params: { productId, quantity } }),
  updateQuantity: (cartId, quantity) => request.put('/cart/update', null, { params: { cartId, quantity } }),
  delete: (id) => request.delete(`/cart/${id}`),
  updateChecked: (id, checked) => request.put(`/cart/check/${id}`, null, { params: { checked } })
}

// ==================== 订单相关 API ====================
export const orderApi = {
  create: (data) => request.post('/order/create', data),
  getList: (params) => request.get('/order/list', { params }),
  getDetail: (id) => request.get(`/order/${id}`),
  cancel: (id) => request.put(`/order/cancel/${id}`),
  pay: (id) => request.post(`/order/pay/${id}`),
  confirm: (id) => request.put(`/order/confirm/${id}`),
  applyReturn: (id) => request.put(`/order/return/${id}`)
}

// ==================== 管理员 API ====================
export const adminApi = {
  // 仪表盘
  getDashboardStats: () => request.get('/admin/dashboard/stats'),

  // 商品管理
  getProductList: (params) => request.get('/admin/product/list', { params }),
  addProduct: (data) => request.post('/admin/product', data),
  updateProduct: (data) => request.put('/admin/product', data),
  deleteProduct: (id) => request.delete(`/admin/product/${id}`),

  // 分类管理
  getCategoryList: () => request.get('/admin/category/list'),
  addCategory: (data) => request.post('/admin/category', data),
  updateCategory: (data) => request.put('/admin/category', data),
  deleteCategory: (id) => request.delete(`/admin/category/${id}`),

  // 轮播图管理
  getBannerList: () => request.get('/admin/banner/list'),
  addBanner: (data) => request.post('/admin/banner', data),
  updateBanner: (data) => request.put('/admin/banner', data),
  deleteBanner: (id) => request.delete(`/admin/banner/${id}`),

  // 订单管理
  getOrderList: (params) => request.get('/admin/order/list', { params }),
  shipOrder: (id) => request.put(`/admin/order/ship/${id}`)
}
