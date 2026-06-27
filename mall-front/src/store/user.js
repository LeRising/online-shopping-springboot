/**
 * 用户状态管理（Pinia Store）
 * 管理用户登录状态、Token、用户信息
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  // ==================== 状态 ====================
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))

  // ==================== 计算属性 ====================
  const isLoggedIn = computed(() => !!token.value)                    // 是否已登录
  const isAdmin = computed(() => userInfo.value?.role === 1)          // 是否管理员（role=1）
  const nickname = computed(() => userInfo.value?.nickname || userInfo.value?.username || '')  // 显示昵称

  // ==================== 操作方法 ====================

  /**
   * 登录成功后保存用户信息
   * @param {Object} data - 登录响应数据，包含 token、userId、username、nickname、role
   */
  function setLogin(data) {
    token.value = data.token
    userInfo.value = {
      userId: data.userId,
      username: data.username,
      nickname: data.nickname,
      role: data.role
    }
    // 持久化到 localStorage
    localStorage.setItem('token', data.token)
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  /**
   * 退出登录，清除所有用户数据
   */
  function logout() {
    token.value = ''
    userInfo.value = {}
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  return { token, userInfo, isLoggedIn, isAdmin, nickname, setLogin, logout }
})
