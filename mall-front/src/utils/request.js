/**
 * HTTP 请求工具
 * 基于 Axios 封装，统一处理请求/响应拦截、Token 注入、错误提示
 */
import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

// 创建 Axios 实例，设置基础路径和超时时间
const request = axios.create({
  baseURL: '/api',    // 通过 Vite 代理转发到后端 Gateway
  timeout: 10000      // 请求超时时间：10 秒
})

/**
 * 请求拦截器
 * 自动在请求头中添加 JWT Token
 */
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

/**
 * 响应拦截器
 * 统一处理后端返回的业务错误码
 */
request.interceptors.response.use(
  response => {
    const res = response.data

    // 业务错误码非 200 时，显示错误提示
    if (res.code !== 200) {
      ElMessage.error(res.msg || '请求失败')

      // 401 未授权：清除登录状态并跳转到登录页
      if (res.code === 401) {
        // 避免在登录页重复跳转
        if (router.currentRoute.value.path !== '/login') {
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          router.push('/login')
        }
      }
      return Promise.reject(new Error(res.msg))
    }

    // 业务成功，返回数据
    return res
  },
  error => {
    // HTTP 状态码 401：未授权
    if (error.response?.status === 401) {
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
