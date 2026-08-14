<template>
  <div class="admin-page">
    <h2>Gestion des Utilisateurs</h2>
    <p>Cette page est réservée à l'Administrateur pour gérer les accès et le côté technique de l'application.</p>

    <div class="card">
      <h3>Liste des Utilisateurs</h3>
      <table class="table">
        <thead>
          <tr>
            <th>Nom d'utilisateur</th>
            <th>Rôle</th>
            <th>Groupes</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.username }}</td>
            <td>{{ user.role }}</td>
            <td>{{ user.groupeIds.join(', ') || 'N/A' }}</td>
            <td>
              <button class="btn btn-edit">Modifier</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const users = ref([
  { id: 1, username: 'admin_sys', role: 'ADMIN', groupeIds: [] },
  { id: 2, username: 'president_jeunes', role: 'PRESIDENT', groupeIds: [1] },
  { id: 3, username: 'tresorier_jeunes', role: 'TRESORIER_CAISSIER', groupeIds: [1] },
  { id: 4, username: 'leader_global', role: 'LEADER', groupeIds: [] },
])

onMounted(async () => {
  // Ici on appellerait /api/users si disponible pour charger la liste
})
</script>

<style scoped>
.admin-page {
  padding: 20px;
}
.card {
  background: white;
  padding: 20px;
  border-radius: var(--radius);
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  margin-top: 20px;
  overflow-x: auto;
}
.table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
}
th, td {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: left;
}
th {
  background-color: var(--primary);
  color: white;
}
.btn {
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.btn-edit {
  background-color: var(--primary-dark);
  color: white;
}
</style>
