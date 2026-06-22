import request from '../utils/request'

export function getProductList(params) {
  return request.get('/product/list', { params })
}

export function getProductDetail(id) {
  return request.get(`/product/${id}`)
}

export function getHotProducts() {
  return request.get('/product/hot')
}

export function getNewProducts() {
  return request.get('/product/new')
}

export function getCategoryList() {
  return request.get('/product/category/list')
}

export function getBannerList() {
  return request.get('/banner/list')
}

// 管理员接口
export function addProduct(data) {
  return request.post('/admin/product', data)
}

export function updateProduct(data) {
  return request.put('/admin/product', data)
}

export function deleteProduct(id) {
  return request.delete(`/admin/product/${id}`)
}
