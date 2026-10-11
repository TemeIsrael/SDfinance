import { computed, ref } from 'vue'
import { initialCaisses, paiementFilters } from '@/data/financeData.js'
import { useAuthStore } from '@/stores/auth'

export const useFinance = () => {
  const { activeGroupeId, currentRole, currentUser, userGroups } = useAuthStore()
  
  // States populated from API
  const records = ref([])
  const membresCaisse = ref([])
  
  // Split records into the 3 UI categories
  const transactions = computed(() => records.value.filter(r => r.typeRecord === 'TRANSACTION_CAISSE'))
  const paiements = computed(() => records.value.filter(r => r.typeRecord === 'PAIEMENT_MEMBRE'))
  const autresCaisses = computed(() => records.value.filter(r => r.typeRecord === 'ENTREE_CAISSE'))
  
  const caisses = computed(() => {
    if (['PRESIDENT', 'TRESORIER_CAISSIER'].includes(currentRole.value)) {
      return initialCaisses.filter(c => c.id !== 'offrandes');
    }
    return initialCaisses;
  })
  
  const caisseSelectionnee = ref(null)
  const caisseId = computed(() => caisseSelectionnee.value?.id)
  const filtreActif = ref('dime')
  const membreFiltre = ref('tous')
  const anneeFiltre = ref('')

  const newTrans = ref({ description: '', montant: '', motif: '', date: '' })
  const newPaiement = ref({ nom: '', groupe: '', montant: '', date: '', type: 'dime' })
  const newMembreCaisse = ref({ nom: '', groupe: '', photoUrl: '', dateAjout: '' })
  const newAutreCaisse = ref({ nom: '', groupe: '', montant: '', date: '' })

  const fetchFinanceData = async () => {
    const token = localStorage.getItem('token')
    if (!token) return
    try {
      const res = await fetch('/api/finance/records', {
        headers: { Authorization: `Bearer ${token}` }
      })
      if (res.ok) {
        const data = await res.json()
        // Map backend DTO to frontend structure
        records.value = data.map(r => ({
          id: r.id,
          typeRecord: r.typeRecord,
          caisse: r.caisseId,
          montant: r.montant,
          date: r.dateOperation ? new Date(r.dateOperation).toLocaleDateString('fr-FR') : '',
          description: r.description,
          type: r.description, // For paiements, description = type (dime, offrande)
          nom: r.membreNom || '',
          groupe: r.membreGroupeId ? String(r.membreGroupeId) : ''
        }))
      }
    } catch (err) {
      console.error('Erreur chargement finance:', err)
    }
  }

  const totalEntrees = computed(() =>
    transactions.value
      .filter((t) => t.description === 'depot')
      .reduce((sum, t) => sum + Number(t.montant), 0),
  )

  const totalSorties = computed(() =>
    transactions.value
      .filter((t) => t.description === 'retrait')
      .reduce((sum, t) => sum + Number(t.montant), 0),
  )

  const ajouterRecord = async (typeRecord, caisseId, montant, description, dateStr, membreNom = null) => {
    const token = localStorage.getItem('token')
    const groupeId = Number(activeGroupeId.value || userGroups.value?.[0] || 1)
    
    // Convert DD/MM/YYYY to YYYY-MM-DD for backend
    let dateOperation = null
    if (dateStr && dateStr.includes('/')) {
        const parts = dateStr.split('/')
        dateOperation = `${parts[2]}-${parts[1]}-${parts[0]}`
    } else if (dateStr && dateStr.includes('-')) {
        dateOperation = dateStr
    }

    try {
      const res = await fetch('/api/finance/records', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({
          typeRecord,
          caisseId,
          montant: Number(montant),
          dateOperation,
          description,
          membreNom,
          membreGroupeId: membreNom ? groupeId : null,
          groupeId
        })
      })
      if (res.ok) {
        await fetchFinanceData()
        return true
      }
      return false
    } catch (err) {
      console.error('Erreur ajout record:', err)
      return false
    }
  }

  const ajouterTransaction = async () => {
    if (!newTrans.value.description || !newTrans.value.montant) return
    const success = await ajouterRecord(
        'TRANSACTION_CAISSE',
        caisseId.value || 'fonctionnement',
        newTrans.value.montant,
        newTrans.value.description, // 'depot' ou 'retrait'
        newTrans.value.date || new Date().toISOString().slice(0, 10)
    )
    if (success) {
      newTrans.value = { description: '', montant: '', motif: '', date: '' }
    }
  }

  const selectionnerCaisse = (c) => {
    caisseSelectionnee.value = c
  }

  const ajouterPaiement = async () => {
    if (!newPaiement.value.nom || !newPaiement.value.montant) return
    const success = await ajouterRecord(
        'PAIEMENT_MEMBRE',
        'offrandes', // Les paiements de dîmes vont dans offrandes
        newPaiement.value.montant,
        newPaiement.value.type,
        newPaiement.value.date || new Date().toISOString().slice(0, 10),
        newPaiement.value.nom
    )
    if (success) {
      newPaiement.value = { nom: '', groupe: '', montant: '', date: '', type: 'dime' }
    }
  }

  const fetchMembres = async () => {
    const token = localStorage.getItem('token')
    if (!token) return
    try {
      const res = await fetch('/api/members', {
        headers: { Authorization: `Bearer ${token}` },
      })
      if (res.ok) {
        const data = await res.json()
        membresCaisse.value = data.map((m) => ({
          id: m.id,
          nom: `${m.nom} ${m.prenom}`.trim(),
          groupe: String(m.groupeId),
          photoUrl: m.photoUrl || '',
          date: m.dateAjout
            ? new Date(m.dateAjout).toLocaleDateString('fr-FR')
            : new Date().toLocaleDateString('fr-FR'),
        }))
      }
    } catch (err) {
      console.error('Erreur chargement membres API:', err)
    }
  }

  const ajouterMembreCaisse = async () => {
    if (!newMembreCaisse.value.nom) return
    const token = localStorage.getItem('token')
    const groupeId = Number(activeGroupeId.value || userGroups.value?.[0] || 1)
    const fullName = newMembreCaisse.value.nom.trim()
    const spaceIndex = fullName.indexOf(' ')
    const nom = spaceIndex > 0 ? fullName.substring(0, spaceIndex) : fullName
    const prenom = spaceIndex > 0 ? fullName.substring(spaceIndex + 1) : '.'

    try {
      const res = await fetch('/api/members', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          nom,
          prenom,
          sexe: 'H',
          statut: 'ACTIF',
          groupeId,
          photoUrl: newMembreCaisse.value.photoUrl || null,
          dateAjout: newMembreCaisse.value.dateAjout || null,
        }),
      })
      if (res.ok) {
        await fetchMembres()
        newMembreCaisse.value = { nom: '', groupe: '', photoUrl: '', dateAjout: '' }
      } else {
        const err = await res.json().catch(() => ({}))
        alert(err.message || 'Erreur lors de l\'ajout du membre. Veuillez réessayer.')
      }
    } catch (err) {
      console.error('Erreur ajout membre:', err)
      alert('Erreur de connexion. Vérifiez votre connexion et réessayez.')
    }
  }

  const paiementsFiltres = computed(() => {
    let filtered = paiements.value.filter(p => p.caisse === caisseSelectionnee.value?.id)

    if (caisseSelectionnee.value?.id === 'offrandes') {
      if (currentRole.value === 'MEMBRE') {
        filtered = filtered.filter(p => p.type !== 'offrande')
      }
      if (filtreActif.value && currentRole.value !== 'MEMBRE') {
        filtered = filtered.filter((p) => p.type === filtreActif.value)
      }
    }

    const normalizedYear = String(anneeFiltre.value || '').trim()
    if (normalizedYear) {
      filtered = filtered.filter((p) => {
        if (!p.date) return false;
        const year = new Date(p.date.split('/').reverse().join('-')).getFullYear().toString()
        return year === normalizedYear
      })
    }

    return filtered
  })

  const membresFiltres = computed(() => {
    let filtered = membresCaisse.value;
    if (currentRole.value !== 'LEADER') {
      const targetGroupeId = activeGroupeId.value || userGroups.value?.[0] || 1;
      filtered = filtered.filter(m => String(m.groupe) === String(targetGroupeId));
    } else {
      if (membreFiltre.value !== 'tous') {
        filtered = filtered.filter(m => String(m.groupe) === String(membreFiltre.value));
      }
    }
    return filtered.slice().sort((a, b) => a.nom.localeCompare(b.nom));
  })

  const totalMembres = computed(() => membresFiltres.value.length)

  const autresCaissesFiltres = computed(() => {
    let items = autresCaisses.value.filter((item) => item.caisse === caisseSelectionnee.value?.id)

    if (currentRole.value === 'MEMBRE') {
      const username = currentUser.value?.username?.trim().toLowerCase()
      items = items.filter(item => item.nom.trim().toLowerCase() === username)
    }

    const normalizedYear = String(anneeFiltre.value || '').trim()
    const filteredItems = normalizedYear
      ? items.filter((item) => {
          if (!item.date) return false;
          const year = new Date(item.date.split('/').reverse().join('-')).getFullYear().toString()
          return year === normalizedYear
        })
      : items

    if (caisseSelectionnee.value?.id !== 'epargne') {
      return filteredItems
    }

    const grouped = new Map()

    filteredItems.forEach((item) => {
      const dateSuffix = item.date ? item.date.slice(-4) : ''
      const key = `${item.nom.toLowerCase()}::${dateSuffix}`
      const existing = grouped.get(key)

      if (!existing) {
        grouped.set(key, { ...item, montant: Number(item.montant) })
      } else {
        existing.montant = Number(existing.montant) + Number(item.montant)
        // Keep the latest date or formatting logic if needed
      }
    })

    return Array.from(grouped.values())
  })

  const totalAutresCaisses = computed(() =>
    autresCaissesFiltres.value.reduce((sum, item) => sum + Number(item.montant), 0),
  )

  const totalCaisseComplet = computed(() => {
    const totalP = paiementsFiltres.value.reduce((sum, p) => sum + Number(p.montant), 0)
    const totalA = autresCaissesFiltres.value.reduce((sum, item) => sum + Number(item.montant), 0)
    return totalP + totalA
  })

  const ajouterAutreCaisse = async () => {
    if (!newAutreCaisse.value.nom || !newAutreCaisse.value.montant) return

    const success = await ajouterRecord(
        'ENTREE_CAISSE',
        caisseId.value,
        newAutreCaisse.value.montant,
        'Contribution',
        newAutreCaisse.value.date || new Date().toISOString().slice(0, 10),
        newAutreCaisse.value.nom.trim()
    )

    if (success) {
      newAutreCaisse.value = { nom: '', groupe: '', montant: '', date: '' }
    }
  }

  return {
    transactions,
    caisses,
    paiements,
    membresCaisse,
    autresCaisses,
    caisseSelectionnee,
    caisseId,
    filtreActif,
    membreFiltre,
    anneeFiltre,
    newTrans,
    newPaiement,
    newMembreCaisse,
    newAutreCaisse,
    totalEntrees,
    totalSorties,
    ajouterTransaction,
    selectionnerCaisse,
    ajouterPaiement,
    ajouterMembreCaisse,
    fetchMembres,
    fetchFinanceData,
    paiementsFiltres,
    membresFiltres,
    activeGroupeId,
    userGroups,
    currentRole,
    currentUser,
    totalMembres,
    autresCaissesFiltres,
    totalAutresCaisses,
    totalCaisseComplet,
    ajouterAutreCaisse,
    filtresPaiement: paiementFilters,
  }
}
