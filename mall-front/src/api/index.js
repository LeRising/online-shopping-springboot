/**
 * API 集中定义文件
 * 统一管理所有后端接口调用，按模块分组
 */
import request from '../utils/request'

// ==================== 用户相关 API ====================
export const userApi = {
  register: (data) => request.post('/user/register', data),      // 用户注册
  login: (data) => request.post('/user/login', data),            // 用户登录
  getInfo: () => request.get('/user/info'),                      // 获取当前用户信息
  updateInfo: (data) => request.put('/user/info', data),         // 更新用户信息
  logout: () => request.post('/user/logout'),                    // 退出登录

  // 收货地址管理
  getAddressList: () => request.get('/user/address/list'),       // 获取地址列表
  addAddress: (data) => request.post('/user/address', data),     // 新增地址
  updateAddress: (id, data) => request.put(`/user/address/${id}`, data),  // 更新地址
  deleteAddress: (id) => request.delete(`/user/address/${id}`)   // 删除地址
}

// ==================== 商品相关 API ====================
export const productApi = {
  getList: (params) => request.get('/product/list', { params }), // 获取商品列表（分页+筛选）
  getDetail: (id) => request.get(`/product/${id}`),             // 获取商品详情
  getHot: () => request.get('/product/hot'),                     // 获取热门商品
  getNew: () => request.get('/product/new'),                     // 获取新品推荐

  // 分类
  getCategoryList: () => request.get('/product/category/list'),  // 获取所有分类

  // 公告轮播
  getBannerList: () => request.get('/banner/list')               // 获取公告列表
}

// ==================== 购物车相关 API ====================
export const cartApi = {
  getList: () => request.get('/cart/list'),                      // 获取购物车列表
  add: (productId, quantity = 1) => request.post('/cart/add', null, { params: { productId, quantity } }),  // 添加商品到购物车
  updateQuantity: (cartId, quantity) => request.put('/cart/update', null, { params: { cartId, quantity } }), // 更新商品数量
  delete: (id) => request.delete(`/cart/${id}`),                 // 删除购物车项
  updateChecked: (id, checked) => request.put(`/cart/check/${id}`, null, { params: { checked } })  // 更新选中状态
}

// ==================== 订单相关 API ====================
export const orderApi = {
  create: (data) => request.post('/order/create', data),         // 创建订单
  getList: (params) => request.get('/order/list', { params }),   // 获取订单列表
  getDetail: (id) => request.get(`/order/${id}`),               // 获取订单详情
  cancel: (id) => request.put(`/order/cancel/${id}`),           // 取消订单
  pay: (id) => request.post(`/order/pay/${id}`),                // 支付订单
  confirm: (id) => request.put(`/order/confirm/${id}`),         // 确认收货
  applyReturn: (id) => request.put(`/order/return/${id}`)       // 申请退货
}

// ==================== 管理员 API ====================
export const adminApi = {
  // 仪表盘统计
  getDashboardStats: () => request.get('/admin/dashboard/stats'), // 获取统计数据

  // 商品管理（CRUD）
  getProductList: (params) => request.get('/admin/product/list', { params }),
  addProduct: (data) => request.post('/admin/product', data),
  updateProduct: (data) => request.put('/admin/product', data),
  deleteProduct: (id) => request.delete(`/admin/product/${id}`),

  // 分类管理（CRUD）
  getCategoryList: () => request.get('/admin/category/list'),
  addCategory: (data) => request.post('/admin/category', data),
  updateCategory: (data) => request.put('/admin/category', data),
  deleteCategory: (id) => request.delete(`/admin/category/${id}`),

  // 公告管理（CRUD）
  getBannerList: () => request.get('/admin/banner/list'),
  addBanner: (data) => request.post('/admin/banner', data),
  updateBanner: (data) => request.put('/admin/banner', data),
  deleteBanner: (id) => request.delete(`/admin/banner/${id}`),

  // 订单管理
  getOrderList: (params) => request.get('/admin/order/list', { params }),
  shipOrder: (id) => request.put(`/admin/order/ship/${id}`)     // 订单发货
}
