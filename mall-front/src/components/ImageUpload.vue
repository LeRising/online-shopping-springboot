<template>
  <div class="image-upload">
    <el-upload
      :action="uploadUrl"
      :show-file-list="false"
      :on-success="handleSuccess"
      :before-upload="beforeUpload"
      :headers="uploadHeaders"
      drag
    >
      <div v-if="modelValue" class="preview-container">
        <img :src="getImageUrl(modelValue)" class="preview-img" />
        <div class="preview-overlay">
          <el-icon><Edit /></el-icon>
          <span>重新上传</span>
        </div>
      </div>
      <div v-else class="upload-placeholder">
        <el-icon class="upload-icon"><Upload /></el-icon>
        <div class="upload-text">点击或拖拽上传图片</div>
        <div class="upload-hint">支持 jpg、png、gif 格式，最大 10MB</div>
      </div>
    </el-upload>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])

// 上传地址
const uploadUrl = '/api/file/upload'

// 上传请求头（携带 Token）
const uploadHeaders = computed(() => {
  const token = localStorage.getItem('token')
  return token ? { Authorization: `Bearer ${token}` } : {}
})

// 上传前校验
const beforeUpload = (file) => {
  const isImage = ['image/jpeg', 'image/png', 'image/gif'].includes(file.type)
  if (!isImage) {
    ElMessage.error('只能上传 jpg/png/gif 格式的图片')
    return false
  }
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过 10MB')
    return false
  }
  return true
}

// 上传成功回调
const handleSuccess = (response) => {
  if (response.code === 200) {
    emit('update:modelValue', response.data)
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.msg || '上传失败')
  }
}

// 获取图片完整 URL
const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return url
}
</script>

<style scoped>
.image-upload {
  width: 100%;
}

.image-upload :deep(.el-upload) {
  width: 100%;
}

.image-upload :deep(.el-upload-dragger) {
  width: 100%;
  padding: 0;
  border: 2px dashed var(--el-border-color);
  border-radius: 8px;
  transition: border-color 0.3s;
}

.image-upload :deep(.el-upload-dragger:hover) {
  border-color: var(--el-color-primary);
}

.preview-container {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
  border-radius: 6px;
}

.preview-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  opacity: 0;
  transition: opacity 0.3s;
}

.preview-container:hover .preview-overlay {
  opacity: 1;
}

.preview-overlay .el-icon {
  font-size: 24px;
  margin-bottom: 8px;
}

.upload-placeholder {
  padding: 40px 20px;
  text-align: center;
}

.upload-icon {
  font-size: 48px;
  color: var(--el-text-color-placeholder);
  margin-bottom: 16px;
}

.upload-text {
  font-size: 16px;
  color: var(--el-text-regular);
  margin-bottom: 8px;
}

.upload-hint {
  font-size: 12px;
  color: var(--el-text-placeholder);
}
</style>
