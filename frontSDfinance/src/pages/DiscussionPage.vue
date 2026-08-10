<template>
  <!-- Page discussion (style discussion.html) -->
  <div class="contenu">
    <!-- Panneau gauche : liste des conversations -->
    <div class="panneau-gauche">
      <h3>Historique des conversations</h3>
      <div class="recherche">
        <input type="text" v-model="recherche" placeholder="Rechercher..." />
      </div>
      <div class="card-list">
        <div
          v-for="conv in conversationsFiltrees"
          :key="conv.id"
          class="card"
          :class="{ active: convActive?.id === conv.id }"
          @click="ouvrirConversation(conv)"
        >
          <div class="title">{{ conv.nom }}</div>
          <div class="bdy">{{ conv.apercu }}</div>
        </div>
      </div>
    </div>

    <!-- Zone de chat -->
    <div class="Discussion">
      <section class="chat">
        <header class="chat-header">
          <h4 id="nomDiscussion">{{ convActive ? convActive.nom : 'Sélectionnez une conversation' }}</h4>
        </header>

        <div class="messages" id="messages">
          <div
            v-for="(msg, i) in messagesActifs"
            :key="i"
            class="message"
            :class="msg.type"
          >
            {{ msg.texte }}
          </div>
        </div>

        <footer class="chat-footer">
          <input type="text" v-model="messageInput" placeholder="Écrire un message..." @keyup.enter="envoyerMessage" />
          <button class="btn" @click="envoyerMessage">Envoyer</button>
        </footer>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const recherche = ref('')
const messageInput = ref('')
const convActive = ref(null)

const conversations = ref([
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
])

const conversationsFiltrees = computed(() => {
  if (!recherche.value) return conversations.value
  return conversations.value.filter((c) =>
    c.nom.toLowerCase().includes(recherche.value.toLowerCase()),
  )
})

const messagesActifs = computed(() => convActive.value?.messages ?? [])

const ouvrirConversation = (conv) => {
  convActive.value = conv
}

const envoyerMessage = () => {
  if (!messageInput.value.trim() || !convActive.value) return
  convActive.value.messages.push({ texte: messageInput.value, type: 'envoye' })
  messageInput.value = ''
}
</script>

<style scoped>
.contenu {
  display: flex;
  height: calc(100vh - var(--header-h));
  gap: 10px;
  padding: 10px;
  background: inherit;
}

.panneau-gauche {
  width: 220px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex-shrink: 0;
}

.panneau-gauche h3 {
  text-decoration: underline;
  font-size: 0.95rem;
}

.recherche input {
  width: 100%;
  padding: 8px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-family: 'Poppins', sans-serif;
}

.card-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.card {
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 6px;
  padding: 10px 14px;
  cursor: pointer;
  transition: 0.3s;
}

.card:hover,
.card.active {
  background: var(--primary);
}

.title {
  color: black;
  font-weight: bold;
  margin-bottom: 4px;
}

.bdy {
  font-size: 0.85rem;
  color: #555;
}

/* chat */
.Discussion {
  flex: 1;
  background: white;
  border-radius: var(--radius);
  overflow: hidden;
}

.chat {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.chat-header {
  padding: 12px 16px;
  background: var(--primary);
}

.chat-header h4 {
  color: white;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background: #ece5dd;
  display: flex;
  flex-direction: column;
}

.message {
  max-width: 65%;
  padding: 10px;
  margin: 6px;
  border-radius: 10px;
}

.recu {
  background: var(--primary);
  align-self: flex-start;
}

.envoye {
  background: var(--primary-dark);
  color: white;
  align-self: flex-end;
}

.chat-footer {
  display: flex;
  padding: 10px;
  gap: 8px;
  border-top: 1px solid #eee;
}

.chat-footer input {
  flex: 1;
  padding: 8px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-family: 'Poppins', sans-serif;
}

.btn {
  border: none;
  padding: 8px 16px;
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
</style>
