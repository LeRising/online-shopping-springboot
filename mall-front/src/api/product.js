import { productApi, adminApi } from './index'

// 商品相关
export const getProductList = productApi.getList
export const getProductDetail = productApi.getDetail
export const getHotProducts = productApi.getHot
export const getNewProducts = productApi.getNew
export const getCategoryList = productApi.getCategoryList
export const getBannerList = productApi.getBannerList

// 管理员接口
export const addProduct = adminApi.addProduct
export const updateProduct = adminApi.updateProduct
export const deleteProduct = adminApi.deleteProduct
