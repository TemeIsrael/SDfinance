<template>
  <div class="page-auth">
    <header>
      <h1>SDfinance</h1>
      <p>la gestion de nos finances</p>
    </header>

    <form class="card" @submit.prevent="seConnecter">
      <h2>Connexion</h2>

      <label for="username">Nom d'utilisateur</label>
      <input type="text" id="username" v-model="form.username" placeholder="prenom nom" required />

      <label for="password">Mot de passe</label>
      <div style="position: relative;">
        <input :type="showPassword ? 'text' : 'password'" id="password" v-model="form.password" placeholder="••••••••" required style="width: 100%; box-sizing: border-box;" />
        <span @click="showPassword = !showPassword" style="position: absolute; right: 10px; top: 50%; transform: translateY(-50%); cursor: pointer; font-size: 1.2em;">
          {{ showPassword ? '👁️' : '🙈' }}
        </span>
      </div>

      <label for="groupe">Groupe</label>
      <select id="groupe" v-model="form.groupe">
        <option value="1">Jeune</option>
        <option value="2">Femme</option>
        <option value="3">Homme</option>
        <option value="4">Aucun</option>
      </select>

      <button type="submit">Se connecter</button>

      <p class="error-msg" v-if="error">{{ error }}</p>

      <p class="lien-inscription">
        Pas encore inscrit ?
        <RouterLink to="/inscription">S'inscrire</RouterLink>
      </p>
    </form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const route = useRoute()
const { login } = useAuthStore()

const form = reactive({
  username: route.query.username || '',
  password: '',
  groupe: '',
})

const error = ref('')
const showPassword = ref(false)

const seConnecter = async () => {
  error.value = ''
    try {
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: form.username, password: form.password, groupe: form.groupe })
      })

      if (response.ok) {
        const data = await response.json()
        if (form.groupe && form.groupe !== '4' && data.groupeIds && data.groupeIds.length > 0) {
          const selectedGid = Number(form.groupe)
          if (!data.groupeIds.includes(selectedGid)) {
            error.value = "Ce compte n'appartient pas au groupe sélectionné. Veuillez choisir votre groupe d'origine."
            return
          }
        }
        login({ username: data.username, role: data.role, groupeIds: data.groupeIds }, data.token)
        // Redirection selon le rôle réel retourné
        if (data.role === 'ADMIN') {
          router.replace({ name: 'admin-users' })
        } else if (data.role === 'MEMBRE' || data.role === 'TRESORIER_CAISSIER') {
          router.replace({ name: 'transactions' })
        } else {
          router.replace({ name: 'membres' })
        }
      } else {
        const errData = await response.json().catch(() => ({}))
        const msg = errData.message || 'Échec de la connexion'
        error.value = msg
      }
    } catch (err) {
      console.error('Erreur lors de la connexion', err)
      error.value = 'Impossible de contacter le serveur d\'authentification.'
    }
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

.error-msg {
  color: red;
  text-align: center;
  margin-top: 10px;
  font-size: 0.9rem;
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
