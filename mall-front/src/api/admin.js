import { adminApi } from './index'

// 仪表盘
export const getDashboardStats = adminApi.getDashboardStats

// 分类管理
export const getAdminCategoryList = adminApi.getCategoryList
export const addCategory = adminApi.addCategory
export const updateCategory = adminApi.updateCategory
export const deleteCategory = adminApi.deleteCategory

// Banner管理
export const getAdminBannerList = adminApi.getBannerList
export const addBanner = adminApi.addBanner
export const updateBanner = adminApi.updateBanner
export const deleteBanner = adminApi.deleteBanner
