<template>
  <div class="page-auth">
    <header>
      <h1>SDfinance</h1>
      <p>la gestion de nos finances</p>
    </header>

    <form class="card" @submit.prevent="seConnecter">
      <h2>Connexion</h2>

      <label for="username">Nom d'utilisateur</label>
      <input type="text" id="username" v-model="form.username" placeholder="admin, president, tresorier..." required />

      <label for="password">Mot de passe</label>
      <input type="password" id="password" v-model="form.password" placeholder="••••••••" required />

      <label for="groupe">Groupe (Simulation ou réel)</label>
      <select id="groupe" v-model="form.groupe">
        <option value="">-- Sélectionnez un groupe --</option>
        <option value="1">Jeune</option>
        <option value="2">Femme</option>
        <option value="3">Homme</option>
      </select>

      <!-- Sélecteur de rôle pour simuler la connexion si le backend n'est pas lancé -->
      <label for="mockRole">Rôle</label>
      <select id="mockRole" v-model="form.mockRole">
        <option value="MEMBRE">Simple Membre</option>
        <option value="PRESIDENT">Président</option>
        <option value="TRESORIER_CAISSIER">Trésorier/Caissier</option>
        <option value="LEADER">Leader</option>
        <option value="ADMIN">Admin</option>
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
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const { login } = useAuthStore()

const form = reactive({
  username: '',
  password: '',
  groupe: '',
  mockRole: 'MEMBRE',
})

const error = ref('')

const seConnecter = async () => {
  error.value = ''
  try {
    // Tenter une vraie connexion si le backend est disponible
    const response = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: form.username, password: form.password, groupe: form.groupe })
    })

    if (response.ok) {
      const data = await response.json()
      login({ username: data.username, role: data.role, groupeIds: data.groupeIds }, data.token)
    } else {
      throw new Error("Echec connexion")
    }
  } catch (err) {
    console.warn("Backend indisponible, utilisation des données simulées", err)
    
    // Simulation pour pouvoir tester l'UI sans backend
    const mockGroupeIds = form.mockRole === 'LEADER' || form.mockRole === 'ADMIN' ? [] : (form.groupe ? [form.groupe] : [1])
    
    login({ 
      username: form.username || 'TestUser', 
      role: form.mockRole, 
      groupeIds: mockGroupeIds 
    }, 'fake-jwt-token')
  }

  // Redirection basée sur le rôle
  if (form.mockRole === 'ADMIN') {
    router.replace({ name: 'admin-users' })
  } else if (form.mockRole === 'MEMBRE' || form.mockRole === 'TRESORIER_CAISSIER') {
    router.replace({ name: 'transactions' })
  } else {
    // PRESIDENT ou LEADER
    router.replace({ name: 'membres' })
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
