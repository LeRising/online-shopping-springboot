import { cartApi, orderApi, adminApi } from './index'

// 购物车
export const getCartList = cartApi.getList
export const addToCart = cartApi.add
export const updateCartQuantity = cartApi.updateQuantity
export const deleteCartItem = cartApi.delete
export const updateCartCheck = cartApi.updateChecked

// 订单
export const createOrder = orderApi.create
export const getOrderList = orderApi.getList
export const getOrderDetail = orderApi.getDetail
export const cancelOrder = orderApi.cancel
export const payOrder = orderApi.pay
export const confirmOrder = orderApi.confirm
export const returnOrder = orderApi.applyReturn

// 管理员
export const getAdminOrderList = adminApi.getOrderList
export const shipOrder = adminApi.shipOrder
