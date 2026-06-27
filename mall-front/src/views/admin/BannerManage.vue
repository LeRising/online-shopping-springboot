<!--
  公告管理页面
  支持公告的增删改查操作
-->
<template>
  <div class="banner-manage">
    <!-- 标题栏 -->
    <div class="header">
      <h2>公告管理</h2>
      <el-button type="primary" @click="handleAdd">新增公告</el-button>
    </div>

    <!-- 公告列表表格 -->
    <el-table :data="banners" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="content" label="公告内容" min-width="400" />
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
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑公告' : '新增公告'" width="600px">
      <el-form :model="form" label-width="100px">
        <!-- 公告内容 -->
        <el-form-item label="公告内容" required>
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="4"
            placeholder="请输入公告内容"
          />
        </el-form-item>
        <!-- 排序值 -->
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

// ==================== 响应式状态 ====================
const banners = ref([])            // 公告列表
const loading = ref(false)
const dialogVisible = ref(false)   // 对话框显示状态
const isEdit = ref(false)          // 是否为编辑模式
const submitting = ref(false)      // 提交按钮加载状态
const form = ref({
  id: null,
  content: '',
  sort: 0
})

// ==================== 数据加载 ====================

/** 加载公告列表 */
const loadBanners = async () => {
  loading.value = true
  try {
    const res = await getAdminBannerList()
    banners.value = res.data || []
  } finally {
    loading.value = false
  }
}

// ==================== 事件处理 ====================

/** 打开新增对话框 */
const handleAdd = () => {
  isEdit.value = false
  form.value = { id: null, content: '', sort: 0 }
  dialogVisible.value = true
}

/** 打开编辑对话框 */
const handleEdit = (row) => {
  isEdit.value = true
  form.value = {
    id: row.id,
    content: row.content,
    sort: row.sort || 0
  }
  dialogVisible.value = true
}

/** 提交表单（新增/编辑） */
const handleSubmit = async () => {
  if (!form.value.content.trim()) {
    ElMessage.warning('请输入公告内容')
    return
  }

  submitting.value = true
  try {
    const data = {
      content: form.value.content,
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

/** 删除公告 */
const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该公告？', '提示')
  await deleteBanner(row.id)
  ElMessage.success('删除成功')
  loadBanners()
}

// ==================== 生命周期 ====================
onMounted(() => {
  loadBanners()
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
</style>
