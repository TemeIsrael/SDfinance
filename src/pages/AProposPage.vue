<template>
  <!-- Page À propos (style a_propos.html) -->
  <div class="page-apropos">
    <div class="debut">
      <h1>SDfinance</h1>
    </div>
    <form class="card">
      <h3>À propos de</h3>
      <h2>SDfinance</h2>
      <p>
        Conçue pour répondre au besoin de simplification de la gestion des finances au sein de la
        Semence Divine, SDfinance est la solution de gestion financière proposée par le groupe des
        jeunes de ladite assemblée.
      </p>
      <p>
        Notre plateforme gère de manière simple et transparente les opérations financières au sein
        de la Semence Divine.
      </p>
      <div class="retour">
        <RouterLink to="/accueil" class="btn">Retour à l'accueil</RouterLink>
      </div>
    </form>
  </div>
</template>

<script setup></script>

<style scoped>
.page-apropos {
  min-height: 100vh;
  background: linear-gradient(135deg, white, var(--primary));
  font-family: 'Poppins', sans-serif;
}

.debut {
  background: white;
  margin: 0 auto;
  padding: 8px 20px;
}

h1 {
  margin: 0;
  color: var(--primary-dark);
}

.card {
  max-width: 400px;
  margin: 40px auto;
  padding: 30px;
  background: white;
  border-radius: var(--radius);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
}

h3,
h2 {
  margin: 0 0 8px;
  text-align: center;
}

h3 {
  font-size: 1rem;
  color: #555;
}

h2 {
  color: var(--primary-dark);
  font-size: 1.6rem;
  margin-bottom: 14px;
}

p {
  margin: 12px 0;
  color: #444;
  line-height: 1.6;
}

.retour {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.btn {
  display: inline-block;
  width: 60%;
  text-align: center;
  padding: 12px;
  background: var(--primary);
  color: white;
  border: none;
  border-radius: var(--radius);
  cursor: pointer;
  transition: all 0.5s;
  font-family: 'Poppins', sans-serif;
  text-decoration: none;
  font-weight: 600;
}

.btn:hover {
  transform: scale(1.1);
  background: black;
}
</style>
