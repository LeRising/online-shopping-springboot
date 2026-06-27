<!--
  分类管理页面
  支持分类的增删改查操作
-->
<template>
  <div class="category-manage">
    <!-- 标题栏 -->
    <div class="header">
      <h2>分类管理</h2>
      <el-button type="primary" @click="handleAdd">新增分类</el-button>
    </div>

    <!-- 分类列表表格 -->
    <el-table :data="categories" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名称" min-width="150" />
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
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '新增分类'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="分类名称" required>
          <el-input v-model="form.name" placeholder="请输入分类名称" />
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
import { getAdminCategoryList, addCategory, updateCategory, deleteCategory } from '../../api/admin'

// ==================== 响应式状态 ====================
const categories = ref([])         // 分类列表
const loading = ref(false)
const dialogVisible = ref(false)   // 对话框显示状态
const isEdit = ref(false)          // 是否为编辑模式
const submitting = ref(false)      // 提交按钮加载状态
const form = ref({
  id: null,
  name: '',
  sort: 0
})

// ==================== 数据加载 ====================

/** 加载分类列表 */
const loadCategories = async () => {
  loading.value = true
  try {
    const res = await getAdminCategoryList()
    categories.value = res.data || []
  } finally {
    loading.value = false
  }
}

// ==================== 事件处理 ====================

/** 打开新增对话框 */
const handleAdd = () => {
  isEdit.value = false
  form.value = { id: null, name: '', sort: 0 }
  dialogVisible.value = true
}

/** 打开编辑对话框 */
const handleEdit = (row) => {
  isEdit.value = true
  form.value = {
    id: row.id,
    name: row.name,
    sort: row.sort || 0
  }
  dialogVisible.value = true
}

/** 提交表单（新增/编辑） */
const handleSubmit = async () => {
  if (!form.value.name.trim()) {
    ElMessage.warning('请输入分类名称')
    return
  }

  submitting.value = true
  try {
    if (isEdit.value) {
      await updateCategory(form.value)
      ElMessage.success('修改成功')
    } else {
      await addCategory(form.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadCategories()
  } finally {
    submitting.value = false
  }
}

/** 删除分类 */
const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除分类"${row.name}"？`, '提示')
  await deleteCategory(row.id)
  ElMessage.success('删除成功')
  loadCategories()
}

// ==================== 生命周期 ====================
onMounted(() => {
  loadCategories()
})
</script>

<style scoped>
.category-manage {
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
</style>
