<template>
  <!-- Page contacts (style contacts.html) -->
  <div class="page-contact">
    <div class="debut">
      <h1>SDfinance</h1>
    </div>
    <h2>Nous contacter</h2>
    <p>Une question, un problème, ou un besoin d'information ? Nous sommes là pour vous aider.</p>
    <h3>Informations de contacts</h3>

   <table>
    <thead>
      <tr>
        <th class="diagonale">
          <span class="bas_gauche">nom et prenom</span>
          <span class="haute_droite">Informations</span>
        </th>
        <th>téléphnoe</th>
        <th>adresse mail</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>Zao Zao </td>
        <td>699735108</td>
        <td></td>
     </tr>
     <tr>
        <td>Kouzaita</td>
        <td>699886055</td>
        <td></td>
     </tr>
     <tr>
        <td>WANGUE samuel (groupr des hommes)</td>
        <td>681609771</td>
        <td>Wanguesamuel13@gmail.com</td>
     </tr>
     <tr>
        <td>KEDE artur (groupe des hommes)</td>
        <td>696911636</td>
        <td>kedembargaartursylver1988@gmail.com</td>
     </tr>
     <tr>
        <td>BAKALE (groupe des femmes)</td>
        <td>691707077</td>
        <td>sagessedivine2017@gmail.com</td>
     </tr>
     <tr>
        <td>mado (gropue des femmes)</td>
        <td>699656535</td>
        <td>ngbinyet@gmail.com</td>
     </tr>
     <tr>
        <td>NDJENGUE larissa (groupe des jeunes)</td>
        <td>687311957</td>
        <td>Ndjengueelisabeth72@gmail.com</td>
     </tr>
     <tr>
        <td>EBENE emmanuel (groupe des jeunes)</td>
        <td>686610475</td>
        <td>emmanuellaureemmanuellaure@gmail.com</td>
     </tr>
     <tr>
        <td>ESSAKA marcel (en cas de soucis technique)</td>
        <td>683024277</td>
        <td>marcelthegoaty@gmail.com</td>
     </tr>
     <tr>
        <td>TEME israel (en cas de souis technique)</td>
        <td></td>
        <td>temeisrael65@gmail.com</td>
     </tr>
  </tbody>
   </table>

    <div class="retour">
      <RouterLink to="/accueil" class="btn-retour">Retour à l'accueil</RouterLink>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
.page-contact {
  min-height: 100vh;
  background: linear-gradient(135deg, white, var(--primary));
  font-family: 'Poppins', sans-serif;
  padding-bottom: 40px;
}

.debut {
  background: white;
  margin: 0 auto;
  padding: 8px 20px;
}

h1 {
  margin: 0;
  color: var(--primary-dark);
}

h2,
h3,
h4 {
  margin: 10px;
  text-align: center;
}

p {
  margin: 10px 30px;
  text-align: center;
  color: #555;
}
.table {
  width: 100%;
  background: white;
  margin: 6px 0;
  border: 1px solid #ccc;
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

.diagonale{
  background:white;
  position:relative;
  min-width:150px;
  height:60px
}
.diagonale .bas_gauche{
  position:absolute;
  bottom:5px;
  left:5px
}
.diagonale .haute_droite{
  position:absolute;
  top:5px;
  right:5px
}
.retour {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

.btn-retour {
  display: inline-block;
  padding: 12px 28px;
  background: var(--primary);
  color: white;
  border: none;
  border-radius: var(--radius);
  cursor: pointer;
  transition: all 0.5s;
  font-family: 'Poppins', sans-serif;
  text-decoration: none;
  font-weight: 600;
}

.btn-retour:hover {
  transform: scale(1.1);
  background: black;
}
</style>
