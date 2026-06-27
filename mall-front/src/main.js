/**
 * 应用入口文件
 * 负责初始化 Vue 应用并注册全局依赖
 */
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './styles/theme.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import App from './App.vue'
import router from './router'

// 创建 Vue 应用实例
const app = createApp(App)

// 注册所有 Element Plus 图标组件为全局组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 注册插件：Pinia 状态管理、路由、Element Plus（中文语言包）
app.use(createPinia())
app.use(router)
app.use(ElementPlus, { locale: zhCn })

// 挂载应用到 #app 节点
app.mount('#app')
