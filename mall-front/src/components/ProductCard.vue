<template>
  <el-card class="product-card" shadow="hover" @click="goDetail">
    <img :src="product.image" class="product-img" />
    <div class="product-info">
      <h4 class="product-name">{{ product.name }}</h4>
      <div class="product-price">
        <span class="price">¥{{ product.price }}</span>
        <span class="original-price" v-if="product.originalPrice && product.originalPrice > product.price">
          ¥{{ product.originalPrice }}
        </span>
      </div>
      <div class="product-meta">
        <span>已售 {{ product.sales || 0 }}</span>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  product: { type: Object, required: true }
})

const router = useRouter()

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

.product-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--shadow-card-hover);
}

.product-card :deep(.el-card__body) {
  padding: 0;
}

.product-img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: block;
}

.product-info {
  padding: 14px 16px 16px;
}

.product-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 10px;
  line-height: 1.4;
}

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
  text-decoration: line-through;
}

.product-meta {
  font-size: 12px;
  color: var(--color-text-tertiary);
  margin-top: 6px;
}
</style>
