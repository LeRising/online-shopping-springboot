<template>
  <div class="banner-manage">
    <div class="header">
      <h2>轮播图管理</h2>
      <el-button type="primary" @click="handleAdd">新增轮播图</el-button>
    </div>

    <el-table :data="banners" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="图片" width="200">
        <template #default="{ row }">
          <img :src="row.image" class="banner-img" />
        </template>
      </el-table-column>
      <el-table-column prop="url" label="跳转链接" min-width="200" />
      <el-table-column prop="sort" label="排序" width="100" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button type="primary" text size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" text size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑轮播图' : '新增轮播图'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="选择商品" required>
          <el-select
            v-model="form.productId"
            placeholder="请选择商品"
            filterable
            style="width: 100%"
            @change="handleProductChange"
          >
            <el-option
              v-for="product in productList"
              :key="product.id"
              :label="product.name"
              :value="product.id"
            >
              <div class="product-option">
                <img :src="product.image" class="option-img" />
                <span>{{ product.name }}</span>
                <span class="option-price">¥{{ product.price }}</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品图片" v-if="form.image">
          <img :src="form.image" class="preview-img" />
        </el-form-item>
        <el-form-item label="跳转链接" v-if="form.url">
          <el-input v-model="form.url" disabled />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :max="999" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminBannerList, addBanner, updateBanner, deleteBanner } from '../../api/admin'
import { getProductList } from '../../api/product'

const banners = ref([])
const productList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const form = ref({
  id: null,
  productId: null,
  image: '',
  url: '',
  sort: 0
})

// 加载轮播图列表
const loadBanners = async () => {
  loading.value = true
  try {
    const res = await getAdminBannerList()
    banners.value = res.data || []
  } finally {
    loading.value = false
  }
}

// 加载商品列表（用于选择）
const loadProducts = async () => {
  try {
    const res = await getProductList({ page: 1, size: 100 })
    productList.value = res.data?.records || []
  } catch (e) {
    console.error('加载商品列表失败', e)
  }
}

// 选择商品后自动填充图片和链接
const handleProductChange = (productId) => {
  const product = productList.value.find(p => p.id === productId)
  if (product) {
    form.value.image = product.image
    form.value.url = `/product/${product.id}`
  }
}

const handleAdd = () => {
  isEdit.value = false
  form.value = { id: null, productId: null, image: '', url: '', sort: 0 }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  // 从 url 中提取 productId
  const match = row.url?.match(/\/product\/(\d+)/)
  const productId = match ? parseInt(match[1]) : null
  form.value = {
    id: row.id,
    productId: productId,
    image: row.image,
    url: row.url || '',
    sort: row.sort || 0
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!form.value.productId) {
    ElMessage.warning('请选择商品')
    return
  }

  submitting.value = true
  try {
    const data = {
      image: form.value.image,
      url: form.value.url,
      sort: form.value.sort
    }
    if (isEdit.value) {
      data.id = form.value.id
      await updateBanner(data)
      ElMessage.success('修改成功')
    } else {
      await addBanner(data)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadBanners()
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该轮播图？', '提示')
  await deleteBanner(row.id)
  ElMessage.success('删除成功')
  loadBanners()
}

onMounted(() => {
  loadBanners()
  loadProducts()
})
</script>

<style scoped>
.banner-manage {
  background: var(--color-bg-surface);
  padding: 24px;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-card);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
}

.banner-img {
  width: 150px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid var(--el-border-color);
}

.preview-img {
  width: 200px;
  height: 100px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid var(--el-border-color);
}

.product-option {
  display: flex;
  align-items: center;
  gap: 10px;
}

.option-img {
  width: 40px;
  height: 40px;
  object-fit: cover;
  border-radius: 4px;
}

.option-price {
  margin-left: auto;
  color: var(--el-color-danger);
  font-weight: 600;
}
</style>
