<template>
  <!-- Menu latéral de l'app connectée -->
  <nav class="menuNav">
    <ul class="menu-list">
      <li class="menu-item" v-if="canViewMembers">
        <RouterLink to="/membres" class="menu-link" active-class="active">Membres</RouterLink>
      </li>
      <li class="menu-item" v-if="canViewEvents">
        <RouterLink to="/evenements" class="menu-link" active-class="active">Événements</RouterLink>
      </li>
      <li class="menu-item" v-if="canViewFinance">
        <RouterLink to="/transactions" class="menu-link" active-class="active">Infos caisses</RouterLink>
      </li>
      <li class="menu-item" v-if="canUseMessaging">
        <RouterLink to="/discussion" class="menu-link" active-class="active">Discussion</RouterLink>
      </li>
      <li class="menu-item" v-if="canManageUsers">
        <RouterLink to="/admin/utilisateurs" class="menu-link" active-class="active">Utilisateurs</RouterLink>
      </li>
    </ul>
  </nav>
</template>

<script setup>
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { hasPermission } from '@/utils/permissions'

const { currentRole } = useAuthStore()

const canViewMembers = computed(() => hasPermission(currentRole.value, 'members', 'canView'))
const canViewEvents = computed(() => hasPermission(currentRole.value, 'events', 'canView'))
const canViewFinance = computed(() => hasPermission(currentRole.value, 'finance', 'canView') || hasPermission(currentRole.value, 'finance', 'canViewPersonal'))
const canUseMessaging = computed(() => hasPermission(currentRole.value, 'messaging', 'canUse'))
const canManageUsers = computed(() => hasPermission(currentRole.value, 'users', 'canManage'))
</script>

<style scoped>
.menuNav {
  background: rgb(207, 232, 239);
  grid-row: 2;
  grid-column: 1;
}

.menu-list {
  display: flex;
  gap: 15px;
  flex-direction: column;
  list-style: none;
  align-items: flex-start;
  transition: all 0.5s;
  padding: 20px;
}

.menu-item {
  margin: 0;
  position: relative;
}

.menu-link {
  margin: 0 auto;
  text-decoration: none;
  display: block;
  transition: all 0.3s;
  color: var(--primary-dark);
}

.menu-link::after {
  width: 0;
  content: '';
  position: absolute;
  bottom: 0;
  height: 3px;
  background-color: var(--primary-dark);
  left: 50%;
  transform: translate(-50%);
  transition: all 0.5s ease;
}

.menu-link.active::after {
  width: 100%;
}

.menu-item:hover .menu-link::after {
  width: 100%;
}

.menu-link.active {
  font-weight: 700;
  color: var(--primary-dark);
}
</style>
