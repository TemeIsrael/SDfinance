export const initialTransactions = []

export const initialCaisses = [
  { id: 'offrandes', nom: "Caisse de l'église", solde: '0 FCFA' },
  { id: 'epargne', nom: 'Épargnes personnelles', solde: '0 FCFA' },
  { id: 'basilique', nom: 'Construction Basilique', solde: '0 FCFA' },
  { id: 'fonctionnement', nom: 'Fonctionnement', solde: '0 FCFA' },
  { id: 'specifique', nom: 'Caisses spécifiques', solde: '0 caisse ouverte' },
]

export const initialPaiements = []

export const membreGroupOptions = [
  { value: 'tous', label: 'Tous' },
  { value: '1', label: 'Jeunes' },
  { value: '2', label: 'Femmes' },
  { value: '3', label: 'Hommes' },
  { value: '4', label: 'Aucun' },
  { value: '5', label: 'Enfants' },
]

export const getGroupeLabel = (value) =>
  membreGroupOptions.find((option) => String(option.value) === String(value))?.label || value

export const initialMembresCaisse = []

export const initialAutresCaisses = []

export const paiementFilters = [
  { label: 'Dimes', value: 'dime' },
  { label: 'Offrandes', value: 'offrande' },
  { label: 'Actions de grâce', value: 'action de grace' },
]

export const formatCurrency = (value) => Number(value ?? 0).toLocaleString('fr-FR') + ' FCFA'
