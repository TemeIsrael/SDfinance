<template>
  <!-- Page d'inscription (style insciptionPage.html) -->
  <div class="page-auth">
    <header>
      <h1>SDfinance</h1>
      <p>la gestion de nos finances</p>
    </header>

    <form class="card" @submit.prevent="sInscrire">
      <div>
        <h2>Inscription</h2>
      </div>
      <div class="container">
        <div class="nom">
          <label for="nom">Nom</label>
          <input type="text" id="nom" v-model="form.nom" />
        </div>
        <div class="prenom">
          <label for="prenom">Prénom</label>
          <input type="text" id="prenom" v-model="form.prenom" />
        </div>
        <div class="mail">
          <label for="email">Email</label>
          <input type="email" id="email" v-model="form.email" />
        </div>
        <div class="password">
          <label for="password">Mot de passe</label>
          <input type="password" id="password" v-model="form.password" />
        </div>
        <div class="telephone">
          <label for="telephone">Tél</label>
          <input type="text" id="telephone" v-model="form.telephone" />
        </div>
        <div class="groupe">
          <label for="groupe">Groupe</label>
          <select id="groupe" name="groupe" v-model="form.groupe">
            <option value="groupe_jeunes">Jeune</option>
            <option value="groupe_femmes">Femme</option>
            <option value="groupe_hommes">Homme</option>
            <option value="groupe_hommes">Leader</option>
          </select>
        </div>
        <div class="date-adhesion">
          <label for="dateAdhesion">Date d'adhésion</label>
          <input type="date" id="dateAdhesion" v-model="form.dateAdhesion" />
        </div>
        <div class="sexe">
          <label for="sexe">Sexe</label>
          <input type="text" id="sexe" v-model="form.sexe" placeholder="H/F" />
        </div>
        <div class="btn">
          <button type="submit">S'inscrire</button>
        </div>
      </div>

      <p class="lien-connexion">
        Déjà inscrit ?
        <RouterLink to="/connexion">Se connecter</RouterLink>
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
  email: '',
  password: '',
  telephone: '',
  groupe: 'groupe_jeunes',
  dateAdhesion: '',
  sexe: '',
})

const sInscrire = () => {
  router.push('/connexion')
}
</script>

<style scoped>
.page-auth {
  min-height: 100vh;
  background: linear-gradient(135deg, white, var(--primary));
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
  max-width: 460px;
  margin: 20px auto 40px;
  padding: 30px;
  background: white;
  border-radius: var(--radius);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
}

.card h2 {
  text-align: center;
  margin-bottom: 16px;
  color: var(--primary-dark);
}

.container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 6px;
}

label {
  display: block;
  margin-top: 10px;
  font-weight: 600;
  color: black;
  font-size: 0.9rem;
}

input,
select {
  width: 100%;
  padding: 8px;
  margin-top: 4px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-family: 'Poppins', sans-serif;
}

.mail,
.password,
.date-adhesion {
  grid-column: 1 / 3;
}

.btn {
  grid-column: 1 / 3;
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

button {
  width: 60%;
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

.lien-connexion {
  text-align: center;
  margin-top: 14px;
  font-size: 0.9rem;
}

.lien-connexion a {
  color: var(--primary-dark);
  font-weight: 600;
  text-decoration: none;
}

.lien-connexion a:hover {
  text-decoration: underline;
}
</style>
