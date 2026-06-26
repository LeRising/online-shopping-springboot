import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器：添加 Token
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器：统一错误处理
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.msg || '请求失败')
      if (res.code === 401) {
        // 只有在非登录页面时才跳转
        if (router.currentRoute.value.path !== '/login') {
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          router.push('/login')
        }
      }
      return Promise.reject(new Error(res.msg))
    }
    return res
  },
  error => {
    if (error.response?.status === 401) {
      // 只有在非登录页面时才显示"请先登录"并跳转
      if (router.currentRoute.value.path !== '/login') {
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        router.push('/login')
        ElMessage.error('请先登录')
      }
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default request
