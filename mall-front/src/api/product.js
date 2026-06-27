/**
 * 商品 API 导出文件
 * 从 index.js 中导出商品相关接口
 */
import { productApi, adminApi } from './index'

// 商品查询（前台）
export const getProductList = productApi.getList        // 获取商品列表
export const getProductDetail = productApi.getDetail    // 获取商品详情
export const getHotProducts = productApi.getHot         // 获取热门商品
export const getNewProducts = productApi.getNew         // 获取新品推荐
export const getCategoryList = productApi.getCategoryList   // 获取分类列表
export const getBannerList = productApi.getBannerList       // 获取公告列表

// 商品管理（后台）
export const addProduct = adminApi.addProduct           // 新增商品
export const updateProduct = adminApi.updateProduct     // 更新商品
export const deleteProduct = adminApi.deleteProduct     // 删除商品
