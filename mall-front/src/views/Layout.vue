<template>
  <el-container class="layout">
    <!-- 顶部导航 -->
    <el-header class="header">
      <div class="header-content">
        <div class="logo" @click="router.push('/')">
          <el-icon><ShoppingCart /></el-icon>
          <span>网上商城</span>
        </div>
        <div class="nav-right">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索商品"
            class="search-input"
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>

          <el-badge :content="cartCount" :hidden="cartCount === 0" class="cart-badge">
            <el-button text @click="router.push('/cart')">
              <el-icon size="20"><ShoppingCart /></el-icon>
            </el-button>
          </el-badge>

          <template v-if="userStore.isLoggedIn">
            <el-dropdown>
              <span class="user-info">
                <el-icon><User /></el-icon>
                {{ userStore.nickname }}
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="router.push('/user')">个人中心</el-dropdown-item>
                  <el-dropdown-item @click="router.push('/order')">我的订单</el-dropdown-item>
                  <el-dropdown-item v-if="userStore.isAdmin" @click="router.push('/admin')">
                    后台管理
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="router.push('/login')">登录</el-button>
            <el-button @click="router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </el-header>

    <!-- 主内容 -->
    <el-main class="main">
      <router-view />
    </el-main>

    <!-- 底部 -->
    <el-footer class="footer">
      <p>© 2024 网上商城 - 基于 SpringCloud 微服务架构</p>
    </el-footer>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import { getCartList } from '../api/order'

const router = useRouter()
const userStore = useUserStore()
const searchKeyword = ref('')
const cartCount = ref(0)

const handleSearch = () => {
  router.push({ path: '/', query: { keyword: searchKeyword.value } })
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

const loadCartCount = async () => {
  if (!userStore.isLoggedIn) return
  try {
    const res = await getCartList()
    cartCount.value = res.data?.length || 0
  } catch (e) {
    // ignore
  }
}

onMounted(() => {
  loadCartCount()
})
</script>

<style scoped>
.layout {
  min-height: 100dvh;
  display: flex;
  flex-direction: column;
}

/* ---- Header ---- */
.header {
  background: rgba(255,255,255,.85);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  box-shadow: 0 1px 3px rgba(0,0,0,.04), 0 1px 2px rgba(0,0,0,.03);
  padding: 0 24px;
  position: sticky;
  top: 0;
  z-index: 100;
  border-bottom: 1px solid rgba(229,231,235,.5);
}

.header-content {
  max-width: 1280px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
}

/* ---- Logo ---- */
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
  font-weight: 700;
  color: var(--el-color-primary);
  cursor: pointer;
  letter-spacing: -0.02em;
}
.logo .el-icon {
  font-size: 24px;
}

/* ---- Nav Right ---- */
.nav-right {
  display: flex;
  align-items: center;
  gap: 14px;
}

.search-input {
  width: 280px;
}

.cart-badge {
  margin-right: 4px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  color: var(--color-text-secondary);
  font-size: 14px;
  font-weight: 500;
}
.user-info:hover {
  color: var(--el-color-primary);
}

/* ---- Main ---- */
.main {
  flex: 1;
  max-width: 1280px;
  width: 100%;
  margin: 0 auto;
  padding: 24px 24px 40px;
}

/* ---- Footer ---- */
.footer {
  text-align: center;
  color: var(--color-text-tertiary);
  padding: 28px 20px;
  background: var(--color-bg-surface);
  border-top: 1px solid var(--el-border-color);
  font-size: 13px;
  letter-spacing: 0.02em;
}
</style>
