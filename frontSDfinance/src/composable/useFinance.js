import { computed, ref } from 'vue'
import {
  initialAutresCaisses,
  initialCaisses,
  initialMembresCaisse,
  initialPaiements,
  initialTransactions,
  paiementFilters,
} from '@/data/financeData.js'
import { useAuthStore } from '@/stores/auth'

export const useFinance = () => {
  const { activeGroupeId, currentRole, currentUser, userGroups } = useAuthStore()
  
  const transactions = ref([...initialTransactions])
  const caisses = computed(() => {
    // Hide the church caisse ('offrandes') for presidents and trésoriers.
    if (['PRESIDENT', 'TRESORIER_CAISSIER'].includes(currentRole.value)) {
      return initialCaisses.filter(c => c.id !== 'offrandes');
    }
    // All other roles (MEMBRE, LEADER, COLLABORATOR, etc.) see all caisses.
    return initialCaisses;
  })
  const paiements = ref([...initialPaiements])
  const membresCaisse = ref([...initialMembresCaisse])
  const autresCaisses = ref([...initialAutresCaisses])
  const caisseSelectionnee = ref(null)
  const caisseId = computed(() => caisseSelectionnee.value?.id)
  const filtreActif = ref('dime')
  const membreFiltre = ref('tous')
  const anneeFiltre = ref('')

  const newTrans = ref({ description: '', montant: '', motif: '', date: '' })
  const newPaiement = ref({ nom: '', groupe: '', montant: '', date: '', type: 'dime' })
  const newMembreCaisse = ref({ nom: '', groupe: '' })
  const newAutreCaisse = ref({ nom: '', groupe: '', montant: '', date: '' })

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

  const ajouterTransaction = () => {
    if (!newTrans.value.description || !newTrans.value.montant) return
    transactions.value.push({ ...newTrans.value, id: Date.now() })
    newTrans.value = { description: '', montant: '', motif: '', date: '' }
  }

  const selectionnerCaisse = (c) => {
    caisseSelectionnee.value = c
  }

  const ajouterPaiement = () => {
    if (!newPaiement.value.nom || !newPaiement.value.montant) return
    paiements.value.push({ ...newPaiement.value, id: Date.now() })
    newPaiement.value = { nom: '', groupe: '', montant: '', date: '', type: 'dime' }
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
          date: new Date().toLocaleDateString('fr-FR'),
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
          sexe: 'HOMME',
          statut: 'ACTIF',
          groupeId,
        }),
      })
      if (res.ok) {
        await fetchMembres()
        newMembreCaisse.value = { nom: '', groupe: '' }
      } else {
        // Fallback local if backend fails or returns error
        membresCaisse.value.push({
          id: Date.now(),
          nom: fullName,
          groupe: String(groupeId),
          date: new Date().toLocaleDateString('fr-FR'),
        })
        newMembreCaisse.value = { nom: '', groupe: '' }
      }
    } catch (err) {
      console.error('Erreur ajout membre:', err)
      membresCaisse.value.push({
        id: Date.now(),
        nom: fullName,
        groupe: String(groupeId),
        date: new Date().toLocaleDateString('fr-FR'),
      })
      newMembreCaisse.value = { nom: '', groupe: '' }
    }
  }

  const paiementsFiltres = computed(() => {
    if (!caisseSelectionnee.value || caisseSelectionnee.value.id !== 'offrandes') {
      return paiements.value
    }

    let filtered = paiements.value

    if (currentRole.value === 'MEMBRE') {
        // MEMBRE sees only dimes and actions de grâce (offrande excluded)
        filtered = filtered.filter(p => p.type !== 'offrande')
      }

    if (filtreActif.value && currentRole.value !== 'MEMBRE') {
      filtered = filtered.filter((p) => p.type === filtreActif.value)
    }

    return filtered
  })

  const membresFiltres = computed(() => {
    let filtered = membresCaisse.value;
    // Non-leader roles see only members of the active group
    if (currentRole.value !== 'LEADER') {
      const targetGroupeId = activeGroupeId.value || userGroups.value?.[0] || 1;
      filtered = filtered.filter(m => String(m.groupe) === String(targetGroupeId));
    } else {
      // Leader can filter by selected group if not 'tous'
      if (membreFiltre.value !== 'tous') {
        filtered = filtered.filter(m => String(m.groupe) === String(membreFiltre.value));
      }
    }
    // Sort alphabetically by member name
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
          const year = new Date(item.date.split('/').reverse().join('-')).getFullYear().toString()
          return year === normalizedYear
        })
      : items

    if (caisseSelectionnee.value?.id !== 'epargne') {
      return filteredItems
    }

    const grouped = new Map()

    filteredItems.forEach((item) => {
      const key = `${item.nom.toLowerCase()}::${item.date.slice(-4)}`
      const existing = grouped.get(key)

      if (!existing) {
        grouped.set(key, { ...item, montant: Number(item.montant) })
      } else {
        existing.montant = Number(existing.montant) + Number(item.montant)
        existing.date = item.date
      }
    })

    return Array.from(grouped.values())
  })

  const totalAutresCaisses = computed(() =>
    autresCaissesFiltres.value.reduce((sum, item) => sum + Number(item.montant), 0),
  )

  const ajouterAutreCaisse = () => {
    if (!newAutreCaisse.value.nom || !newAutreCaisse.value.montant) return

    const date = newAutreCaisse.value.date || new Date().toISOString().slice(0, 10)
    const formattedDate = date.split('-').reverse().join('/')

    if (caisseId.value === 'epargne') {
      const existing = autresCaisses.value.find(
        (item) =>
          item.caisse === caisseId.value &&
          item.nom.trim().toLowerCase() === newAutreCaisse.value.nom.trim().toLowerCase() &&
          item.date.endsWith(new Date(date).getFullYear().toString()),
      )

      if (existing) {
        existing.montant = Number(existing.montant) + Number(newAutreCaisse.value.montant)
        existing.date = formattedDate
      } else {
        autresCaisses.value.push({
          id: Date.now(),
          nom: newAutreCaisse.value.nom.trim(),
          groupe: newAutreCaisse.value.groupe,
          montant: Number(newAutreCaisse.value.montant),
          date: formattedDate,
          caisse: caisseId.value,
        })
      }
    } else {
      autresCaisses.value.push({
        id: Date.now(),
        nom: newAutreCaisse.value.nom.trim(),
        groupe: newAutreCaisse.value.groupe,
        montant: Number(newAutreCaisse.value.montant),
        date: formattedDate,
        caisse: caisseId.value,
      })
    }

    newAutreCaisse.value = { nom: '', groupe: '', montant: '', date: '' }
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
    paiementsFiltres,
    membresFiltres,
    activeGroupeId,
    currentRole,
    currentUser,
    // existing returns ...
    totalMembres,
    autresCaissesFiltres,
    totalAutresCaisses,
    ajouterAutreCaisse,
    filtresPaiement: paiementFilters,
  }
}
