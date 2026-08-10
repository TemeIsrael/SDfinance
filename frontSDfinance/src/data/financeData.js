export const initialTransactions = [
  {
    id: 1,
    description: 'retrait',
    montant: 300000,
    motif: 'fête des récoltes',
    date: '01.07.2026',
  },
  { id: 2, description: 'depot', montant: 50000, motif: 'Dîmes', date: '09.05.2026' },
]

export const initialCaisses = [
  { id: 'offrandes', nom: "Caisse de l'église", solde: '1 850 000 FCFA' },
  { id: 'epargne', nom: 'Épargnes personnelles', solde: '4 200 000 FCFA' },
  { id: 'basilique', nom: 'Construction Basilique', solde: '12 500 000 FCFA' },
  { id: 'fonctionnement', nom: 'Fonctionnement', solde: '850 000 FCFA' },
  { id: 'temporaire', nom: 'Caisses temporaires', solde: '4 caisses ouvertes' },
]

export const initialPaiements = [
  { id: 1, nom: 'Teme israel', montant: 300000, date: '01.07.2026', type: 'dime' },
  { id: 2, nom: 'Essomba Mablla', montant: 50000, date: '09.05.2026', type: 'offrande' },
  { id: 3, nom: 'Marius Tchou', montant: 25000, date: '12.05.2026', type: 'action de grace' },
]

export const membreGroupOptions = [
  { value: 'tous', label: 'Tous' },
  { value: 'groupe_jeunes', label: 'Jeunes' },
  { value: 'groupe_femmes', label: 'Femmes' },
  { value: 'groupe_hommes', label: 'Hommes' },
  { value: 'groupe_enfants', label: 'Enfants' },
]

export const getGroupeLabel = (value) =>
  membreGroupOptions.find((option) => option.value === value)?.label || value

export const initialMembresCaisse = [
  { id: 1, nom: 'Teme israel', groupe: 'groupe_hommes', date: '01/07/2026' },
  { id: 2, nom: 'Evina Brice', groupe: 'groupe_femmes', date: '25/07/2026' },
  { id: 3, nom: 'Essomba Guillaume', groupe: 'groupe_hommes', date: '18/07/2026' },
  { id: 4, nom: 'Ddjengue Larissa', groupe: 'groupe_jeunes', date: '22/07/2026' },
]

export const initialAutresCaisses = [
  { id: 1, nom: 'Travaux de toiture', montant: 1200000, date: '15/03/2025', caisse: 'basilique' },
  { id: 2, nom: 'Épargne mensuelle', montant: 350000, date: '20/06/2025', caisse: 'epargne' },
  { id: 3, nom: 'Renfort fondation', montant: 800000, date: '10/01/2024', caisse: 'basilique' },
  { id: 4, nom: 'Apport personnel', montant: 250000, date: '12/08/2024', caisse: 'epargne' },
]

export const paiementFilters = [
  { label: 'Dimes', value: 'dime' },
  { label: 'Offrandes', value: 'offrande' },
  { label: 'Actions de grâce', value: 'action de grace' },
]

export const formatCurrency = (value) => Number(value ?? 0).toLocaleString('fr-FR') + ' FCFA'
