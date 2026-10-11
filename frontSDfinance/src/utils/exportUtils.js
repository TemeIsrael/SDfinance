/**
 * Service d'exportation pour l'application SDfinance.
 * Prise en charge de l'export CSV (compatible Excel avec BOM UTF-8) et de l'impression PDF.
 */

/**
 * Nettoie une cellule pour le format CSV (gestion des guillemets et séparateurs).
 * @param {any} val 
 * @returns {string}
 */
function escapeCsvCell(val) {
  if (val === null || val === undefined) return ''
  let str = String(val).trim()
  if (str.includes(';') || str.includes('"') || str.includes('\n') || str.includes(',')) {
    str = '"' + str.replace(/"/g, '""') + '"'
  }
  return str
}

/**
 * Exporte un tableau de données en fichier CSV avec encodage UTF-8 BOM.
 * @param {Object} options
 * @param {string} options.filename - Nom du fichier (ex: export_transactions.csv)
 * @param {Array<string>} options.headers - Entêtes des colonnes (ex: ['Description', 'Montant', 'Date'])
 * @param {Array<Array<any>>} options.rows - Lignes de données
 * @param {string} [options.delimiter=';'] - Séparateur (point-virgule par défaut pour Excel FR)
 */
export function exportToCSV({ filename = 'export.csv', headers = [], rows = [], delimiter = ';' }) {
  if (!rows || !rows.length) {
    alert('Aucune donnée à exporter.')
    return
  }

  const csvContent = []
  
  // Entêtes
  if (headers && headers.length) {
    csvContent.push(headers.map(escapeCsvCell).join(delimiter))
  }

  // Lignes
  rows.forEach(row => {
    csvContent.push(row.map(escapeCsvCell).join(delimiter))
  })

  const fullCsv = csvContent.join('\r\n')
  
  // BOM UTF-8 (\uFEFF) pour forcer Excel à lire le fichier en UTF-8 (accents français corrects)
  const blob = new Blob(['\uFEFF' + fullCsv], { type: 'text/csv;charset=utf-8;' })
  
  const link = document.createElement('a')
  const url = URL.createObjectURL(blob)
  link.setAttribute('href', url)
  link.setAttribute('download', filename.endsWith('.csv') ? filename : `${filename}.csv`)
  link.style.visibility = 'hidden'
  
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(url)
}

/**
 * Génère une vue imprimable / PDF complète avec en-tête SDfinance, logo, date et tableau.
 * @param {Object} options
 * @param {string} options.title - Titre du document (ex: Rapport Financier)
 * @param {string} [options.subtitle] - Sous-titre ou filtres appliqués
 * @param {Array<string>} options.headers - Entêtes de colonnes
 * @param {Array<Array<any>>} options.rows - Données
 * @param {Array<{label: string, value: string}>} [options.summary] - Résumé / Totaux au pied du document
 */
export function exportToPDF({ title = 'Rapport SDfinance', subtitle = '', headers = [], rows = [], summary = [] }) {
  if (!rows || !rows.length) {
    alert('Aucune donnée à imprimer ou exporter.')
    return
  }

  const currentDate = new Date().toLocaleDateString('fr-FR', {
    day: '2-digit',
    month: 'long',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })

  const printWindow = window.open('', '_blank', 'width=900,height=700')
  if (!printWindow) {
    alert('Le navigateur a bloqué la fenêtre d\'impression. Veuillez autoriser les popups.')
    return
  }

  const tableHeadersHtml = headers.map(h => `<th>${escapeHtml(h)}</th>`).join('')
  const tableRowsHtml = rows
    .map(row => `<tr>${row.map(cell => `<td>${escapeHtml(String(cell ?? ''))}</td>`).join('')}</tr>`)
    .join('')

  const summaryHtml = summary && summary.length > 0
    ? `<div class="summary-box">
        ${summary.map(s => `<div class="summary-item"><strong>${escapeHtml(s.label)} :</strong> ${escapeHtml(String(s.value))}</div>`).join('')}
       </div>`
    : ''

  const html = `
    <!DOCTYPE html>
    <html lang="fr">
    <head>
      <meta charset="UTF-8">
      <title>${escapeHtml(title)} - SDfinance</title>
      <style>
        body {
          font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
          margin: 30px;
          color: #1e293b;
          font-size: 13px;
        }
        .header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          border-bottom: 3px solid #1e3a8a;
          padding-bottom: 15px;
          margin-bottom: 20px;
        }
        .logo-area h1 {
          margin: 0;
          color: #1e3a8a;
          font-size: 24px;
          font-weight: 800;
          letter-spacing: -0.5px;
        }
        .logo-area p {
          margin: 3px 0 0 0;
          color: #64748b;
          font-size: 12px;
        }
        .meta-area {
          text-align: right;
          font-size: 12px;
          color: #475569;
        }
        .doc-title {
          font-size: 18px;
          font-weight: bold;
          margin-bottom: 4px;
          color: #0f172a;
        }
        .doc-subtitle {
          font-size: 13px;
          color: #64748b;
          margin-bottom: 16px;
        }
        table {
          width: 100%;
          border-collapse: collapse;
          margin-top: 15px;
          margin-bottom: 20px;
        }
        th {
          background-color: #1e3a8a;
          color: #ffffff;
          padding: 10px 8px;
          text-align: left;
          font-weight: 600;
          font-size: 12px;
          border: 1px solid #1e3a8a;
        }
        td {
          padding: 8px;
          border: 1px solid #cbd5e1;
          font-size: 12px;
        }
        tr:nth-child(even) {
          background-color: #f8fafc;
        }
        .summary-box {
          margin-top: 20px;
          background: #f1f5f9;
          border: 1px solid #cbd5e1;
          border-radius: 6px;
          padding: 12px 16px;
          display: flex;
          gap: 24px;
          justify-content: flex-end;
          flex-wrap: wrap;
        }
        .summary-item {
          font-size: 13px;
          color: #0f172a;
        }
        .footer {
          margin-top: 40px;
          border-top: 1px solid #e2e8f0;
          padding-top: 10px;
          font-size: 11px;
          color: #94a3b8;
          text-align: center;
        }
        @media print {
          body { margin: 15px; }
          .no-print { display: none; }
          th { -webkit-print-color-adjust: exact; print-color-adjust: exact; }
          tr:nth-child(even) { -webkit-print-color-adjust: exact; print-color-adjust: exact; }
        }
      </style>
    </head>
    <body>
      <div class="header">
        <div class="logo-area">
          <h1>SDfinance</h1>
          <p>Système De Gestion Financière et Communautaire</p>
        </div>
        <div class="meta-area">
          <div><strong>Généré le :</strong> ${currentDate}</div>
        </div>
      </div>

      <div class="doc-title">${escapeHtml(title)}</div>
      ${subtitle ? `<div class="doc-subtitle">${escapeHtml(subtitle)}</div>` : ''}

      <table>
        <thead>
          <tr>${tableHeadersHtml}</tr>
        </thead>
        <tbody>
          ${tableRowsHtml}
        </tbody>
      </table>

      ${summaryHtml}

      <div class="footer">
        Document officiel généré automatiquement depuis l'application SDfinance.
      </div>

      <script>
        window.onload = function() {
          setTimeout(function() {
            window.print();
          }, 300);
        };
      </script>
    </body>
    </html>
  `

  printWindow.document.open()
  printWindow.document.write(html)
  printWindow.document.close()
}

function escapeHtml(str) {
  return String(str ?? '')
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;')
}
