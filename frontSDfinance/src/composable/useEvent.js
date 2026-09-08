import { ref, computed } from 'vue'
import { useAuthStore } from '@/stores/auth'

export const useEvent = () => {
  const { currentUser, state } = useAuthStore()
  const token = state.token

  const events = ref([])          // [{id, title, startDate, ...}]
  const activeEvent = ref(null)
  const expenses = ref([])       // [{id, description, amount, date}]
  const yearFilter = ref('')
  const expenseYearFilter = ref('')

  const isLeader = computed(() => currentUser?.role === 'LEADER')
  const isPresident = computed(() => currentUser?.role === 'PRESIDENT')

  const fetchEvents = async () => {
    const res = await fetch('/api/events', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.ok) {
      events.value = await res.json()
    }
    // No mock fallback – rely entirely on real backend data
  }

  const selectEvent = async (ev) => {
    activeEvent.value = ev
    await fetchExpenses(ev.id)
  }

  const fetchExpenses = async (eventId) => {
    const res = await fetch(`/api/events/${eventId}/expenses`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.ok) expenses.value = await res.json()
  }

  // ----- CRUD for expenses (leaders only) -----
  const addExpense = async (payload) => {
    if (!isLeader.value) return
    const res = await fetch(`/api/events/${activeEvent.value.id}/expenses`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify(payload)
    })
    if (res.ok) expenses.value.push(await res.json())
  }

  const updateExpense = async (expId, payload) => {
    if (!isLeader.value) return
    const res = await fetch(`/api/events/${activeEvent.value.id}/expenses/${expId}`,
      {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(payload)
      })
    if (res.ok) {
      const idx = expenses.value.findIndex(e => e.id === expId)
      if (idx >= 0) expenses.value[idx] = await res.json()
    }
  }

  const deleteExpense = async (expId) => {
    if (!isLeader.value) return
    await fetch(`/api/events/${activeEvent.value.id}/expenses/${expId}`,
      { method: 'DELETE', headers: { Authorization: `Bearer ${token}` } })
    expenses.value = expenses.value.filter(e => e.id !== expId)
  }

  // ----- Update event (leaders allowed) -----
  const updateEvent = async (eventId, payload) => {
    if (!isLeader.value && currentUser?.role !== 'PRESIDENT') return
    const res = await fetch(`/api/events/${eventId}`,
      {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(payload)
      })
    if (res.ok) {
      const updated = await res.json()
      const idx = events.value.findIndex(e => e.id === eventId)
      if (idx >= 0) events.value[idx] = updated
    }
  }

  const createEvent = async (payload) => {
    if (!isLeader.value) return
    const res = await fetch('/api/events', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify(payload)
    })
    if (res.ok) {
      const created = await res.json()
      events.value.push(created)
    }
  }

  return {
    events,
    activeEvent,
    expenses,
    yearFilter,
    expenseYearFilter,
    fetchEvents,
    selectEvent,
    addExpense,
    updateExpense,
    deleteExpense,
    updateEvent,
    createEvent,
    isLeader,
    isPresident
  }
}
