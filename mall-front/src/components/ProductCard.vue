<!--
  商品卡片组件
  展示商品图片、名称、价格、销量等信息
  点击跳转到商品详情页
-->
<template>
  <el-card class="product-card" shadow="hover" @click="goDetail">
    <!-- 商品图片 -->
    <img :src="product.image" class="product-img" />

    <!-- 商品信息 -->
    <div class="product-info">
      <!-- 商品名称（超出隐藏） -->
      <h4 class="product-name">{{ product.name }}</h4>

      <!-- 价格区域：现价 + 原价（划线） -->
      <div class="product-price">
        <span class="price">¥{{ product.price }}</span>
        <span class="original-price" v-if="product.originalPrice && product.originalPrice > product.price">
          ¥{{ product.originalPrice }}
        </span>
      </div>

      <!-- 销量信息 -->
      <div class="product-meta">
        <span>已售 {{ product.sales || 0 }}</span>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { useRouter } from 'vue-router'

// ==================== Props ====================
const props = defineProps({
  product: { type: Object, required: true }  // 商品数据对象
})

const router = useRouter()

/** 跳转到商品详情页 */
const goDetail = () => {
  router.push(`/product/${props.product.id}`)
}
</script>

<style scoped>
.product-card {
  margin-bottom: 20px;
  cursor: pointer;
  overflow: hidden;
  transition: transform .25s ease, box-shadow .25s ease;
  border: 1px solid var(--el-border-color);
}

/* 悬停上浮效果 */
.product-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--shadow-card-hover);
}

/* 移除卡片内边距 */
.product-card :deep(.el-card__body) {
  padding: 0;
}

/* 商品图片 */
.product-img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: block;
}

/* 商品信息区域 */
.product-info {
  padding: 14px 16px 16px;
}

/* 商品名称 */
.product-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;        /* 单行显示，超出省略 */
  margin-bottom: 10px;
  line-height: 1.4;
}

/* 价格区域 */
.product-price {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.price {
  font-size: 19px;
  color: var(--color-text-price);
  font-weight: 700;
  letter-spacing: -0.02em;
}

.original-price {
  font-size: 12px;
  color: var(--color-text-tertiary);
  text-decoration: line-through;  /* 划线价 */
}

/* 元信息（销量） */
.product-meta {
  font-size: 12px;
  color: var(--color-text-tertiary);
  margin-top: 6px;
}
</style>
