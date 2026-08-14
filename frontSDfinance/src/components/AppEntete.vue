<template>
  <div class="entet">
    <div class="logo"><h3>SDfinance</h3></div>
    <div>Bienvenue, {{ currentUser?.username || 'Utilisateur' }}</div>
    <div class="role-group-container">
      <h3>{{ formatRole(currentRole) }}</h3>
      
      <select 
        v-if="showGroupSelector" 
        v-model="localActiveGroupeId" 
        @change="updateActiveGroup"
        class="group-selector"
      >
        <option v-for="gid in userGroups" :key="gid" :value="gid">
          Groupe {{ gid }}
        </option>
      </select>
    </div>
    
    <div>
      <button class="btn" id="deconnexion" @click="seDeconnecter">Déconnexion</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { requiresGroupContext } from '@/utils/permissions'

const router = useRouter()
const { currentUser, currentRole, activeGroupeId, userGroups, setActiveGroupeId, logout } = useAuthStore()

const localActiveGroupeId = ref(activeGroupeId.value)

// Synchroniser la ref locale si le store change (ex: connexion)
watch(activeGroupeId, (newVal) => {
  localActiveGroupeId.value = newVal
})

const showGroupSelector = computed(() => {
  return requiresGroupContext(currentRole.value) && userGroups.value.length > 1
})

const updateActiveGroup = () => {
  setActiveGroupeId(localActiveGroupeId.value)
  // Recharger la page ou relancer les requêtes pour mettre à jour la vue avec le nouveau groupe
  window.location.reload()
}

const formatRole = (role) => {
  if (!role) return ''
  return role.replace('_', ' ')
}

const seDeconnecter = () => {
  logout()
  router.push('/connexion')
}
</script>

<style scoped>
.entet {
  grid-row: 1;
  grid-column: 1 / 3;
  background-color: var(--primary);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 50px;
  height: var(--header-h);
}

.logo {
  color: white;
  padding-top: 0;
}

.role-group-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.role-group-container h3 {
  margin: 0;
  font-size: 1rem;
}

.group-selector {
  padding: 2px 5px;
  border-radius: 4px;
  font-size: 0.8rem;
  border: none;
}

.btn {
  width: 100%;
  border: none;
  padding: 6px 10px;
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
