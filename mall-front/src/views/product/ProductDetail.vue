<!--
  商品详情页
  展示商品图片、价格、描述等信息，支持加入购物车
-->
<template>
  <div class="product-detail" v-if="product" v-loading="loading">
    <el-row :gutter="30">
      <!-- 左侧：商品图片 -->
      <el-col :xs="24" :md="10">
        <img :src="product.image" class="detail-img" />
      </el-col>

      <!-- 右侧：商品信息 -->
      <el-col :xs="24" :md="14">
        <!-- 商品名称 -->
        <h1 class="title">{{ product.name }}</h1>

        <!-- 价格区域 -->
        <div class="price-box">
          <span class="price">¥{{ product.price }}</span>
          <span class="original" v-if="product.originalPrice > product.price">
            原价: ¥{{ product.originalPrice }}
          </span>
        </div>

        <!-- 商品元信息 -->
        <div class="meta">
          <span>销量: {{ product.sales }}</span>
          <span>库存: {{ product.stock }}</span>
          <span>分类: {{ product.categoryName }}</span>
        </div>

        <!-- 商品描述 -->
        <div class="description">
          <h4>商品描述</h4>
          <p>{{ product.description || '暂无描述' }}</p>
        </div>

        <!-- 操作区域：数量选择 + 加入购物车 -->
        <div class="actions">
          <el-input-number v-model="quantity" :min="1" :max="product.stock" />
          <el-button type="primary" size="large" @click="handleAddToCart">
            <el-icon><ShoppingCart /></el-icon>
            加入购物车
          </el-button>
        </div>
      </el-col>
    </el-row>
  </div>

  <!-- 加载中占位 -->
  <el-empty v-else description="加载中..." />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProductDetail } from '../../api/product'
import { addToCart } from '../../api/order'
import { useUserStore } from '../../store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// ==================== 响应式状态 ====================
const product = ref(null)      // 商品详情数据
const quantity = ref(1)        // 选择数量
const loading = ref(false)     // 加载状态

/** 加载商品详情 */
const loadProduct = async () => {
  loading.value = true
  try {
    const res = await getProductDetail(route.params.id)
    product.value = res.data
  } finally {
    loading.value = false
  }
}

/** 加入购物车（需登录） */
const handleAddToCart = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  try {
    await addToCart(product.value.id, quantity.value)
    ElMessage.success('已添加到购物车')
  } catch (e) {
    // 错误由拦截器统一处理
  }
}

onMounted(() => {
  loadProduct()
})
</script>

<style scoped>
.product-detail {
  background: var(--color-bg-surface);
  padding: 32px;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-card);
}

.detail-img {
  width: 100%;
  max-height: 440px;
  object-fit: cover;
  border-radius: var(--radius-md);
}

.title {
  font-size: 26px;
  font-weight: 700;
  color: var(--color-text-primary);
  letter-spacing: -0.01em;
  margin-bottom: 20px;
  line-height: 1.3;
}

/* 价格区域 */
.price-box {
  background: linear-gradient(135deg, #fef2f2, #fff5f5);
  padding: 20px 24px;
  border-radius: var(--radius-md);
  margin-bottom: 20px;
  border: 1px solid rgba(239,68,68,.1);
}

.price {
  font-size: 32px;
  color: var(--color-text-price);
  font-weight: 800;
  letter-spacing: -0.03em;
}

.original {
  font-size: 15px;
  color: var(--color-text-tertiary);
  text-decoration: line-through;
  margin-left: 14px;
  font-weight: 400;
}

/* 元信息 */
.meta {
  display: flex;
  gap: 28px;
  color: var(--color-text-secondary);
  font-size: 14px;
  margin-bottom: 24px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--el-border-color);
}

/* 描述区域 */
.description {
  margin-bottom: 28px;
}

.description h4 {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 10px;
}

.description p {
  color: var(--color-text-secondary);
  line-height: 1.8;
  font-size: 14px;
}

/* 操作区域 */
.actions {
  display: flex;
  align-items: center;
  gap: 16px;
}
</style>
