<!--
  用户注册页
  包含用户名、密码、确认密码、昵称（选填）表单
-->
<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2 class="title">用户注册</h2>

      <!-- 注册表单 -->
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0">
        <!-- 用户名 -->
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名（3-20个字符）" prefix-icon="User" size="large" />
        </el-form-item>

        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input v-model="form.password" placeholder="密码（6-20个字符）" prefix-icon="Lock"
            type="password" show-password size="large" />
        </el-form-item>

        <!-- 确认密码 -->
        <el-form-item prop="confirmPassword">
          <el-input v-model="form.confirmPassword" placeholder="确认密码" prefix-icon="Lock"
            type="password" show-password size="large" />
        </el-form-item>

        <!-- 昵称（选填） -->
        <el-form-item>
          <el-input v-model="form.nickname" placeholder="昵称（选填）" size="large" />
        </el-form-item>

        <!-- 注册按钮 -->
        <el-form-item>
          <el-button type="primary" size="large" style="width: 100%" :loading="loading" @click="handleRegister">
            注册
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 登录链接 -->
      <div class="footer-link">
        已有账号？<router-link to="/login">立即登录</router-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '../../api/user'

const router = useRouter()
const formRef = ref()          // 表单引用
const loading = ref(false)     // 按钮加载状态

// 表单数据
const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: ''
})

/** 自定义验证：确认密码一致性 */
const validateConfirm = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次密码不一致'))
  } else {
    callback()
  }
}

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度3-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度6-20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirm, trigger: 'blur' }
  ]
}

/** 执行注册 */
const handleRegister = async () => {
  await formRef.value.validate()    // 触发表单验证
  loading.value = true
  try {
    await register({
      username: form.username,
      password: form.password,
      nickname: form.nickname
    })
    ElMessage.success('注册成功，请登录')
    router.push('/login')           // 注册成功跳转登录页
  } catch (e) {
    // 错误由拦截器统一处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 注册页全屏居中布局 */
.login-page {
  min-height: 100dvh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(160deg, #eff6ff 0%, #e0e7ff 40%, #f0fdf4 100%);
  position: relative;
  overflow: hidden;
}

/* 装饰性背景光晕 */
.login-page::before {
  content: '';
  position: absolute;
  top: -180px;
  right: -120px;
  width: 420px;
  height: 420px;
  background: radial-gradient(circle, rgba(37,99,235,.06) 0%, transparent 70%);
  border-radius: 50%;
  pointer-events: none;
}

.login-page::after {
  content: '';
  position: absolute;
  bottom: -120px;
  left: -80px;
  width: 320px;
  height: 320px;
  background: radial-gradient(circle, rgba(249,115,22,.06) 0%, transparent 70%);
  border-radius: 50%;
  pointer-events: none;
}

/* 注册卡片 */
.login-card {
  width: 440px;
  padding: 36px 32px 28px;
  border-radius: var(--radius-xl);
  background: rgba(255,255,255,.92);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  box-shadow: var(--shadow-dialog);
  position: relative;
  z-index: 1;
}

.title {
  text-align: center;
  margin-bottom: 28px;
  color: var(--color-text-primary);
  font-size: 26px;
  font-weight: 700;
  letter-spacing: -0.02em;
}

/* 底部登录链接 */
.footer-link {
  text-align: center;
  color: var(--color-text-tertiary);
  font-size: 14px;
  margin-top: 8px;
}

.footer-link a {
  color: var(--el-color-primary);
  font-weight: 600;
}
</style>
