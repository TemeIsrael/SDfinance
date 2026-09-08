<template>
  <div class="event-page">
    <!-- Editor panel – only for leaders (top) -->
    <section v-if="isLeader" class="event-editor">
      <!-- Step 1: basic info -->
      <div v-if="!showExpenseForm">
        <h3>Créer un événement</h3>
        <input v-model="newEventTitle" placeholder="Titre" />
        <input v-model="newEventDate" type="date" />
        <input v-model="newEventLocation" placeholder="Lieu" />
        <button @click="showExpenseForm = true" class="btn primary">Détails</button>
      </div>

      <!-- Step 2: add expenses before final creation -->
      <div v-else class="expense-form">
        <h4>Ajouter des dépenses</h4>
        <div class="add-expense">
          <input v-model="newExpenseDesc" placeholder="Description" />
          <input v-model.number="newExpenseAmt" type="number" placeholder="Montant" />
          <button @click="addTempExpense" class="btn primary">Ajouter</button>
        </div>
        <button @click="createNewEvent" class="btn primary" style="margin-top:12px;">Créer</button>
      </div>
    </section>

    <!-- List of past events (middle) -->
    <section class="event-list">
      <header class="filter-bar">
        <input type="number" v-model="yearFilter" placeholder="Année" class="year-input" />
      </header>
      <div class="cards">
        <div
          v-for="ev in filteredEvents"
          :key="ev.id"
          class="card"
          :class="{ active: activeEvent?.id === ev.id }"
          @click="selectEvent(ev)"
        >
          {{ ev.title }} – {{ formatDate(ev.startDate) }}
        </div>
      </div>
    </section>

<section v-if="activeEvent" class="expenses-section">
  <h4>Détails de l'événement « {{ activeEvent.title }} »</h4>
  <table class="table">
    <thead>
      <tr>
        <th>Description</th>
        <th>Montant (fcfa)</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="exp in filteredExpenses" :key="exp.id">
        <td>{{ exp.description }}</td>
        <td>{{ Number(exp.amount).toFixed(2) }}</td>
      </tr>
    </tbody>
  </table>
</section>
    <div v-else class="empty-state">Sélectionnez un événement</div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useEvent } from '@/composable/useEvent'

// Auth & role
const { currentRole } = useAuthStore()
const isLeader = computed(() => ['LEADER', 'PRESIDENT'].includes(currentRole.value)) // leaders (including presidents) can edit events
const isPresident = computed(() => currentRole.value === 'PRESIDENT')

// Composable API
const {
  events,
  activeEvent,
  expenses,
  fetchEvents,
  selectEvent,
  addExpense,
  updateExpense,
  deleteExpense,
  updateEvent,
  createEvent,
} = useEvent()

// UI state
const yearFilter = ref('')
const newExpenseDesc = ref('')
const newExpenseAmt = ref(0)

// Creation fields
const newEventTitle = ref('')
const newEventDate = ref('')
const newEventDesc = ref('')
const newEventLocation = ref('')
// flag to toggle expense sub‑form during creation
const showExpenseForm = ref(false)
// temporary list of expenses before event is persisted
const newExpenses = ref([])
const editEventLocation = ref('')

// Editing fields (filled when an event is selected)
const editEventTitle = ref('')
const editEventDate = ref('')
const editEventDesc = ref('')

// Filters
const filteredEvents = computed(() => {
  if (!yearFilter.value) return events.value
  return events.value.filter(ev => new Date(ev.startDate).getFullYear() === Number(yearFilter.value))
})

const filteredExpenses = computed(() => expenses.value)

onMounted(() => fetchEvents())

// When an event is selected, populate edit fields
watch(activeEvent, ev => {
  if (ev) {
    editEventTitle.value = ev.title || ''
    editEventDate.value = ev.startDate ? ev.startDate.slice(0, 10) : ''
    editEventDesc.value = ev.description || ''
    editEventLocation.value = ev.location || ''
  }
})

const createNewEvent = async () => {
  if (!newEventTitle.value.trim()) return
  const payload = {
    title: newEventTitle.value,
    startDate: newEventDate.value ? new Date(newEventDate.value).toISOString() : new Date().toISOString(),
    description: newEventDesc.value,
    location: newEventLocation.value,
  }
  await createEvent(payload)
  // after creation, refresh list and set the newly created event as active
  await fetchEvents()
  const created = events.value[events.value.length - 1]
  if (created) activeEvent.value = created
  // reset form
  newEventTitle.value = ''
  newEventDate.value = ''
  newEventDesc.value = ''
  newEventLocation.value = ''
}

const saveEventEdits = async () => {
  if (!activeEvent.value) return
  const payload = {
    title: editEventTitle.value,
    startDate: editEventDate.value ? new Date(editEventDate.value).toISOString() : activeEvent.value.startDate,
    description: editEventDesc.value,
    location: editEventLocation.value,
  }
  await updateEvent(activeEvent.value.id, payload)
}

const formatDate = dateStr => (dateStr ? new Date(dateStr).toLocaleDateString() : '')

const createExpense = async () => {
  if (!newExpenseDesc.value.trim()) return
  await addExpense({
    description: newExpenseDesc.value,
    amount: Number(newExpenseAmt.value),
    date: new Date().toISOString(),
  })
  newExpenseDesc.value = ''
  newExpenseAmt.value = 0
}

const saveExpense = async exp => {
  await updateExpense(exp.id, {
    description: exp.description,
    amount: Number(exp.amount),
    date: exp.date,
  })
}

const removeExpense = async id => await deleteExpense(id)
</script>

<style scoped>
.event-page {
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding: 16px;
}
.event-list,
.event-editor,
.expenses-section {
  background: #fff;
  border-radius: var(--radius);
  border: 1px solid #ddd;
  padding: 16px;
}
.cards {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 12px;
}
.card {
  padding: 8px 12px;
  cursor: pointer;
  transition: background 0.2s;
}
.card.active {
  background: var(--primary);
  color: #fff;
}
.filter-bar {
  display: flex;
  justify-content: flex-end;
}
.year-input {
  width: 80px;
  padding: 4px;
}
.table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 12px;
}
.table th,
.table td {
  border: 1px solid #eee;
  padding: 6px;
}
.add-expense {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}
.btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
}
.btn.primary {
  background: var(--primary-dark);
  color: #fff;
  transition: all 0.3s ease
}
.btn.primary:hover{
  background-color:black;
}
.btn.danger {
  background: #d9534f;
  color: #fff;
}
.empty-state {
  grid-column: 1 / -1;
  text-align: center;
  color: #666;
  padding: 40px 0;
}
</style>
