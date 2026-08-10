<template>
  <!-- Page Événements -->
  <section class="page-evenements">
    <h3>Événements à venir</h3>
    <div class="events-list">
      <div
        v-for="event in evenements"
        :key="event.id"
        class="event-card"
      >
        <div class="event-date">{{ event.date }}</div>
        <div class="event-info">
          <h4>{{ event.titre }}</h4>
          <p>{{ event.lieu }}</p>
        </div>
      </div>
    </div>

    <!-- Formulaire ajout événement -->
    <div class="ajout-event">
      <h4>Ajouter un événement</h4>
      <form @submit.prevent="ajouterEvenement">
        <input type="text" v-model="newEvent.titre" placeholder="Titre de l'événement" />
        <input type="date" v-model="newEvent.date" />
        <input type="text" v-model="newEvent.lieu" placeholder="Lieu" />
        <div class="pourAjout">
          <button class="btn" type="submit">Ajouter</button>
        </div>
      </form>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'

const evenements = ref([
  { id: 1, titre: 'Réunion générale', date: '12/08/2026', lieu: 'Salle principale' },
  { id: 2, titre: 'Audit financier', date: '18/08/2026', lieu: 'Bureau central' },
  { id: 3, titre: 'Formation membres', date: '02/09/2026', lieu: 'En ligne' },
  { id: 4, titre: 'Collecte offrandes', date: '06/09/2026', lieu: 'Église' },
])

const newEvent = ref({ titre: '', date: '', lieu: '' })

const ajouterEvenement = () => {
  if (!newEvent.value.titre || !newEvent.value.date) return
  evenements.value.push({ ...newEvent.value, id: Date.now() })
  newEvent.value = { titre: '', date: '', lieu: '' }
}
</script>

<style scoped>
.page-evenements {
  padding: 14px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-evenements h3 {
  font-size: 1.2rem;
  color: var(--primary-dark);
  text-decoration: underline;
}

.events-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.event-card {
  background: white;
  border-left: 5px solid var(--primary);
  border-radius: var(--radius);
  padding: 12px 16px;
  display: flex;
  gap: 16px;
  align-items: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s;
}

.event-card:hover {
  transform: translateX(4px);
  border-left-color: var(--primary-dark);
}

.event-date {
  min-width: 90px;
  font-weight: 700;
  color: var(--primary-dark);
  font-size: 0.9rem;
}

.event-info h4 {
  margin-bottom: 4px;
  color: #333;
}

.event-info p {
  font-size: 0.85rem;
  color: #777;
}

.ajout-event {
  background: var(--primary);
  border-radius: var(--radius);
  padding: 14px;
  max-width: 400px;
}

.ajout-event h4 {
  margin-bottom: 10px;
  color: white;
  text-align: center;
}

input {
  width: 100%;
  padding: 8px;
  margin: 4px 0;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-family: 'Poppins', sans-serif;
}

.pourAjout {
  display: flex;
  justify-content: center;
  margin-top: 8px;
}

.btn {
  border: none;
  padding: 8px 20px;
  color: aliceblue;
  background-color: var(--primary-dark);
  cursor: pointer;
  border-radius: var(--radius);
  transition: all 0.5s;
  font-family: 'Poppins', sans-serif;
}

.btn:hover {
  background-color: black;
}
</style>
