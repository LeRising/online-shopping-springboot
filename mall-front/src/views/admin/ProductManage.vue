<template>
  <div class="product-manage">
    <div class="header-bar">
      <h2>商品管理</h2>
      <el-button type="primary" @click="openDialog()">新增商品</el-button>
    </div>

    <el-table :data="products">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="图片" width="80">
        <template #default="{ row }">
          <img :src="row.image" style="width: 50px; height: 50px; object-fit: cover;" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称" min-width="200" />
      <el-table-column prop="price" label="价格" width="100" />
      <el-table-column prop="stock" label="库存" width="80" />
      <el-table-column prop="sales" label="销量" width="80" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button text size="small" @click="openDialog(row)">编辑</el-button>
          <el-button text size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination v-model:current-page="currentPage" :page-size="10" :total="total"
        layout="prev, pager, next" @current-change="loadProducts" />
    </div>

    <!-- 商品编辑弹窗 -->
    <el-dialog v-model="showDialog" :title="editingProduct ? '编辑商品' : '新增商品'" width="600">
      <el-form :model="productForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="productForm.name" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="productForm.categoryId" placeholder="选择分类" style="width: 100%">
            <el-option v-for="cat in categoryOptions" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="productForm.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="原价">
          <el-input-number v-model="productForm.originalPrice" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="productForm.stock" :min="0" />
        </el-form-item>
        <el-form-item label="图片URL">
          <el-input v-model="productForm.image" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="productForm.description" type="textarea" :rows="3" />
        </el-form-item>
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

const products = ref([])
const categoryOptions = ref([])
const currentPage = ref(1)
const total = ref(0)
const showDialog = ref(false)
const editingProduct = ref(null)

const productForm = reactive({
  name: '', categoryId: 1, price: 0, originalPrice: 0,
  stock: 0, image: '', description: '', status: 1
})

const loadProducts = async () => {
  const res = await getProductList({ page: currentPage.value, size: 10 })
  products.value = res.data?.records || []
  total.value = res.data?.total || 0
}

const openDialog = (product = null) => {
  editingProduct.value = product
  if (product) {
    Object.assign(productForm, product)
  } else {
    Object.assign(productForm, {
      name: '', categoryId: 1, price: 0, originalPrice: 0,
      stock: 0, image: '', description: '', status: 1
    })
  }
  showDialog.value = true
}

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

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该商品？', '提示')
  await deleteProduct(row.id)
  ElMessage.success('已删除')
  loadProducts()
}

const loadCategories = async () => {
  try {
    const res = await getCategoryList()
    categoryOptions.value = res.data || []
  } catch (e) {
    // ignore
  }
}

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
