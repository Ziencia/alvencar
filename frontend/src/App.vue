<script setup>
import NavBar from '@/components/NavBar.vue'
</script>

<template>
  <div class="app-wrapper">
    <div class="app-container">
      <NavBar></NavBar>
      <main><router-view></router-view></main>
    </div>
  </div>
</template>

<style scoped>
.app-wrapper {
  display: flex;
  justify-content: center;
}
.app-container {
  width: 100%;
  max-width: 80%;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
main {
  flex: 1;
  background: #DFF2D8
}
</style>
