<!--
  根组件
  提供路由出口和全局基础样式
-->
<template>
  <div id="app">
    <!-- 路由视图：根据当前路由渲染对应页面组件 -->
    <router-view />
  </div>
</template>

<script setup>
</script>

<style>
/* 全局基础样式 */
body {
  background-color: var(--color-bg-page);
  color: var(--color-text-primary);
}

#app {
  min-height: 100dvh; /* 使用 dvh 确保最小高度适配移动端浏览器 */
}

/* 全局链接样式 */
a {
  color: var(--el-color-primary);
  text-decoration: none;
}
a:hover {
  color: var(--el-color-primary-dark-2);
}
</style>
