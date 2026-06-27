/**
 * 管理员 API 导出文件
 * 从 index.js 中导出后台管理相关接口
 */
import { adminApi } from './index'

// 仪表盘
export const getDashboardStats = adminApi.getDashboardStats  // 获取统计数据

// 分类管理
export const getAdminCategoryList = adminApi.getCategoryList  // 获取分类列表
export const addCategory = adminApi.addCategory              // 新增分类
export const updateCategory = adminApi.updateCategory        // 更新分类
export const deleteCategory = adminApi.deleteCategory        // 删除分类

// 公告管理
export const getAdminBannerList = adminApi.getBannerList     // 获取公告列表
export const addBanner = adminApi.addBanner                  // 新增公告
export const updateBanner = adminApi.updateBanner            // 更新公告
export const deleteBanner = adminApi.deleteBanner            // 删除公告
