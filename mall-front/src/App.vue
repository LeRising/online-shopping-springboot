<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
</script>

<style>
body {
  background-color: var(--color-bg-page);
  color: var(--color-text-primary);
}

#app {
  min-height: 100dvh;
}

a {
  color: var(--el-color-primary);
  text-decoration: none;
}
a:hover {
  color: var(--el-color-primary-dark-2);
}
</style>
