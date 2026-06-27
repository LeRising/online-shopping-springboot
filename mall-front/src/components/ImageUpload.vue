<!--
  图片上传组件
  支持点击选择和拖拽上传，显示上传进度和预览
  使用 v-model 双向绑定图片路径
-->
<template>
  <div class="image-upload">
    <!-- 上传区域：支持点击和拖拽 -->
    <div class="upload-area" @click="triggerUpload" @drop.prevent="handleDrop" @dragover.prevent>
      <!-- 隐藏的文件输入框 -->
      <input
        ref="fileInput"
        type="file"
        accept="image/jpeg,image/png,image/gif"
        style="display: none"
        @change="handleFileChange"
      />

      <!-- 已上传图片的预览 -->
      <div v-if="modelValue" class="preview-container">
        <img :src="getImageUrl(modelValue)" class="preview-img" />
        <div class="preview-overlay">
          <el-icon><Edit /></el-icon>
          <span>重新上传</span>
        </div>
      </div>

      <!-- 未上传时的占位提示 -->
      <div v-else class="upload-placeholder">
        <el-icon class="upload-icon"><Upload /></el-icon>
        <div class="upload-text">点击或拖拽上传图片</div>
        <div class="upload-hint">支持 jpg、png、gif 格式，最大 10MB</div>
      </div>
    </div>

    <!-- 上传进度条 -->
    <el-progress v-if="uploading" :percentage="uploadPercent" style="margin-top: 8px" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

// ==================== Props & Emits ====================
const props = defineProps({
  modelValue: {              // v-model 绑定的图片路径
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])  // 更新 v-model 值

// ==================== 响应式状态 ====================
const fileInput = ref(null)        // 文件输入框引用
const uploading = ref(false)       // 是否正在上传
const uploadPercent = ref(0)       // 上传进度百分比

// ==================== 方法 ====================

/** 触发文件选择对话框 */
const triggerUpload = () => {
  fileInput.value.click()
}

/** 处理文件选择事件 */
const handleFileChange = (e) => {
  const file = e.target.files[0]
  if (file) {
    uploadFile(file)
  }
  e.target.value = ''  // 清空 input，允许重复选择同一文件
}

/** 处理拖拽放下事件 */
const handleDrop = (e) => {
  const file = e.dataTransfer.files[0]
  if (file) {
    uploadFile(file)
  }
}

/**
 * 上传文件到服务器
 * @param {File} file - 要上传的文件对象
 */
const uploadFile = async (file) => {
  // 校验文件类型
  if (!['image/jpeg', 'image/png', 'image/gif'].includes(file.type)) {
    ElMessage.error('只能上传 jpg/png/gif 格式的图片')
    return
  }
  // 校验文件大小（10MB）
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过 10MB')
    return
  }

  uploading.value = true
  uploadPercent.value = 0

  try {
    const formData = new FormData()
    formData.append('file', file)

    // 发送上传请求，监听上传进度
    const res = await request.post('/file/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      onUploadProgress: (progressEvent) => {
        uploadPercent.value = Math.round((progressEvent.loaded * 100) / progressEvent.total)
      }
    })

    if (res.code === 200) {
      emit('update:modelValue', res.data)  // 更新父组件的图片路径
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

/**
 * 获取图片完整 URL
 * 如果是相对路径则直接返回，如果是完整 URL 也直接返回
 */
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

/* 预览容器 */
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

/* 悬停遮罩层 */
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

/* 上传占位区域 */
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
