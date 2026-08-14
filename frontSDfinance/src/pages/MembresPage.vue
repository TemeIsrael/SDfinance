<template>
  <main class="container">
    <div>
      <h2>Liste des membres</h2>
    </div>

    <!-- Filtre par groupe : visible uniquement pour le LEADER (vue globale) -->
    <div class="menu" v-if="currentRole === 'LEADER'">
      <label class="field-label" for="filtre-groupe">
        Filtrer par groupe
        <select id="filtre-groupe" v-model="membreFiltre">
          <option v-for="option in groupeOptions" :key="option.value" :value="option.value">
            {{ option.label }}
          </option>
        </select>
      </label>
    </div>

    <table class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Nom</th>
          <th>Groupe</th>
          <th>Date</th>
        </tr>
      </thead>
      <tbody id="tbBody">
        <tr v-if="isLoading">
          <td colspan="4">Chargement…</td>
        </tr>
        <tr v-for="m in membresFiltres" :key="m.id">
          <td>{{ m.id }}</td>
          <td>{{ m.nom }}</td>
          <td>{{ getGroupeLabel(m.groupe) }}</td>
          <td>{{ m.date }}</td>
        </tr>
      </tbody>
      <tfoot>
        <tr>
          <td colspan="3">Nombre total</td>
          <td>{{ totalMembres }}</td>
        </tr>
      </tfoot>
    </table>

    <!-- Formulaire LEADER : ajouter uniquement des enfants -->
    <form v-if="currentRole === 'LEADER'" id="form-membre" @submit.prevent="ajouterEnfant">
      <p class="form-hint">Ajouter un enfant</p>
      <input
        type="text"
        id="nomEnfant"
        v-model="newEnfant.nom"
        placeholder="Nom et Prénom de l'enfant"
      />
      <div class="pourAjout">
        <button class="btn" type="submit">Ajouter</button>
      </div>
    </form>

    <!-- Formulaire PRESIDENT : ajouter un membre de son groupe -->
    <form v-else-if="currentRole === 'PRESIDENT'" id="form-membre" @submit.prevent="ajouterMembreCaisse">
      <p class="form-hint">Ajouter un membre</p>
      <input
        type="text"
        id="nomEtPrenom"
        v-model="newMembreCaisse.nom"
        placeholder="Nom et Prénom"
      />
      <div class="pourAjout">
        <button class="btn" id="soumission" type="submit">Ajouter</button>
      </div>
    </form>
  </main>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useGetData } from '@/composable/useGetData.js'
import { useFinance } from '@/composable/useFinance.js'
import { getGroupeLabel, membreGroupOptions } from '@/data/financeData.js'
import { useAuthStore } from '@/stores/auth'

const { isLoading, getData } = useGetData()
const { membreFiltre, newMembreCaisse, ajouterMembreCaisse, membresCaisse, membresFiltres, totalMembres } =
  useFinance()

const { currentRole } = useAuthStore()

const groupeOptions = computed(() => membreGroupOptions)

// Formulaire dédié aux enfants (groupe LEADER uniquement)
const newEnfant = ref({ nom: '' })
const ajouterEnfant = () => {
  if (!newEnfant.value.nom) return
  membresCaisse.value.push({
    id: membresCaisse.value.length + 1,
    nom: newEnfant.value.nom,
    groupe: 'enfants',
    date: new Date().toLocaleDateString('fr-FR'),
  })
  newEnfant.value = { nom: '' }
}

onMounted(() => getData('membres'))
</script>

<style scoped>
.form-hint {
  color: white;
  font-weight: 600;
  margin-bottom: 6px;
}

.container {
  margin-top: 10px;
  padding: 14px;
  background: var(--primary);
  width: 100%;
  max-width: 680px;
  margin-left: auto;
  margin-right: auto;
  border-radius: var(--radius);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
}

.container h2 {
  text-align: center;
  margin-bottom: 10px;
  color: white;
}

.pourAjout {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.menu {
  display: flex;
  gap: 6px;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

.table {
  width: 100%;
  background: white;
  margin: 6px 0;
  border: 1px solid #ccc;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid black;
  padding: 6px;
  text-align: center;
  font-size: 0.85rem;
}

th {
  background-color: rgb(148, 74, 74);
  color: white;
}

.btn {
  border: none;
  padding: 5px 12px;
  color: aliceblue;
  background-color: var(--primary-dark);
  cursor: pointer;
  border-radius: var(--radius);
  transition: all 0.5s;
  font-family: 'Poppins', sans-serif;
}

.btn:hover {
  background-color: black;
}

input,
select {
  width: 100%;
  outline-color: black;
  padding: 6px;
  margin: 4px 0;
  border-radius: var(--radius);
  border: 1px solid #cbd5e1;
  font-family: 'Poppins', sans-serif;
}

.field-label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  color: white;
  font-weight: 600;
}

@media (max-width: 768px) {
  .container {
    padding: 10px;
  }

  .menu {
    flex-direction: column;
  }

  .btn {
    width: 100%;
  }

  .table {
    font-size: 0.8rem;
  }

  th,
  td {
    padding: 4px;
    white-space: nowrap;
  }
}
</style>
