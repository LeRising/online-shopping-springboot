<!--
  仪表盘页面
  展示商品、订单、用户的统计数据
-->
<template>
  <div class="dashboard">
    <h2>仪表盘</h2>

    <!-- 统计卡片 -->
    <el-row :gutter="20" v-loading="loading">
      <!-- 商品总数 -->
      <el-col :xs="24" :sm="8" style="margin-bottom: 16px">
        <el-card shadow="hover">
          <template #header>
            <el-icon><Goods /></el-icon> 商品总数
          </template>
          <div class="stat-value">{{ stats.productCount }}</div>
        </el-card>
      </el-col>

      <!-- 订单总数 -->
      <el-col :xs="24" :sm="8" style="margin-bottom: 16px">
        <el-card shadow="hover">
          <template #header>
            <el-icon><List /></el-icon> 订单总数
          </template>
          <div class="stat-value">{{ stats.orderCount }}</div>
        </el-card>
      </el-col>

      <!-- 用户总数 -->
      <el-col :xs="24" :sm="8" style="margin-bottom: 16px">
        <el-card shadow="hover">
          <template #header>
            <el-icon><User /></el-icon> 用户总数
          </template>
          <div class="stat-value">{{ stats.userCount }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDashboardStats } from '../../api/admin'

const loading = ref(false)
const stats = ref({ productCount: 0, orderCount: 0, userCount: 0 })

/** 加载统计数据 */
const loadStats = async () => {
  loading.value = true
  try {
    const res = await getDashboardStats()
    if (res.data) {
      stats.value = res.data
    }
  } catch (e) {
    // 接口不可用时显示默认值 0
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard h2 {
  font-size: 22px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 24px;
}

/* 统计数值 */
.stat-value {
  font-size: 38px;
  font-weight: 800;
  color: var(--el-color-primary);
  text-align: center;
  padding: 24px 0;
  letter-spacing: -0.02em;
}
</style>
