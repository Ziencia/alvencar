<script setup>
import Header from '@/components/Header.vue'
import NavBar from '@/components/NavBar.vue'
import Footer from '@/components/Footer.vue'
</script>

<template>
  <div class="app-wrapper">
    <div class="app-container">
      <Header></Header>
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
header {
  height: 20px;
  background: #313715
}

main {
  flex: 1;
  background: #DFF2D8
}
</style>
