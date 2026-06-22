import request from '../utils/request'

// 仪表盘
export function getDashboardStats() {
  return request.get('/admin/dashboard/stats')
}

// 分类管理
export function getAdminCategoryList() {
  return request.get('/admin/category/list')
}
export function addCategory(data) {
  return request.post('/admin/category', data)
}
export function updateCategory(data) {
  return request.put('/admin/category', data)
}
export function deleteCategory(id) {
  return request.delete(`/admin/category/${id}`)
}

// Banner管理
export function getAdminBannerList() {
  return request.get('/admin/banner/list')
}
export function addBanner(data) {
  return request.post('/admin/banner', data)
}
export function updateBanner(data) {
  return request.put('/admin/banner', data)
}
export function deleteBanner(id) {
  return request.delete(`/admin/banner/${id}`)
}
