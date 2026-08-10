<template>
  <!-- En-tête de l'application connectée (style gestionTransactions.html) -->
  <div class="entet">
    <div class="logo"><h3>SDfinance</h3></div>
    <div>Bienvenue,</div>
    <div><h3>Président</h3></div>
    <div>
      <button class="btn" id="deconnexion" @click="seDeconnecter">Déconnexion</button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const seDeconnecter = () => {
  router.push('/accueil')
}
</script>

<style scoped>
.entet {
  grid-row: 1;
  grid-column: 1 / 3;
  background-color: var(--primary);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 50px;
  height: var(--header-h);
}

.entet > div {
  width: 120px;
}

.logo {
  color: white;
  padding-top: 0;
}

.btn {
  width: 100%;
  border: none;
  padding: 6px 10px;
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
</style>
