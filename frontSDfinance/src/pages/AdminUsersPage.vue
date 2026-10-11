<template>
  <div class="admin-page">
    <h2>Gestion des Utilisateurs</h2>
    <p>Cette page est réservée à l'Administrateur pour gérer les accès et le côté technique de l'application.</p>

    <div class="card">
      <h3>Liste des Utilisateurs</h3>
      <table class="table">
        <thead>
          <tr>
            <th>Photo</th>
            <th>Nom d'utilisateur</th>
            <th>Rôle</th>
            <th>Groupes</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td class="avatar-cell">
              <img v-if="user.photoUrl" :src="user.photoUrl" alt="Avatar" class="avatar-img" />
              <div v-else class="avatar-placeholder">
                {{ user.username ? user.username.charAt(0).toUpperCase() : '👤' }}
              </div>
            </td>
            <td>{{ user.username }}</td>
            <td>{{ user.role }}</td>
            <td>{{ user.groupeIds ? user.groupeIds.join(', ') : 'N/A' }}</td>
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

const users = ref([])

onMounted(async () => {
  // Chargement réel des utilisateurs via /api si l'endpoint est disponible
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
.avatar-cell {
  width: 46px;
  text-align: center;
}
.avatar-img {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  object-fit: cover;
}
.avatar-placeholder {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  background: var(--primary);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 0.85rem;
}
</style>
