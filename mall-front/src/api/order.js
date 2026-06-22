import request from '../utils/request'

// 购物车
export function getCartList() {
  return request.get('/cart/list')
}

export function addToCart(productId, quantity = 1) {
  return request.post('/cart/add', null, { params: { productId, quantity } })
}

export function updateCartQuantity(cartId, quantity) {
  return request.put('/cart/update', null, { params: { cartId, quantity } })
}

export function deleteCartItem(id) {
  return request.delete(`/cart/${id}`)
}

// 订单
export function createOrder(data) {
  return request.post('/order/create', data)
}

export function getOrderList(params) {
  return request.get('/order/list', { params })
}

export function getOrderDetail(id) {
  return request.get(`/order/${id}`)
}

export function cancelOrder(id) {
  return request.put(`/order/cancel/${id}`)
}

export function payOrder(id) {
  return request.post(`/order/pay/${id}`)
}

// 确认收货
export function confirmOrder(id) {
  return request.put(`/order/confirm/${id}`)
}

// 申请退货
export function returnOrder(id) {
  return request.put(`/order/return/${id}`)
}

// 购物车选中同步
export function updateCartCheck(cartId, checked) {
  return request.put(`/cart/check/${cartId}`, null, { params: { checked } })
}

// 管理员
export function getAdminOrderList(params) {
  return request.get('/admin/order/list', { params })
}

export function shipOrder(id) {
  return request.put(`/admin/order/ship/${id}`)
}
