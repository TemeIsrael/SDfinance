<template>
  <header class="app-header">
    <div>
      <h1>SDfinance</h1>
      <p>Gestion financière et administration des membres</p>
    </div>
    <nav>
      <router-link to="/">Accueil</router-link>
      <router-link to="/transactions">Transactions</router-link>
      <router-link to="/membres">Membres</router-link>
      <router-link to="/evenements">Événements</router-link>
      <router-link to="/contact">Contact</router-link>
    </nav>
  </header>
</template>

<style scoped>
.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.2rem 2rem;
  background: linear-gradient(135deg, #f5ac71, #f1737e);
  color: white;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
}

nav {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

nav a {
  color: white;
  text-decoration: none;
  font-weight: 600;
}

nav a.router-link-active {
  text-decoration: underline;
}
</style>
