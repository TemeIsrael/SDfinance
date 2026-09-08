<template>
  <main class="container">
    <div class="header-tabs">
      <button 
        :class="['tab-btn', activeTab === 'membres' ? 'active' : '']" 
        @click="activeTab = 'membres'"
      >
        👥 Liste des Membres
      </button>
      <button 
        :class="['tab-btn', activeTab === 'appel' ? 'active' : '']" 
        @click="activeTab = 'appel'; fetchAttendanceSessions()"
      >
        📋 Prise d'Appel & Présences
      </button>
    </div>

    <!-- Onglet 1 : Liste des membres -->
    <div v-if="activeTab === 'membres'">
      <h2>Liste des membres</h2>

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
            <th v-if="currentRole === 'LEADER'">Groupe</th>
            <th>Date d'ajout</th>
          </tr>
        </thead>
        <tbody id="tbBody">
          <tr v-if="isLoading">
            <td :colspan="currentRole === 'LEADER' ? 4 : 3">Chargement…</td>
          </tr>
          <tr v-else-if="membresFiltres.length === 0">
            <td :colspan="currentRole === 'LEADER' ? 4 : 3">Aucun membre enregistré</td>
          </tr>
          <tr v-for="m in membresFiltres" :key="m.id">
            <td>{{ m.id }}</td>
            <td>{{ m.nom }}</td>
            <td v-if="currentRole === 'LEADER'">{{ getGroupeLabel(m.groupe) }}</td>
            <td>{{ m.date }}</td>
          </tr>
        </tbody>
        <tfoot>
          <tr>
            <td :colspan="currentRole === 'LEADER' ? 3 : 2">Nombre total</td>
            <td>{{ totalMembres }}</td>
          </tr>
        </tfoot>
      </table>

      <!-- Formulaire LEADER / COLLABORATOR -->
      <form v-if="currentRole === 'COLLABORATOR'" id="form-membre" @submit.prevent="ajouterEnfant">
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

      <!-- Formulaire PRESIDENT : ajouter un membre de son groupe dans la DB backend -->
      <form v-else-if="currentRole === 'PRESIDENT'" id="form-membre" @submit.prevent="ajouterMembreCaisse">
        <p class="form-hint">Ajouter un nouveau membre (Persistant)</p>
        <input
          type="text"
          id="nomEtPrenom"
          v-model="newMembreCaisse.nom"
          placeholder="Nom et Prénom"
          required
        />
        <div class="pourAjout">
          <button class="btn" id="soumission" type="submit">Ajouter au Groupe</button>
        </div>
      </form>
    </div>

    <!-- Onglet 2 : Prise d'Appel & Présences -->
    <div v-if="activeTab === 'appel'" class="attendance-section">
      <h2>Prise d'Appel (Cultes & Réunions)</h2>

      <!-- Formulaire création de session d'appel (PRESIDENT uniquement) -->
      <div v-if="currentRole === 'PRESIDENT'" class="card-form">
        <h3>➕ Nouvelle Session d'Appel</h3>
        <form @submit.prevent="creerSessionAppel">
          <div class="form-group">
            <label>Titre de la séance</label>
            <input 
              type="text" 
              v-model="newSession.titre" 
              placeholder="Ex: Culte du Dimanche, Réunion de Prière..." 
              required 
            />
          </div>

          <div class="form-group">
            <label>Type de Culte</label>
            <select v-model="newSession.typeCulte" required>
              <option value="CULTE_DIMANCHE">Culte Dimanche</option>
              <option value="CULTE_MARDI">Culte Mardi</option>
              <option value="ETUDE_JEUDI">Étude Jeudi</option>
              <option value="PRIERE">Réunion de Prière</option>
              <option value="ETUDE_BIBLIQUE">Étude Biblique</option>
              <option value="REPETITION">Répétition</option>
              <option value="AUTRE">Autre</option>
            </select>
          </div>

          <!-- Champ Libellé spécifique si typeCulte === 'AUTRE' -->
          <div v-if="newSession.typeCulte === 'AUTRE'" class="form-group">
            <label>Libellé spécifique (Obligatoire pour Autre)</label>
            <input 
              type="text" 
              v-model="newSession.libelle" 
              placeholder="Ex: Répétition Chorale, Réunion Spéciale Jeunesse..." 
              required 
            />
          </div>

          <button class="btn btn-success" type="submit" :disabled="isSubmittingSession">
            {{ isSubmittingSession ? 'Création en cours...' : 'Ouvrir la session d\'appel' }}
          </button>
        </form>
      </div>

      <!-- Liste des sessions d'appel -->
      <div class="sessions-list">
        <h3>📋 Séances d'appel enregistrées</h3>
        <p v-if="sessionsList.length === 0" class="empty-msg">Aucune session d'appel créée pour le moment.</p>

        <div v-for="sess in sessionsList" :key="sess.id" class="session-card">
          <div class="session-header">
            <div>
              <strong>{{ sess.titre }}</strong>
              <span class="badge-type">
                {{ sess.typeCulte === 'AUTRE' && sess.libelle ? sess.libelle : sess.typeCulte }}
              </span>
            </div>
            <span :class="['badge-status', sess.cloturee ? 'closed' : 'open']">
              {{ sess.cloturee ? '🔒 Clôturée' : '🟢 Ouverte' }}
            </span>
          </div>

          <div class="session-details">
            <small>📅 {{ new Date(sess.dateSession).toLocaleString('fr-FR') }}</small>
            <div class="stats-pills">
              <span class="pill present">Présents : {{ sess.totalPresents }}</span>
              <span class="pill absent">Absents : {{ sess.totalAbsents }}</span>
            </div>
          </div>

          <div class="session-actions">
            <button class="btn btn-small" @click="ouvrirFeuilleAppel(sess)">
              {{ activeSession?.id === sess.id ? 'Masquer la feuille d\'appel' : 'Faire / Consulter l\'appel' }}
            </button>
          </div>

          <!-- Feuille d'appel dépliée pour la session active -->
          <div v-if="activeSession?.id === sess.id" class="feuille-appel">
            <h4>Feuille d'Appel : {{ sess.titre }}</h4>
            <p v-if="sess.cloturee" class="warn-msg">⚠️ Cette séance est clôturée. La modification est désactivée.</p>

            <table class="table table-appel">
              <thead>
                <tr>
                  <th>Membre</th>
                  <th>Présence</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="rec in currentRecords" :key="rec.memberId">
                  <td><strong>{{ rec.memberNom }} {{ rec.memberPrenom }}</strong></td>
                  <td class="presence-toggle">
                    <button
                      type="button"
                      :disabled="sess.cloturee"
                      :class="['btn-toggle', rec.statut === 'PRESENT' ? 'btn-present' : '']"
                      @click="rec.statut = 'PRESENT'"
                    >
                      ✅ Présent
                    </button>
                    <button
                      type="button"
                      :disabled="sess.cloturee"
                      :class="['btn-toggle', rec.statut === 'ABSENT' ? 'btn-absent' : '']"
                      @click="rec.statut = 'ABSENT'"
                    >
                      ❌ Absent
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>

            <div v-if="!sess.cloturee && currentRole === 'PRESIDENT'" class="batch-actions">
              <button class="btn btn-success" @click="enregistrerAppel(sess.id)">
                💾 Enregistrer l'appel
              </button>
              <button class="btn btn-warning" @click="cloturerSession(sess.id)">
                🔒 Clôturer la séance
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useGetData } from '@/composable/useGetData.js'
import { useFinance } from '@/composable/useFinance.js'
import { getGroupeLabel, membreGroupOptions } from '@/data/financeData.js'

const { isLoading } = useGetData()
const { 
  membreFiltre, 
  newMembreCaisse, 
  ajouterMembreCaisse, 
  membresCaisse, 
  membresFiltres, 
  totalMembres, 
  currentRole,
  fetchMembres,
  activeGroupeId 
} = useFinance()

const groupeOptions = computed(() => membreGroupOptions)
const activeTab = ref('membres')

// Formulaire enfants (COLLABORATOR)
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

// State pour la prise d'appel
const sessionsList = ref([])
const activeSession = ref(null)
const currentRecords = ref([])
const isSubmittingSession = ref(false)

const newSession = ref({
  titre: '',
  typeCulte: 'CULTE_DIMANCHE',
  libelle: ''
})

const fetchAttendanceSessions = async () => {
  const token = localStorage.getItem('token')
  if (!token) return
  try {
    const res = await fetch('/api/members/attendance/sessions', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.ok) {
      sessionsList.value = await res.json()
    }
  } catch (err) {
    console.error('Erreur chargement sessions d\'appel:', err)
  }
}

const creerSessionAppel = async () => {
  if (!newSession.value.titre) return
  if (newSession.value.typeCulte === 'AUTRE' && !newSession.value.libelle) {
    alert('Le libellé est obligatoire pour le type Autre')
    return
  }

  isSubmittingSession.value = true
  const token = localStorage.getItem('token')
  const groupeId = Number(activeGroupeId.value || 1)

  try {
    const res = await fetch('/api/members/attendance/sessions', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify({
        titre: newSession.value.titre,
        typeCulte: newSession.value.typeCulte,
        libelle: newSession.value.typeCulte === 'AUTRE' ? newSession.value.libelle : null,
        groupeId: groupeId
      })
    })

    if (res.ok) {
      newSession.value = { titre: '', typeCulte: 'CULTE_DIMANCHE', libelle: '' }
      await fetchAttendanceSessions()
    } else {
      const err = await res.json().catch(() => ({}))
      alert(err.message || 'Erreur lors de la création de la session d\'appel')
    }
  } catch (err) {
    console.error('Erreur création session:', err)
  } finally {
    isSubmittingSession.value = false
  }
}

const ouvrirFeuilleAppel = async (session) => {
  if (activeSession.value?.id === session.id) {
    activeSession.value = null
    currentRecords.value = []
    return
  }

  activeSession.value = session
  const token = localStorage.getItem('token')

  try {
    const res = await fetch(`/api/members/attendance/sessions/${session.id}/records`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.ok) {
      currentRecords.value = await res.json()
    }
  } catch (err) {
    console.error('Erreur chargement feuille d\'appel:', err)
  }
}

const enregistrerAppel = async (sessionId) => {
  const token = localStorage.getItem('token')
  const items = currentRecords.value.map(r => ({
    memberId: r.memberId,
    statut: r.statut
  }))

  try {
    const res = await fetch(`/api/members/attendance/sessions/${sessionId}/batch`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify({ items })
    })

    if (res.ok) {
      alert('Appel enregistré avec succès !')
      await fetchAttendanceSessions()
    } else {
      alert('Erreur lors de l\'enregistrement de l\'appel')
    }
  } catch (err) {
    console.error('Erreur enregistrement appel:', err)
  }
}

const cloturerSession = async (sessionId) => {
  if (!confirm('Êtes-vous sûr de vouloir clôturer cette session d\'appel ?')) return
  const token = localStorage.getItem('token')

  try {
    const res = await fetch(`/api/members/attendance/sessions/${sessionId}/close`, {
      method: 'PUT',
      headers: { Authorization: `Bearer ${token}` }
    })

    if (res.ok) {
      alert('Session d\'appel clôturée !')
      await fetchAttendanceSessions()
      if (activeSession.value?.id === sessionId) {
        activeSession.value.cloturee = true
      }
    }
  } catch (err) {
    console.error('Erreur clôture session:', err)
  }
}

onMounted(() => {
  fetchMembres()
})
</script>

<style scoped>
.header-tabs {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-bottom: 16px;
}

.tab-btn {
  padding: 8px 16px;
  border: none;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border-radius: 20px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s;
}

.tab-btn.active, .tab-btn:hover {
  background: white;
  color: var(--primary-dark);
}

.form-hint {
  color: white;
  font-weight: 600;
  margin-bottom: 6px;
}

.container {
  margin-top: 10px;
  padding: 18px;
  background: var(--primary);
  width: 100%;
  max-width: 720px;
  margin-left: auto;
  margin-right: auto;
  border-radius: var(--radius);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
}

.container h2 {
  text-align: center;
  margin-bottom: 14px;
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

th, td {
  border: 1px solid black;
  padding: 8px;
  text-align: center;
  font-size: 0.9rem;
}

th {
  background-color: rgb(148, 74, 74);
  color: white;
}

.btn {
  border: none;
  padding: 8px 16px;
  color: aliceblue;
  background-color: var(--primary-dark);
  cursor: pointer;
  border-radius: var(--radius);
  transition: all 0.3s;
  font-family: 'Poppins', sans-serif;
  font-weight: 600;
}

.btn:hover {
  background-color: black;
}

.btn-success {
  background-color: #22c55e;
}
.btn-success:hover {
  background-color: #16a34a;
}

.btn-warning {
  background-color: #eab308;
  color: black;
}
.btn-warning:hover {
  background-color: #ca8a04;
}

.btn-small {
  padding: 4px 10px;
  font-size: 0.85rem;
}

input, select {
  width: 100%;
  outline-color: black;
  padding: 8px;
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

/* Styles pour la section d'appel */
.attendance-section {
  color: white;
}

.card-form {
  background: white;
  color: black;
  padding: 14px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.card-form h3 {
  margin-bottom: 10px;
  color: var(--primary-dark);
}

.form-group {
  margin-bottom: 10px;
}

.form-group label {
  display: block;
  font-weight: 600;
  font-size: 0.85rem;
  margin-bottom: 2px;
}

.sessions-list h3 {
  margin-bottom: 10px;
}

.empty-msg {
  text-align: center;
  font-style: italic;
  opacity: 0.8;
}

.session-card {
  background: white;
  color: black;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 12px;
}

.session-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.badge-type {
  display: inline-block;
  margin-left: 8px;
  background: #e2e8f0;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
}

.badge-status {
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
}
.badge-status.open { background: #dcfce7; color: #15803d; }
.badge-status.closed { background: #fee2e2; color: #b91c1c; }

.session-details {
  margin: 8px 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stats-pills {
  display: flex;
  gap: 6px;
}

.pill {
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 0.75rem;
  font-weight: 600;
}
.pill.present { background: #dcfce7; color: #166534; }
.pill.absent { background: #fee2e2; color: #991b1b; }
.pill.pending { background: #fef3c7; color: #92400e; }

.feuille-appel {
  margin-top: 14px;
  border-top: 1px solid #e2e8f0;
  padding-top: 10px;
}

.presence-toggle {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.btn-toggle {
  padding: 4px 10px;
  border: 1px solid #cbd5e1;
  background: #f8fafc;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.8rem;
  font-weight: 600;
}

.btn-toggle.btn-present {
  background: #22c55e;
  color: white;
  border-color: #16a34a;
}

.btn-toggle.btn-absent {
  background: #ef4444;
  color: white;
  border-color: #dc2626;
}

.batch-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 10px;
}

.warn-msg {
  color: #b91c1c;
  font-weight: 600;
  font-size: 0.85rem;
}
</style>
