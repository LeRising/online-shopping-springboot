<template>
  <el-container class="admin-layout">
    <el-aside width="200px" class="aside">
      <div class="logo" @click="router.push('/')">
        <el-icon><Setting /></el-icon>
        <span>后台管理</span>
      </div>
      <el-menu :default-active="route.path" router class="menu">
        <el-menu-item index="/admin">
          <el-icon><DataAnalysis /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/admin/products">
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/orders">
          <el-icon><List /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span>管理员: {{ userStore.nickname }}</span>
        <el-button text @click="router.push('/')">返回前台</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../../store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
</script>

<style scoped>
.admin-layout {
  min-height: 100dvh;
}

.aside {
  background: linear-gradient(180deg, #1e293b 0%, #0f172a 100%);
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 20px 20px 24px;
  color: #fff;
  font-size: 17px;
  font-weight: 700;
  cursor: pointer;
  letter-spacing: -0.01em;
}

.menu {
  border: none;
  background: transparent;
}

.menu .el-menu-item {
  color: #94a3b8;
  font-weight: 500;
  margin: 2px 8px;
  border-radius: var(--radius-sm);
  transition: all .15s ease;
}

.menu .el-menu-item:hover {
  background: rgba(255,255,255,.06);
  color: #e2e8f0;
}

.menu .el-menu-item.is-active {
  background: rgba(37,99,235,.2);
  color: #93c5fd;
  font-weight: 600;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--color-bg-surface);
  border-bottom: 1px solid var(--el-border-color);
  padding: 0 24px;
  font-size: 14px;
  color: var(--color-text-secondary);
}
</style>
