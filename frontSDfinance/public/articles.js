// Données SDfinance – Membres, Transactions, Caisses
const membres = [
  { id: 1, nom: 'Teme israel', montant: 1000, travailleur: 'non', date: '01/07/2026' },
  { id: 2, nom: 'Evina Brice', montant: 1000, travailleur: 'non', date: '25/07/2026' },
  { id: 3, nom: 'Essomba Guillaume', montant: 1000, travailleur: 'non', date: '18/07/2026' },
  { id: 4, nom: 'Ddjengue Larissa', montant: 5000, travailleur: 'oui', date: '22/07/2026' },
]

const transactions = [
  { id: 1, description: 'retrait', montant: 300000, motif: 'fête des récoltes', date: '01.07.2026' },
  { id: 2, description: 'depot', montant: 50000, motif: 'Dîmes', date: '09.05.2026' },
]

const caisses = [
  { id: 'offrandes', nom: "Caisse de l'église", solde: '1 850 000 FCFA' },
  { id: 'epargne', nom: 'Épargnes personnelles', solde: '4 200 000 FCFA' },
  { id: 'basilique', nom: 'Construction Basilique', solde: '12 500 000 FCFA' },
  { id: 'fonctionnement', nom: 'Fonctionnement', solde: '850 000 FCFA' },
  { id: 'temporaire', nom: 'Caisses temporaires', solde: '4 caisses ouvertes' },
]

const conversations = [
  {
    id: 1,
    nom: 'Jean Dupont',
    apercu: 'Bonjour Président...',
    messages: [
      { texte: 'Bonjour Président', type: 'recu' },
      { texte: 'Bonjour Jean', type: 'envoye' },
      { texte: 'Comment allez-vous ?', type: 'recu' },
    ],
  },
  {
    id: 2,
    nom: 'Paul',
    apercu: 'Merci beaucoup...',
    messages: [
      { texte: 'Merci beaucoup', type: 'recu' },
      { texte: 'Avec plaisir', type: 'envoye' },
    ],
  },
]
