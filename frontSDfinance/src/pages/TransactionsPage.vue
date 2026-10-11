<template>
  <!-- Page gestion des transactions + caisses (style gestionTransactions.html) -->
  <section class="gestion-transactions">
    <!-- Solde global -->
    <div class="solde-zone" v-if="currentRole !== 'MEMBRE'">
      <h3>Gestion des transactions</h3>
      <div class="containerSolde">
        <div class="entree">
          <h4>Entrées</h4>
          <p class="montant" id="montantEntree">{{ formatCurrency(totalEntrees) }}</p>
        </div>
        <div class="sorties">
          <h4>Sorties</h4>
          <p class="montant" id="montantSorties">{{ formatCurrency(totalSorties) }}</p>
        </div>
      </div>
    </div>

    <!-- Transactions -->
    <div class="transaction" v-if="currentRole !== 'MEMBRE'">
      <!-- Formulaire ajout -->
      <div class="AjoutTrans" v-if="canManageFinance">
        <h4>Ajouter une transaction</h4>
        <form id="form-transaction" @submit.prevent="ajouterTransaction">
          <label for="desc">Type de transaction</label>
          <select id="desc" v-model="newTrans.description">
            <option value="" disabled>Sélectionner (depot/retrait)</option>
            <option value="depot">Dépôt (Entrée)</option>
            <option value="retrait">Retrait (Sortie)</option>
          </select>
          <label for="montant">Montant</label>
          <input type="number" id="montant" v-model="newTrans.montant" placeholder="Montant" />
          <label for="motif">Motif</label>
          <select id="motif" v-model="newTrans.motif">
            <option value="" disabled>Sélectionner un motif</option>
            <option value="FACTURES">sortie_factures</option>
            <option value="CONSTRUCTION">sortie_constructions</option>
            <option value="RECEPTION">sortie_réception</option>
            <option value="TRANSPORT_DES_MEMBRES">sortie_transport_des_membres</option>
            <option value="TAXI">sortie_taxi</option>
            <option value="SOUTIEN_DES_PASTEURS">sortie_soutien_des_pasteurs</option>
            <option value="ASSISTANCE">sortie_assistance</option>
            <option value="ACHAT_ET_DU_MATERIEL_ENTRETIEN">sortie_Achat_et_entretien_du_matériel</option>
            <option value="SECRETARIAT">sortie_secrétariat</option>
            <option value="TRVAUX_DIVERS">sortie_travaux_divers</option>
            <option value="EMPRUNT">sortie_emprunt</option>
            <option value="REMPBOURSEMENT">entree_remboursement</option>
            <option value="OFFRANDES">entree_offrandes</option>
            <option value="DIMES">entree_dîmes</option>
            <option value="ACTIONS_DE_GRACE">entree_actions_de_grâces</option>
            <option value="VOEUX">entree_vœux</option>
            <option value="DONS_VOLONTAIRES">entree_dons_volontaires</option>
            <option value="CONSECRATION">entree_offrandes_de_consécration</option>
            <option value="PENTECOTE">entree_collecte_Pentecôte</option>
            <option value="PAQUE">entree_collecte_Pâques</option>
            <option value="RECOLTE">entree_collecte_Récoltes</option>
            <option value="EVANGELISATION">entree_collecte_Évangélisation</option>
          </select>
          <label for="date">Date</label>
          <input type="date" id="date" v-model="newTrans.date" />
          <div class="pourAjout">
            <button class="btn" id="soumission" type="submit">Ajouter</button>
          </div>
        </form>
      </div>

      <!-- Liste transactions -->
      <div class="AfficTrans">
        <div class="header-with-export">
          <h4>Liste des transactions</h4>
          <ExportMenu
            title="Rapport des Transactions Financières"
            filename="transactions_financieres"
            :headers="['Description', 'Montant', 'Motif', 'Date']"
            :rows="transactionsExportRows"
            :summary="transactionsExportSummary"
            size="small"
          />
        </div>
        <table class="table">
          <thead>
            <tr>
              <th>Description</th>
              <th>Montant</th>
              <th>Motif</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody class="tbBody">
            <tr v-if="isLoading">
              <td colspan="4">Chargement…</td>
            </tr>
            <tr v-for="t in transactions" :key="t.id">
              <td>{{ t.description }}</td>
              <td>{{ formatCurrency(t.montant) }}</td>
              <td>{{ t.motif }}</td>
              <td>{{ t.date }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Gestion des caisses -->
    <div class="gestion-caisses">
      <div>
        <h3>Gestion des caisses</h3>
        <ul class="cards-caisses">
          <li
            v-for="caisse in caisses.filter(c => c.id !== 'specifique')"
            :key="caisse.id"
            class="card-caisse"
             :class="{ active: caisseSelectionnee && caisseSelectionnee.id === caisse.id }"
            @click.stop="selectionnerCaisse(caisse)"
          >
            <h4>{{ caisse.nom }}</h4>
            <p>{{ caisse.solde }}</p>
          </li>
        </ul>
      </div>

      <!-- Détail caisse sélectionnée -->
      <div class="detail" v-if="caisseSelectionnee">
        <h4 id="titreCaisse">{{ caisseSelectionnee.nom }}</h4>

        <div class="detail1">
          <div class="AfficTrans">
            <div class="header-with-export">
              <h4 v-if="caisseSelectionnee && caisseSelectionnee.id === 'offrandes'" style="margin: 0;">
                {{ currentRole === 'MEMBRE' ? 'Historique de mes Dîmes et Actions de grâce' : 'Liste des transactions' }}
              </h4>
              <h4 v-else style="margin: 0;">
                {{ currentRole === 'MEMBRE' ? 'Mon historique personnel' : 'Liste des entrées' }}
              </h4>
              <ExportMenu
                :title="'Rapport - Caisse ' + (caisseSelectionnee?.nom || 'Financière')"
                :subtitle="'Filtre actif : ' + (filtreActif || anneeFiltre || 'Tous')"
                :filename="'caisse_' + (caisseSelectionnee?.id || 'export')"
                :headers="caisseExportHeaders"
                :rows="caisseExportRows"
                :summary="caisseExportSummary"
                size="small"
              />
            </div>
            <div class="filtres" v-if="caisseSelectionnee && caisseSelectionnee.id === 'offrandes' && currentRole !== 'PRESIDENT'">
              <template v-for="filtre in filtresPaiement" :key="filtre.value">
                <button
                  v-if="currentRole !== 'MEMBRE' || filtre.value !== 'offrande'"
                  class="filtre-btn"
                  :class="{ actif: filtreActif === filtre.value }"
                  type="button"
                  @click="filtreActif = filtre.value"
                >
                  {{ filtre.label }}
                </button>
              </template>
            </div>
            <div class="filtres" v-else>
              <input
                v-model="anneeFiltre"
                type="text"
                inputmode="numeric"
                placeholder="Saisir une année"
                class="input-annee"
              />
            </div>
            <table class="table" v-if="currentRole !== 'MEMBRE'">
  <thead>
    <tr>
      <th v-if="currentRole !== 'MEMBRE'">Nom</th>
      <th v-if="currentRole !== 'MEMBRE' && (currentRole === 'LEADER' || currentRole === 'COLLABORATOR')">Groupe</th>
      <th>Date</th>
      <th>Montant</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="p in paiementsFiltres" :key="p.id">
      <td v-if="currentRole !== 'MEMBRE'">{{ p.nom }}</td>
      <td v-if="currentRole !== 'MEMBRE' && (currentRole === 'LEADER' || currentRole === 'COLLABORATOR')">{{ getGroupeLabel(p.groupeId || p.groupe) }}</td>
      <td>{{ p.date }}</td>
      <td>{{ formatCurrency(p.montant) }}</td>
    </tr>
    <tr v-for="item in autresCaissesFiltres" :key="item.id">
      <td v-if="currentRole !== 'MEMBRE'">{{ item.nom }}</td>
      <td v-if="currentRole !== 'MEMBRE' && (currentRole === 'LEADER' || currentRole === 'COLLABORATOR')">{{ getGroupeLabel(item.groupeId || item.groupe) }}</td>
      <td>{{ item.date }}</td>
      <td>{{ formatCurrency(item.montant) }}</td>
    </tr>
  </tbody>
  <tfoot v-if="caisseSelectionnee?.id !== 'offrandes'">
    <tr>
      <td :colspan="currentRole === 'LEADER' || currentRole === 'COLLABORATOR' ? 3 : 2">Total</td>
      <td>{{ formatCurrency(totalCaisseComplet) }}</td>
    </tr>
  </tfoot>
</table>

<table class="table" v-else-if="caisseSelectionnee && ['fonctionnement','epargne','basilique'].includes(caisseSelectionnee.id)">
  <thead>
    <tr>
      <th>Date</th>
      <th>Montant</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="p in paiementsFiltres" :key="p.id">
      <td>{{ p.date }}</td>
      <td>{{ formatCurrency(p.montant) }}</td>
    </tr>
    <tr v-for="item in autresCaissesFiltres" :key="item.id">
      <td>{{ item.date }}</td>
      <td>{{ formatCurrency(item.montant) }}</td>
    </tr>
  </tbody>
  <tfoot v-if="caisseSelectionnee && caisseSelectionnee.id !== 'offrandes'">
    <tr>
      <td colspan="1">Total</td>
      <td>{{ formatCurrency(totalAutresCaisses) }}</td>
    </tr>
  </tfoot>
</table>
          </div>

          <div class="AjoutTrans" v-if="canManageFinance && caisseSelectionnee && caisseSelectionnee.id === 'offrandes'">
            <form @submit.prevent="ajouterPaiement">
              <h4>Ajouter un paiement</h4>
              <input type="text" v-model="newPaiement.nom" placeholder="Nom du membre" />
              <input type="number" v-model="newPaiement.montant" placeholder="Montant" />
              <label for="typePaiement">Type</label>
              <select id="typePaiement" v-model="newPaiement.type">
                <option value="dime">Dime</option>
                <option value="offrande">Offrande</option>
                <option value="action de grace">Action de grâce</option>
              </select>
              <input type="date" v-model="newPaiement.date" />
              <div class="pourAjout">
                <button class="btn" type="submit">Ajouter</button>
              </div>
            </form>
          </div>

          <div class="AjoutTrans" v-else-if="canManageFinance">
            <form @submit.prevent="ajouterAutreCaisse">
              <h4>Ajouter une entrée</h4>
              <input type="text" v-model="newAutreCaisse.nom" placeholder="Nom du membre" />
              <input type="number" v-model="newAutreCaisse.montant" placeholder="Montant" />
              <input type="date" v-model="newAutreCaisse.date" />
              <div class="pourAjout">
                <button class="btn" type="submit">Ajouter</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useGetData } from '@/composable/useGetData.js'
import { useFinance } from '@/composable/useFinance.js'
import { formatCurrency, getGroupeLabel, membreGroupOptions } from '@/data/financeData.js'
import { useAuthStore } from '@/stores/auth'
import { hasPermission } from '@/utils/permissions'
import ExportMenu from '@/components/ExportMenu.vue'

const { isLoading, getData } = useGetData()
const {
  transactions,
  caisses,
  caisseSelectionnee,
  filtresPaiement,
  filtreActif,
  membreFiltre,
  anneeFiltre,
  newTrans,
  newPaiement,
  newMembreCaisse,
  newAutreCaisse,
  membresCaisse,
  totalEntrees,
  totalSorties,
  ajouterTransaction,
  selectionnerCaisse,
  ajouterPaiement,
  ajouterMembreCaisse,
  ajouterAutreCaisse,
  paiementsFiltres,
  membresFiltres,
  totalMembres,
  autresCaissesFiltres,
  totalAutresCaisses,
  totalCaisseComplet,
  fetchFinanceData,
} = useFinance()

const { currentRole } = useAuthStore()
const canManageFinance = computed(() => hasPermission(currentRole.value, 'finance', 'canManage'))

const groupeOptions = computed(() => membreGroupOptions)
const groupeFormOptions = computed(() =>
  membreGroupOptions.filter((option) => option.value !== 'tous'),
)

// Computed export data pour transactions
const transactionsExportRows = computed(() => {
  return (transactions.value || []).map(t => [
    t.description || '',
    formatCurrency(t.montant),
    t.motif || '',
    t.date || ''
  ])
})

const transactionsExportSummary = computed(() => [
  { label: 'Total Entrées', value: formatCurrency(totalEntrees.value) },
  { label: 'Total Sorties', value: formatCurrency(totalSorties.value) }
])

// Computed export data pour caisses
const caisseExportHeaders = computed(() => {
  if (currentRole.value === 'MEMBRE') {
    return ['Date', 'Montant']
  }
  if (currentRole.value === 'LEADER' || currentRole.value === 'COLLABORATOR') {
    return ['Nom', 'Groupe', 'Date', 'Montant']
  }
  return ['Nom', 'Date', 'Montant']
})

const caisseExportRows = computed(() => {
  const isMembre = currentRole.value === 'MEMBRE'
  const isLeaderOrCollab = currentRole.value === 'LEADER' || currentRole.value === 'COLLABORATOR'

  const pRows = (paiementsFiltres.value || []).map(p => {
    if (isMembre) return [p.date || '', formatCurrency(p.montant)]
    if (isLeaderOrCollab) return [p.nom || '', getGroupeLabel(p.groupeId || p.groupe), p.date || '', formatCurrency(p.montant)]
    return [p.nom || '', p.date || '', formatCurrency(p.montant)]
  })

  const aRows = (autresCaissesFiltres.value || []).map(item => {
    if (isMembre) return [item.date || '', formatCurrency(item.montant)]
    if (isLeaderOrCollab) return [item.nom || '', getGroupeLabel(item.groupeId || item.groupe), item.date || '', formatCurrency(item.montant)]
    return [item.nom || '', item.date || '', formatCurrency(item.montant)]
  })

  return [...pRows, ...aRows]
})

const caisseExportSummary = computed(() => {
  const totalVal = caisseSelectionnee.value?.id === 'offrandes' ? totalCaisseComplet.value : (totalCaisseComplet.value || totalAutresCaisses.value)
  return [
    { label: 'Total ' + (caisseSelectionnee.value?.nom || 'Caisse'), value: formatCurrency(totalVal) }
  ]
})

onMounted(() => {
  getData('transactions')
  fetchFinanceData()
})
</script>

<style scoped>
.gestion-transactions {
  display: flex;
  flex-direction: column;
  gap: 14px;
  padding: 10px;
  width: 100%;
}

.solde-zone {
  background: var(--primary);
  border-radius: var(--radius);
  padding: 14px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
}

.solde-zone h3 {
  text-align: center;
  margin-bottom: 8px;
  color: white;
}

.containerSolde {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

.entree,
.sorties {
  width: min(180px, 100%);
  min-height: 80px;
  border-radius: 10px;
  background: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.montant {
  font-weight: 700;
  color: var(--primary-dark);
}

.transaction,
.detail1 {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.AjoutTrans {
  width: min(220px, 100%);
  background: var(--primary);
  border-radius: var(--radius);
  padding: 12px;
  flex-shrink: 0;
}

.AjoutTrans h4 {
  text-align: center;
  margin-bottom: 6px;
}

.AfficTrans {
  flex: 1;
  background: linear-gradient(135deg, rgb(207, 232, 239), var(--primary));
  border-radius: var(--radius);
  padding: 10px;
  overflow-x: auto;
}

.pourAjout {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

label {
  display: block;
  margin-top: 10px;
  font-weight: 600;
  color: black;
  font-size: 0.85rem;
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

.input-annee {
  min-width: 140px;
  max-width: 180px;
}

.input-annee {
  min-width: 140px;
  max-width: 180px;
}

.btn {
  border: none;
  padding: 6px 18px;
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

.table {
  width: 100%;
  background: white;
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

/* caisses */
.gestion-caisses {
  display: flex;
  flex-direction: column;
  gap: 10px;
  background: var(--primary);
  border-radius: var(--radius);
  padding: 12px;
}

.gestion-caisses h3 {
  color: white;
}

.cards-caisses {
  list-style: none;
  display: flex;
  gap: 8px;
  overflow-x: auto;
  padding: 8px 0;
  scroll-snap-type: x proximity;
}

.card-caisse {
  min-width: 160px;
  scroll-snap-align: start;
  border: 1px solid #ccc;
  border-radius: 10px;
  background: white;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 8px;
  transition: 0.3s;
}

.card-caisse:hover,
.card-caisse.active {
  background: var(--primary-dark);
  color: white;
  border-color: var(--primary-dark);
}

.card-caisse p {
  font-size: 13px;
  font-weight: bold;
  color: #444;
  margin-top: 4px;
}

.card-caisse.active p,
.card-caisse:hover p {
  color: white;
}

.detail {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.detail h2 {
  color: white;
}

.filtres {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 8px;
}

.filtre-btn {
  border: none;
  background: white;
  color: var(--primary-dark);
  padding: 6px 10px;
  border-radius: 999px;
  cursor: pointer;
  font-weight: 600;
}

.filtre-btn.actif {
  background: var(--primary-dark);
  color: white;
}

.header-with-export {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  flex-wrap: wrap;
  gap: 10px;
}

@media (max-width: 768px) {
  .gestion-transactions {
    padding: 6px;
  }

  .containerSolde {
    gap: 10px;
  }

  .entree,
  .sorties,
  .AjoutTrans {
    width: 100%;
  }

  .transaction,
  .detail1 {
    flex-direction: column;
  }

  .AfficTrans {
    min-width: 0;
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
