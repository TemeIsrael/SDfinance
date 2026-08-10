<template>
  <!-- Page contacts (style contacts.html) -->
  <div class="page-contact">
    <div class="debut">
      <h1>SDfinance</h1>
    </div>
    <h2>Nous contacter – les leaders</h2>
    <p>Une question, un problème, ou un besoin d'information ? Nous sommes là pour vous aider.</p>
    <h3>Informations de contacts</h3>

    <div class="address">
      <h4>TÉLÉPHONES</h4>
      <ul class="liste-contacts">
        <li>📞 +237 6XX XXX XXX – Président</li>
        <li>📞 +237 6XX XXX XXX – Trésorier</li>
        <li>📞 +237 6XX XXX XXX – Secrétaire</li>
      </ul>
    </div>

    <div class="telephone">
      <h4>EMAILS</h4>
      <ul class="liste-contacts">
        <li>✉️ contact@sdfinance.com</li>
        <li>✉️ president@sdfinance.com</li>
        <li>✉️ tresorier@sdfinance.com</li>
      </ul>
    </div>

    <div class="retour">
      <RouterLink to="/accueil" class="btn-retour">Retour à l'accueil</RouterLink>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
.page-contact {
  min-height: 100vh;
  background: linear-gradient(135deg, white, var(--primary));
  font-family: 'Poppins', sans-serif;
  padding-bottom: 40px;
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

h2,
h3,
h4 {
  margin: 10px;
  text-align: center;
}

p {
  margin: 10px 30px;
  text-align: center;
  color: #555;
}

.address,
.telephone {
  background: white;
  width: 85%;
  margin: 14px auto;
  min-height: 120px;
  border-radius: var(--radius);
  padding: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.liste-contacts {
  list-style: none;
  padding: 8px 20px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.liste-contacts li {
  font-size: 0.95rem;
  color: #444;
}

.retour {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

.btn-retour {
  display: inline-block;
  padding: 12px 28px;
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

.btn-retour:hover {
  transform: scale(1.1);
  background: black;
}
</style>
