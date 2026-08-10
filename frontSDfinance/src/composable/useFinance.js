import { computed, ref } from 'vue'
import {
  initialAutresCaisses,
  initialCaisses,
  initialMembresCaisse,
  initialPaiements,
  initialTransactions,
  paiementFilters,
} from '@/data/financeData.js'

export const useFinance = () => {
  const transactions = ref([...initialTransactions])
  const caisses = ref([...initialCaisses])
  const paiements = ref([...initialPaiements])
  const membresCaisse = ref([...initialMembresCaisse])
  const autresCaisses = ref([...initialAutresCaisses])
  const caisseSelectionnee = ref(null)
  const filtreActif = ref('dime')
  const membreFiltre = ref('tous')
  const anneeFiltre = ref('')

  const newTrans = ref({ description: '', montant: '', motif: '', date: '' })
  const newPaiement = ref({ nom: '', montant: '', date: '', type: 'dime' })
  const newMembreCaisse = ref({ nom: '', groupe: '' })
  const newAutreCaisse = ref({ nom: '', montant: '', date: '' })

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
    newPaiement.value = { nom: '', montant: '', date: '', type: 'dime' }
  }

  const ajouterMembreCaisse = () => {
    if (!newMembreCaisse.value.nom || !newMembreCaisse.value.groupe) return
    membresCaisse.value.push({
      ...newMembreCaisse.value,
      id: membresCaisse.value.length + 1,
      date: new Date().toLocaleDateString('fr-FR'),
    })
    newMembreCaisse.value = { nom: '', groupe: '' }
  }

  const paiementsFiltres = computed(() => {
    if (!caisseSelectionnee.value || caisseSelectionnee.value.id !== 'offrandes') {
      return paiements.value
    }

    if (!filtreActif.value) {
      return paiements.value
    }

    return paiements.value.filter((p) => p.type === filtreActif.value)
  })

  const membresFiltres = computed(() => {
    if (membreFiltre.value === 'tous') {
      return membresCaisse.value
    }

    return membresCaisse.value.filter((m) => m.groupe === membreFiltre.value)
  })

  const totalMembres = computed(() => membresFiltres.value.length)

  const autresCaissesFiltres = computed(() => {
    const items = autresCaisses.value.filter((item) => item.caisse === caisseSelectionnee.value?.id)

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
    const caisseId = caisseSelectionnee.value?.id

    if (caisseId === 'epargne') {
      const existing = autresCaisses.value.find(
        (item) =>
          item.caisse === caisseId &&
          item.nom.toLowerCase() === newAutreCaisse.value.nom.trim().toLowerCase() &&
          item.date.endsWith(new Date(date).getFullYear().toString()),
      )

      if (existing) {
        existing.montant = Number(existing.montant) + Number(newAutreCaisse.value.montant)
        existing.date = formattedDate
      } else {
        autresCaisses.value.push({
          id: Date.now(),
          nom: newAutreCaisse.value.nom.trim(),
          montant: Number(newAutreCaisse.value.montant),
          date: formattedDate,
          caisse: caisseId,
        })
      }
    } else {
      autresCaisses.value.push({
        id: Date.now(),
        nom: newAutreCaisse.value.nom.trim(),
        montant: Number(newAutreCaisse.value.montant),
        date: formattedDate,
        caisse: caisseId,
      })
    }

    newAutreCaisse.value = { nom: '', montant: '', date: '' }
  }

  return {
    transactions,
    caisses,
    paiements,
    membresCaisse,
    autresCaisses,
    caisseSelectionnee,
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
    paiementsFiltres,
    membresFiltres,
    totalMembres,
    autresCaissesFiltres,
    totalAutresCaisses,
    ajouterAutreCaisse,
    filtresPaiement: paiementFilters,
  }
}
