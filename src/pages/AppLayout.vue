<template>
  <!-- Layout de l'app connectée avec sidebar (grid 2 colonnes) -->
  <div class="page-app">
    <AppEntete />
    <AppMenuNav />
    <div class="contenu">
      <RouterView />
    </div>
  </div>
</template>

<script setup>
import AppEntete from '@/components/AppEntete.vue'
import AppMenuNav from '@/components/AppMenuNav.vue'
</script>

<style scoped>
.page-app {
  display: grid;
  grid-template-columns: var(--sidebar-w) 1fr;
  grid-template-rows: var(--header-h) 1fr;
  min-height: 100vh;
  gap: 1px;
  background: linear-gradient(135deg, rgb(207, 232, 239), var(--primary));
}

.contenu {
  grid-row: 2;
  grid-column: 2 / 3;
  background: white;
  overflow-y: auto;
  padding: 10px;
}

@media (max-width: 768px) {
  .page-app {
    grid-template-columns: 1fr;
    grid-template-rows: auto auto 1fr;
  }

  .contenu {
    grid-column: 1 / 2;
    grid-row: 3;
    padding: 8px;
  }
}
</style>
