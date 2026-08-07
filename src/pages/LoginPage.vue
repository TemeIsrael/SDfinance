<template>
  <!-- Page de connexion (style connexion.html) -->
  <div class="page-auth">
    <header>
      <h1>SDfinance</h1>
      <p>la gestion de nos finances</p>
    </header>

    <form class="card" @submit.prevent="seConnecter">
      <h2>Connexion</h2>

      <label for="nom">Nom</label>
      <input type="text" id="nom" v-model="form.nom" placeholder="Votre nom" />

      <label for="prenom">Prénom</label>
      <input type="text" id="prenom" v-model="form.prenom" placeholder="Votre prénom" />

      <label for="password">Mot de passe</label>
      <input type="password" id="password" v-model="form.password" placeholder="••••••••" />

      <label for="groupe">Groupe</label>
      <select id="groupe" name="groupe" v-model="form.groupe">
        <option value="">-- Sélectionnez un groupe --</option>
        <option value="groupe_jeunes">Jeune</option>
        <option value="groupe_femmes">Femme</option>
        <option value="groupe_hommes">Homme</option>
        <option value="groupe_leaders">Leader</option>
      </select>

      <button type="submit">Se connecter</button>

      <p class="lien-inscription">
        Pas encore inscrit ?
        <RouterLink to="/inscription">S'inscrire</RouterLink>
      </p>
    </form>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = reactive({
  nom: '',
  prenom: '',
  password: '',
  groupe: '',
})

const seConnecter = () => {
  router.replace({ name: 'membres' })
}
</script>

<style scoped>
.page-auth {
  min-height: 100vh;
  background: linear-gradient(135deg, white, var(--primary));
  font-family: 'Poppins', sans-serif;
}

header {
  text-align: center;
  padding: 20px 0 0;
}

header h1 {
  color: var(--primary-dark);
  font-size: 2rem;
}

header p {
  color: #666;
}

.card {
  max-width: 380px;
  margin: 30px auto;
  padding: 30px;
  background: white;
  border-radius: var(--radius);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
  display: flex;
  flex-direction: column;
}

.card h2 {
  text-align: center;
  margin-bottom: 16px;
  color: var(--primary-dark);
}

label {
  display: block;
  margin-top: 12px;
  font-weight: 600;
  color: black;
}

input,
select {
  width: 100%;
  padding: 10px;
  margin-top: 6px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-family: 'Poppins', sans-serif;
}

button {
  width: 100%;
  display: block;
  margin-top: 20px;
  padding: 12px;
  background: var(--primary);
  color: white;
  border: none;
  border-radius: var(--radius);
  cursor: pointer;
  transition: all 0.5s;
  font-family: 'Poppins', sans-serif;
  font-size: 1rem;
  font-weight: 600;
}

button:hover {
  background: var(--primary-dark);
  transform: scale(1.03);
}

.lien-inscription {
  text-align: center;
  margin-top: 14px;
  font-size: 0.9rem;
}

.lien-inscription a {
  color: var(--primary-dark);
  font-weight: 600;
  text-decoration: none;
}

.lien-inscription a:hover {
  text-decoration: underline;
}
</style>
