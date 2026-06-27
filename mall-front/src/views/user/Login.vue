<!--
  用户登录页
  表单验证 + 调用登录接口 + 保存 Token
-->
<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2 class="title">用户登录</h2>

      <!-- 登录表单 -->
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0">
        <!-- 用户名 -->
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" size="large" />
        </el-form-item>

        <!-- 密码（支持回车登录） -->
        <el-form-item prop="password">
          <el-input v-model="form.password" placeholder="密码" prefix-icon="Lock" type="password"
            show-password size="large" @keyup.enter="handleLogin" />
        </el-form-item>

        <!-- 登录按钮 -->
        <el-form-item>
          <el-button type="primary" size="large" style="width: 100%" :loading="loading" @click="handleLogin">
            登录
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 注册链接 -->
      <div class="footer-link">
        还没有账号？<router-link to="/register">立即注册</router-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../../api/user'
import { useUserStore } from '../../store/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()          // 表单引用（用于验证）
const loading = ref(false)     // 登录按钮加载状态

// 表单数据
const form = reactive({
  username: '',
  password: ''
})

// 表单验证规则
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

/** 执行登录 */
const handleLogin = async () => {
  await formRef.value.validate()    // 触发表单验证
  loading.value = true
  try {
    const res = await login(form)
    userStore.setLogin(res.data)    // 保存登录状态
    ElMessage.success('登录成功')
    router.push('/')                // 跳转首页
  } catch (e) {
    // 错误由拦截器统一处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 登录页全屏居中布局 */
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

/* 登录卡片 */
.login-card {
  width: 420px;
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
  margin-bottom: 32px;
  color: var(--color-text-primary);
  font-size: 26px;
  font-weight: 700;
  letter-spacing: -0.02em;
}

/* 底部注册链接 */
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
