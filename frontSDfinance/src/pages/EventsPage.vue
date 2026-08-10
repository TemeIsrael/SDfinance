<script setup lang="ts">
const events = [
  { title: 'Réunion générale', date: '12/08/2026', place: 'Salle A' },
  { title: 'Audit financier', date: '18/08/2026', place: 'Bureau central' },
  { title: 'Formation', date: '02/09/2026', place: 'En ligne' },
]
</script>

<template>
  <section class="page">
    <h2>Événements</h2>
    <div class="cards">
      <article v-for="event in events" :key="event.title" class="card">
        <h3>{{ event.title }}</h3>
        <p>{{ event.date }}</p>
        <p>{{ event.place }}</p>
      </article>
    </div>
  </section>
</template>

<style scoped>
.cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 1rem;
}
.card {
  background: white;
  padding: 1rem;
  border-radius: 10px;
  box-shadow: 0 6px 18px rgba(0,0,0,0.08);
}
</style>
