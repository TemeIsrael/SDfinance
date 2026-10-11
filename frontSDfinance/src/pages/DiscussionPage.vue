<template>
  <!-- Page discussion (style discussion.html) -->
  <div class="contenu">


    <!-- Zone de chat -->
    <div class="Discussion">
      <section class="chat">
        <header class="chat-header">
          <h4 id="nomDiscussion">
            {{ activeDiscussion ? getDiscussionName(activeDiscussion) : '' }}
          </h4>
        </header>

        <div v-if="activeDiscussion">
          <div class="messages" id="messages">
            <div
              v-for="(msg, i) in activeDiscussionMessages"
              :key="msg.id || i"
              class="message"
              :class="msg.senderUsername === currentUser?.username ? 'envoye' : 'recu'"
            >
              {{ msg.content }}
              <div style="font-size:0.7em; opacity:0.7; text-align:right; margin-top:4px;">
                {{ msg.sentAt ? new Date(msg.sentAt).toLocaleTimeString() : '' }}
              </div>
            </div>
          </div>
          <footer class="chat-footer">
            <input type="text" v-model="messageInput" placeholder="Écrire un message..." @keyup.enter="envoyerMessage" :disabled="!activeDiscussion || !isConnected" />
            <button class="btn" @click="envoyerMessage" :disabled="!activeDiscussion || !isConnected">Envoyer</button>
          </footer>
        </div>
        <div v-else class="empty" style="padding:20px; color:#000; text-align:center;">
          Sélectionnez une conversation dans la liste à gauche
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useChat } from '@/composable/useChat'
import { useAuthStore } from '@/stores/auth'

const { state, currentUser } = useAuthStore()
const token = state.token

const {
  discussions,
  activeDiscussionMessages,
  activeDiscussion,
  isConnected,
  fetchDiscussions,
  connect,
  disconnect,
  selectDiscussion,
  sendMessage: sendChatMsg,
  initGlobalDiscussion
} = useChat()

const recherche = ref('')
const messageInput = ref('')
const nouveauContact = ref('')

const conversationsFiltrees = computed(() => {
  if (!recherche.value) return discussions.value
  return discussions.value.filter((c) => {
    const nom = c.counterpartUsername || c.ownerUsername || 'Global'
    return nom.toLowerCase().includes(recherche.value.toLowerCase())
  })
})

onMounted(async () => {
  if (token) {
    await fetchDiscussions(token)
    await initGlobalDiscussion(token)
    connect(token)
  }
})

onUnmounted(() => {
  disconnect()
})

const getDiscussionName = (conv) => {
  return 'Discussion'
}

const ouvrirConversation = (conv) => {
  selectDiscussion(conv, token)
}

const envoyerMessage = () => {
  if (!messageInput.value.trim() || !activeDiscussion.value) return
  sendChatMsg(messageInput.value)
  messageInput.value = ''
}

// La création de nouvelles discussions individuelles n'est plus nécessaire avec la discussion globale.
// Fonction conservée pour compatibilité éventuelle, mais ne fait rien.
const creerDiscussion = async () => {
  // No‑op for global discussion
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
  background: #EDCDAF;
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
