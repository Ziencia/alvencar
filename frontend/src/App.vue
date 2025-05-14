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
      <Footer></Footer>
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

navbar {
  background: lightskyblue;
}

header, footer {
  height: 30px;
  background: rgb(5, 94, 94)
}

main {
  flex: 1;
  background: rgb(70, 156, 145);
}
</style>
