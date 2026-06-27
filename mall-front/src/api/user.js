/**
 * 用户 API 导出文件
 * 从 index.js 中导出用户相关接口，便于按模块引入
 */
import { userApi } from './index'

// 用户认证
export const register = userApi.register           // 用户注册
export const login = userApi.login                 // 用户登录

// 用户信息
export const getUserInfo = userApi.getInfo          // 获取用户信息
export const updateUserInfo = userApi.updateInfo    // 更新用户信息

// 收货地址管理
export const getAddressList = userApi.getAddressList    // 获取地址列表
export const addAddress = userApi.addAddress            // 新增地址
export const updateAddress = userApi.updateAddress      // 更新地址
export const deleteAddress = userApi.deleteAddress      // 删除地址
