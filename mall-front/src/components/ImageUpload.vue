<template>
  <div class="image-upload">
    <div class="upload-area" @click="triggerUpload" @drop.prevent="handleDrop" @dragover.prevent>
      <input
        ref="fileInput"
        type="file"
        accept="image/jpeg,image/png,image/gif"
        style="display: none"
        @change="handleFileChange"
      />
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
    </div>
    <el-progress v-if="uploading" :percentage="uploadPercent" style="margin-top: 8px" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])

const fileInput = ref(null)
const uploading = ref(false)
const uploadPercent = ref(0)

// 触发文件选择
const triggerUpload = () => {
  fileInput.value.click()
}

// 处理文件选择
const handleFileChange = (e) => {
  const file = e.target.files[0]
  if (file) {
    uploadFile(file)
  }
  // 清空 input 以便重复选择同一文件
  e.target.value = ''
}

// 处理拖拽
const handleDrop = (e) => {
  const file = e.dataTransfer.files[0]
  if (file) {
    uploadFile(file)
  }
}

// 上传文件
const uploadFile = async (file) => {
  // 校验文件类型
  if (!['image/jpeg', 'image/png', 'image/gif'].includes(file.type)) {
    ElMessage.error('只能上传 jpg/png/gif 格式的图片')
    return
  }
  // 校验文件大小
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过 10MB')
    return
  }

  uploading.value = true
  uploadPercent.value = 0

  try {
    const formData = new FormData()
    formData.append('file', file)

    const res = await request.post('/file/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      onUploadProgress: (progressEvent) => {
        uploadPercent.value = Math.round((progressEvent.loaded * 100) / progressEvent.total)
      }
    })

    if (res.code === 200) {
      emit('update:modelValue', res.data)
      ElMessage.success('上传成功')
    } else {
      ElMessage.error(res.msg || '上传失败')
    }
  } catch (e) {
    ElMessage.error('上传失败')
  } finally {
    uploading.value = false
    uploadPercent.value = 0
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

.upload-area {
  width: 100%;
  border: 2px dashed var(--el-border-color);
  border-radius: 8px;
  cursor: pointer;
  transition: border-color 0.3s;
  overflow: hidden;
}

.upload-area:hover {
  border-color: var(--el-color-primary);
}

.preview-container {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
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
