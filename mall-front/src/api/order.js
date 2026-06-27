/**
 * 订单与购物车 API 导出文件
 * 从 index.js 中导出购物车、订单相关接口
 */
import { cartApi, orderApi, adminApi } from './index'

// 购物车操作
export const getCartList = cartApi.getList                  // 获取购物车列表
export const addToCart = cartApi.add                       // 添加商品到购物车
export const updateCartQuantity = cartApi.updateQuantity    // 更新商品数量
export const deleteCartItem = cartApi.delete               // 删除购物车项
export const updateCartCheck = cartApi.updateChecked        // 更新选中状态

// 订单操作（用户端）
export const createOrder = orderApi.create                 // 创建订单
export const getOrderList = orderApi.getList               // 获取订单列表
export const getOrderDetail = orderApi.getDetail           // 获取订单详情
export const cancelOrder = orderApi.cancel                 // 取消订单
export const payOrder = orderApi.pay                       // 支付订单
export const confirmOrder = orderApi.confirm               // 确认收货
export const returnOrder = orderApi.applyReturn            // 申请退货

// 订单管理（管理员端）
export const getAdminOrderList = adminApi.getOrderList     // 获取所有订单列表
export const shipOrder = adminApi.shipOrder                // 订单发货
