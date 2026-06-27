<!--
  商品管理页面
  支持商品的增删改查、图片上传、分页浏览
-->
<template>
  <div class="product-manage">
    <!-- 标题栏 -->
    <div class="header-bar">
      <h2>商品管理</h2>
      <el-button type="primary" @click="openDialog()">新增商品</el-button>
    </div>

    <!-- 商品列表表格 -->
    <el-table :data="products">
      <el-table-column prop="id" label="ID" width="80" />
      <!-- 商品图片缩略图 -->
      <el-table-column label="图片" width="80">
        <template #default="{ row }">
          <img :src="row.image" style="width: 50px; height: 50px; object-fit: cover;" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称" min-width="200" />
      <el-table-column prop="price" label="价格" width="100" />
      <el-table-column prop="stock" label="库存" width="80" />
      <el-table-column prop="sales" label="销量" width="80" />
      <!-- 状态标签 -->
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <!-- 操作按钮 -->
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button text size="small" @click="openDialog(row)">编辑</el-button>
          <el-button text size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination v-model:current-page="currentPage" :page-size="10" :total="total"
        layout="prev, pager, next" @current-change="loadProducts" />
    </div>

    <!-- ==================== 商品编辑弹窗 ==================== -->
    <el-dialog v-model="showDialog" :title="editingProduct ? '编辑商品' : '新增商品'" width="600">
      <el-form :model="productForm" label-width="80px">
        <!-- 商品名称 -->
        <el-form-item label="名称">
          <el-input v-model="productForm.name" />
        </el-form-item>

        <!-- 商品分类 -->
        <el-form-item label="分类">
          <el-select v-model="productForm.categoryId" placeholder="选择分类" style="width: 100%">
            <el-option v-for="cat in categoryOptions" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>

        <!-- 价格 -->
        <el-form-item label="价格">
          <el-input-number v-model="productForm.price" :min="0" :precision="2" />
        </el-form-item>

        <!-- 原价 -->
        <el-form-item label="原价">
          <el-input-number v-model="productForm.originalPrice" :min="0" :precision="2" />
        </el-form-item>

        <!-- 库存 -->
        <el-form-item label="库存">
          <el-input-number v-model="productForm.stock" :min="0" />
        </el-form-item>

        <!-- 商品图片（使用 ImageUpload 组件） -->
        <el-form-item label="商品图片">
          <ImageUpload v-model="productForm.image" />
        </el-form-item>

        <!-- 商品描述 -->
        <el-form-item label="描述">
          <el-input v-model="productForm.description" type="textarea" :rows="3" />
        </el-form-item>

        <!-- 上下架状态 -->
        <el-form-item label="状态">
          <el-radio-group v-model="productForm.status">
            <el-radio :value="1">上架</el-radio>
            <el-radio :value="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProductList, addProduct, updateProduct, deleteProduct, getCategoryList } from '../../api/product'
import ImageUpload from '../../components/ImageUpload.vue'

// ==================== 响应式状态 ====================
const products = ref([])             // 商品列表
const categoryOptions = ref([])      // 分类选项
const currentPage = ref(1)
const total = ref(0)
const showDialog = ref(false)        // 弹窗显示状态
const editingProduct = ref(null)     // 正在编辑的商品（null 为新增）

// 商品表单数据
const productForm = reactive({
  name: '', categoryId: 1, price: 0, originalPrice: 0,
  stock: 0, image: '', description: '', status: 1
})

// ==================== 数据加载 ====================

/** 加载商品列表（分页） */
const loadProducts = async () => {
  const res = await getProductList({ page: currentPage.value, size: 10 })
  products.value = res.data?.records || []
  total.value = res.data?.total || 0
}

/** 加载分类下拉选项 */
const loadCategories = async () => {
  try {
    const res = await getCategoryList()
    categoryOptions.value = res.data || []
  } catch (e) {
    // ignore
  }
}

// ==================== 事件处理 ====================

/** 打开弹窗（新增或编辑） */
const openDialog = (product = null) => {
  editingProduct.value = product
  if (product) {
    Object.assign(productForm, product)      // 填充编辑数据
  } else {
    Object.assign(productForm, {             // 重置为默认值
      name: '', categoryId: 1, price: 0, originalPrice: 0,
      stock: 0, image: '', description: '', status: 1
    })
  }
  showDialog.value = true
}

/** 保存商品（新增/编辑） */
const handleSave = async () => {
  if (editingProduct.value) {
    await updateProduct({ ...productForm, id: editingProduct.value.id })
  } else {
    await addProduct(productForm)
  }
  ElMessage.success('保存成功')
  showDialog.value = false
  loadProducts()
}

/** 删除商品 */
const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该商品？', '提示')
  await deleteProduct(row.id)
  ElMessage.success('已删除')
  loadProducts()
}

// ==================== 生命周期 ====================
onMounted(() => {
  loadProducts()
  loadCategories()
})
</script>

<style scoped>
.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}
</style>
