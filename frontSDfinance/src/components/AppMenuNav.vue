<template>
  <!-- Menu latéral de l'app connectée (style gestionTransactions.html) -->
  <nav class="menuNav">
    <ul class="menu-list">
      <li class="menu-item">
        <RouterLink to="/app/membres" class="menu-link" active-class="active">Membres</RouterLink>
      </li>
      <li class="menu-item">
        <RouterLink to="/app/evenements" class="menu-link" active-class="active">Événements</RouterLink>
      </li>
      <li class="menu-item">
        <RouterLink to="/app/transactions" class="menu-link" active-class="active">Infos caisses</RouterLink>
      </li>
      <li class="menu-item">
        <RouterLink to="/app/discussion" class="menu-link" active-class="active">Discussion</RouterLink>
      </li>
    </ul>
  </nav>
</template>

<script setup></script>

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
