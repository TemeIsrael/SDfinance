<template>
  <main class="container">
    <div>
      <h2>Liste de la caisse de fonctionnement</h2>
    </div>

    <div class="menu">
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

    <form id="form-membre" @submit.prevent="ajouterMembreCaisse">
      <input
        type="text"
        id="nomEtPrenom"
        v-model="newMembreCaisse.nom"
        placeholder="Nom et Prénom"
      />
      <select id="groupeMembre" v-model="newMembreCaisse.groupe">
        <option value="">-- Sélectionnez un groupe --</option>
        <option v-for="option in groupeFormOptions" :key="option.value" :value="option.value">
          {{ option.label }}
        </option>
      </select>
      <div class="pourAjout">
        <button class="btn" id="soumission" type="submit">Ajouter</button>
      </div>
    </form>
  </main>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useGetData } from '@/composable/useGetData.js'
import { useFinance } from '@/composable/useFinance.js'
import { getGroupeLabel, membreGroupOptions } from '@/data/financeData.js'

const { isLoading, getData } = useGetData()
const { membreFiltre, newMembreCaisse, ajouterMembreCaisse, membresFiltres, totalMembres } =
  useFinance()

const groupeOptions = computed(() => membreGroupOptions)
const groupeFormOptions = computed(() =>
  membreGroupOptions.filter((option) => option.value !== 'tous'),
)

onMounted(() => getData('membres'))
</script>

<style scoped>
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
