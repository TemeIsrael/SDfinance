<template>
  <!-- Page 404 Not Found -->
  <div class="not-found">
    <h1>404</h1>
    <h2>Page introuvable</h2>
    <p>La page que vous cherchez n'existe pas.</p>
    <RouterLink to="/accueil" class="btn-retour">Retour à l'accueil</RouterLink>
  </div>
</template>

<script setup></script>

<style scoped>
.not-found {
  min-height: 100vh;
  background: linear-gradient(135deg, white, var(--primary));
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 12px;
  text-align: center;
  padding: 20px;
}

h1 {
  font-size: 96px;
  color: var(--primary-dark);
  margin: 0;
}

h2 {
  font-size: 1.8rem;
  color: #444;
}

p {
  color: #666;
}

.btn-retour {
  display: inline-block;
  margin-top: 20px;
  padding: 12px 28px;
  background: var(--primary);
  color: white;
  border-radius: var(--radius);
  text-decoration: none;
  font-weight: 600;
  transition: all 0.4s;
}

.btn-retour:hover {
  background: var(--primary-dark);
  transform: scale(1.05);
}
</style>
