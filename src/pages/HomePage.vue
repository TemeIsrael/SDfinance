<script setup>
import backgroundImage from '@/images/arrièrePlan.jpeg'
</script>

<template>
  <!-- Page d'accueil / Hero (style loadingPage.html) -->
  <div class="page-accueil">
    <!-- Nav publique -->
    <header>
      <nav>
        <div class="logo"><h3>SDfinance</h3></div>
        <ul class="menu-list">
          <li class="menu-item">
            <RouterLink to="/a-propos" class="menu-link">À propos</RouterLink>
          </li>
          <li class="menu-item">
            <RouterLink to="/contact" class="menu-link">Contact</RouterLink>
          </li>
        </ul>
      </nav>
    </header>

    <!-- Section hero -->
    <section
      class="tbBody"
      :style="{
        backgroundImage: `linear-gradient(rgba(254, 252, 252, 0.85), rgba(245, 238, 238, 0.75)), url(${backgroundImage})`,
      }"
    >
      <div class="hero-content">
        <h1>Gestion financière</h1>
        <p>Gestion simple et transparente de nos opérations financières</p>
        <div class="hero-actions">
          <RouterLink :to="{ name: 'connexion' }" class="btn-hero">Se connecter</RouterLink>
          <RouterLink :to="{ name: 'inscription' }" class="btn-hero">S'inscrire</RouterLink>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.page-accueil {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* ── nav ── */
header {
  width: 100%;
}

nav {
  background-color: var(--primary);
  box-shadow: 0 5px 5px rgb(249, 248, 248);
  position: sticky;
  top: 0;
  z-index: 100000;
  min-height: 50px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 50px;
}

.logo {
  color: white;
  flex-shrink: 0;
}

.logo h3 {
  margin: 0;
  font-size: 24px;
}

.menu-list {
  display: flex;
  gap: 10px;
  max-width: 1200px;
  margin: 0 auto;
  list-style: none;
  justify-content: center;
  align-items: center;
  padding: 8px 0;
  transition: all 0.5s;
}

.menu-item {
  position: relative;
}

.menu-link {
  text-decoration: none;
  display: block;
  transition: all 0.3s;
  color: white;
  padding: 6px 14px;
}

.menu-link::after {
  width: 0;
  content: '';
  position: absolute;
  bottom: 0;
  height: 3px;
  background-color: white;
  left: 50%;
  transform: translate(-50%);
  transition: all 0.5s ease;
}

.menu-item:hover .menu-link::after {
  width: 100%;
}

/* ── hero ── */
.tbBody {
  flex: 1;
  background-size: contain;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
}

.hero-content {
  text-align: center;
  color: var(--primary-dark);
}

.hero-content h1 {
  font-size: 52px;
  color: var(--primary-dark);
  margin-bottom: 16px;
}

.hero-content p {
  font-size: 20px;
  margin-bottom: 32px;
  color: #555;
}

.hero-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  flex-wrap: wrap;
}

.btn-hero {
  display: inline-block;
  padding: 12px 30px;
  background-color: var(--primary);
  color: white;
  text-decoration: none;
  border-radius: var(--radius);
  font-weight: 600;
  transition: all 0.3s;
}

.btn-hero:hover {
  background-color: var(--primary-dark);
  transform: scale(1.05);
}
</style>
