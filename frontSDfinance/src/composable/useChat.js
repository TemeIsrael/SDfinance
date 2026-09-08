import { ref } from 'vue';

const discussions = ref([]);
const activeDiscussionMessages = ref([]);
const activeDiscussion = ref(null);
const socket = ref(null);
const isConnected = ref(false);
let currentSubscriptionId = null;

// Simple STOMP Frame Parser
function parseFrame(data) {
  const [headerSection, bodySection] = data.split('\n\n');
  if (!headerSection) return null;
  const lines = headerSection.split('\n');
  const command = lines[0].trim();
  const headers = {};
  for (let i = 1; i < lines.length; i++) {
    const line = lines[i].trim();
    if (line) {
      const idx = line.indexOf(':');
      if (idx !== -1) {
        const key = line.substring(0, idx).trim();
        const val = line.substring(idx + 1).trim();
        headers[key] = val;
      }
    }
  }
  let body = bodySection || '';
  if (body.endsWith('\u0000')) {
    body = body.slice(0, -1);
  }
  return { command, headers, body };
}

// Simple STOMP Frame Builder
function buildFrame(command, headers, body = '') {
  let frame = `${command}\n`;
  for (const [key, val] of Object.entries(headers)) {
    frame += `${key}:${val}\n`;
  }
  frame += `\n${body}\u0000`;
  return frame;
}

export const useChat = () => {

  const fetchDiscussions = async (token) => {
    try {
      const res = await fetch('/api/messaging/discussions', {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (res.ok) {
        discussions.value = await res.json();
      }
    } catch (err) {
      console.error('Failed to fetch discussions:', err);
    }
  };

  const connect = (token) => {
    if (socket.value && (socket.value.readyState === WebSocket.OPEN || socket.value.readyState === WebSocket.CONNECTING)) {
      return;
    }

    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    // Standard websocket upgrade URL mapped by Nginx location /ws/
    const wsUrl = `${protocol}//${window.location.host}/ws/websocket`;
    socket.value = new WebSocket(wsUrl);

    socket.value.onopen = () => {
      // Send STOMP CONNECT frame
      const frame = buildFrame('CONNECT', {
        'accept-version': '1.1,1.2',
        'heart-beat': '10000,10000',
        'Authorization': `Bearer ${token}`
      });
      socket.value.send(frame);
    };

    socket.value.onmessage = (event) => {
      const frame = parseFrame(event.data);
      if (!frame) return;

      if (frame.command === 'CONNECTED') {
        isConnected.value = true;
        console.log('STOMP Connected successfully!');
        if (activeDiscussion.value) {
          subscribeToDiscussion(activeDiscussion.value.id);
        }
      } else if (frame.command === 'MESSAGE') {
        try {
          const msg = JSON.parse(frame.body);
          activeDiscussionMessages.value.push(msg);
          
          const d = discussions.value.find(item => item.id === activeDiscussion.value?.id);
          if (d) {
            d.lastMessagePreview = msg.senderUsername + ": " + msg.content;
          }
        } catch (e) {
          console.error('Failed to parse message body:', e);
        }
      }
    };

    socket.value.onclose = () => {
      isConnected.value = false;
      console.log('STOMP Connection closed.');
    };

    socket.value.onerror = (err) => {
      console.error('WebSocket error:', err);
    };
  };

  const disconnect = () => {
    if (socket.value) {
      socket.value.close();
      socket.value = null;
    }
    isConnected.value = false;
  };

  const subscribeToDiscussion = (discussionId) => {
    if (!socket.value || socket.value.readyState !== WebSocket.OPEN) return;

    if (currentSubscriptionId) {
      const unsub = buildFrame('UNSUBSCRIBE', { id: currentSubscriptionId });
      socket.value.send(unsub);
    }

    currentSubscriptionId = `sub-${discussionId}`;
    const sub = buildFrame('SUBSCRIBE', {
      id: currentSubscriptionId,
      destination: `/topic/discussion/${discussionId}`
    });
    socket.value.send(sub);
  };

  const selectDiscussion = async (discussion, token) => {
    activeDiscussion.value = discussion;
    // If the discussion object already contains a `messages` array (mock data), use it directly.
    if (discussion.messages && Array.isArray(discussion.messages)) {
      activeDiscussionMessages.value = discussion.messages;
    } else {
      activeDiscussionMessages.value = [];
      try {
        const res = await fetch(`/api/messaging/discussions/${discussion.id}/messages`, {
          headers: { 'Authorization': `Bearer ${token}` }
        });
        if (res.ok) {
          activeDiscussionMessages.value = await res.json();
        }
      } catch (err) {
        console.error('Failed to load messages history:', err);
      }
    }
    // Subscribe to live updates for this discussion.
    subscribeToDiscussion(discussion.id);
  };

  const sendMessage = (content) => {
    if (!isConnected.value || !socket.value || !activeDiscussion.value) return;

    const payload = JSON.stringify({
      discussionId: activeDiscussion.value.id,
      content: content
    });

    const frame = buildFrame('SEND', {
      destination: '/app/chat.send',
      'content-type': 'application/json'
    }, payload);

    socket.value.send(frame);
  };

  const startNewDiscussion = async (counterpartUsername, token) => {
    try {
      const res = await fetch('/api/messaging/discussions', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ counterpartUsername })
      });
      if (res.ok) {
        const newDiscussion = await res.json();
        const exists = discussions.value.find(d => d.id === newDiscussion.id);
        if (!exists) {
          discussions.value.unshift(newDiscussion);
        }
        await selectDiscussion(newDiscussion, token);
      }
    } catch (err) {
      console.error('Failed to create/fetch discussion:', err);
    }
  };

  return {
    discussions,
    activeDiscussionMessages,
    activeDiscussion,
    isConnected,
    fetchDiscussions,
    connect,
    disconnect,
    selectDiscussion,
    sendMessage,
    startNewDiscussion
  };
};
