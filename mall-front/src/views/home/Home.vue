<template>
  <div class="home">
    <!-- 公告轮播 -->
    <el-carousel v-if="banners.length" class="notice-bar" :interval="4000" indicator-position="none">
      <el-carousel-item v-for="banner in banners" :key="banner.id">
        <div class="notice-content">
          <el-icon><Bell /></el-icon>
          <span>{{ banner.content }}</span>
        </div>
      </el-carousel-item>
    </el-carousel>

    <!-- 分类导航 -->
    <div class="section">
      <h3 class="section-title">商品分类</h3>
      <div class="category-list">
        <div
          class="category-item"
          :class="{ active: browsing && selectedCategory === null }"
          @click="selectCategory(null)"
        >
          全部
        </div>
        <div
          v-for="cat in categories"
          :key="cat.id"
          class="category-item"
          :class="{ active: selectedCategory === cat.id }"
          @click="selectCategory(cat.id)"
        >
          {{ cat.name }}
        </div>
      </div>
    </div>

    <!-- 热门商品（首页默认展示） -->
    <div class="section" v-if="!browsing && !keyword">
      <h3 class="section-title">🔥 热门商品</h3>
      <el-row :gutter="20" v-loading="loadingHot">
        <el-col :xs="12" :sm="8" :md="6" v-for="product in hotProducts" :key="product.id">
          <ProductCard :product="product" />
        </el-col>
      </el-row>
    </div>

    <!-- 新品推荐（首页默认展示） -->
    <div class="section" v-if="!browsing && !keyword">
      <h3 class="section-title">🆕 新品推荐</h3>
      <el-row :gutter="20" v-loading="loadingNew">
        <el-col :xs="12" :sm="8" :md="6" v-for="product in newProducts" :key="product.id">
          <ProductCard :product="product" />
        </el-col>
      </el-row>
    </div>

    <!-- 商品列表（分类浏览/搜索） -->
    <div class="section" v-if="browsing || keyword">
      <h3 class="section-title">
        {{ keyword ? `搜索: ${keyword}` : (selectedCategory ? '分类商品' : '全部商品') }}
      </h3>
      <el-row :gutter="20" v-loading="loadingProducts">
        <el-col :xs="12" :sm="8" :md="6" v-for="product in products" :key="product.id">
          <ProductCard :product="product" />
        </el-col>
      </el-row>
      <div class="pagination" v-if="total > pageSize">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="loadProducts"
        />
      </div>
      <el-empty v-if="products.length === 0" description="暂无商品" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getBannerList, getCategoryList, getHotProducts, getNewProducts, getProductList } from '../../api/product'
import ProductCard from '../../components/ProductCard.vue'

const route = useRoute()
const router = useRouter()

const banners = ref([])
const categories = ref([])
const hotProducts = ref([])
const newProducts = ref([])
const products = ref([])
const selectedCategory = ref(null)
const browsing = ref(false)
const keyword = ref('')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)
const loadingBanners = ref(false)
const loadingHot = ref(false)
const loadingNew = ref(false)
const loadingProducts = ref(false)

const loadBanners = async () => {
  loadingBanners.value = true
  try {
    const res = await getBannerList()
    banners.value = res.data || []
  } finally {
    loadingBanners.value = false
  }
}

const loadCategories = async () => {
  const res = await getCategoryList()
  categories.value = res.data || []
}

const loadHotProducts = async () => {
  loadingHot.value = true
  try {
    const res = await getHotProducts()
    hotProducts.value = res.data || []
  } finally {
    loadingHot.value = false
  }
}

const loadNewProducts = async () => {
  loadingNew.value = true
  try {
    const res = await getNewProducts()
    newProducts.value = res.data || []
  } finally {
    loadingNew.value = false
  }
}

const loadProducts = async () => {
  loadingProducts.value = true
  try {
    const params = {
    page: currentPage.value,
    size: pageSize.value
  }
  if (selectedCategory.value) params.categoryId = selectedCategory.value
  if (keyword.value) params.keyword = keyword.value
  const res = await getProductList(params)
  products.value = res.data?.records || []
  total.value = res.data?.total || 0
  } finally {
    loadingProducts.value = false
  }
}

const selectCategory = (catId) => {
  selectedCategory.value = catId
  browsing.value = true
  currentPage.value = 1
  loadProducts()
}

watch(() => route.query.keyword, (val) => {
  keyword.value = val || ''
  if (val) {
    browsing.value = false
    loadProducts()
  }
}, { immediate: true })

onMounted(() => {
  loadBanners()
  loadCategories()
  loadHotProducts()
  loadNewProducts()
  if (keyword.value) {
    loadProducts()
  }
})
</script>

<style scoped>
.home {
  padding-bottom: 20px;
}

/* ---- Notice Bar ---- */
.notice-bar {
  margin-bottom: 24px;
  border-radius: var(--radius-lg);
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.notice-bar :deep(.el-carousel__container) {
  height: 48px;
}

.notice-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  height: 100%;
  padding: 0 24px;
  color: #fff;
  font-size: 15px;
  font-weight: 500;
}

.notice-content .el-icon {
  font-size: 18px;
  flex-shrink: 0;
}

/* ---- Section ---- */
.section {
  margin-bottom: 36px;
}

.section-title {
  font-size: 22px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 20px;
  padding-left: 14px;
  border-left: 4px solid var(--el-color-primary);
  letter-spacing: -0.01em;
  line-height: 1.3;
}

/* ---- Category Pills ---- */
.category-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.category-item {
  padding: 8px 22px;
  background: var(--color-bg-surface);
  border: 1px solid var(--el-border-color);
  border-radius: var(--el-border-radius-round);
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text-secondary);
  transition: all .25s ease;
  user-select: none;
}

.category-item:hover {
  border-color: var(--el-color-primary-light-5);
  color: var(--el-color-primary);
  background: var(--color-bg-accent);
}

.category-item.active {
  background: var(--el-color-primary);
  color: #fff;
  border-color: var(--el-color-primary);
  box-shadow: 0 2px 8px rgba(37,99,235,.25);
}

/* ---- Pagination ---- */
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 28px;
}
</style>
