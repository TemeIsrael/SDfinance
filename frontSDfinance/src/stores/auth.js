import { reactive, computed } from 'vue';

// On utilise reactive pour créer un store simple partagé dans toute l'application Vue
const state = reactive({
  user: null, // { username: string, role: string, groupeIds: array }
  token: null,
  activeGroupeId: null, // L'ID du groupe actuellement sélectionné dans le header
});

// Charger l'état depuis le localStorage au démarrage
const savedUser = localStorage.getItem('user');
const savedToken = localStorage.getItem('token');
const savedActiveGroupeId = localStorage.getItem('activeGroupeId');

if (savedUser && savedToken) {
  state.user = JSON.parse(savedUser);
  state.token = savedToken;
  if (savedActiveGroupeId) {
    state.activeGroupeId = Number(savedActiveGroupeId);
  } else if (state.user.groupeIds && state.user.groupeIds.length > 0) {
    state.activeGroupeId = state.user.groupeIds[0];
  }
}

export const useAuthStore = () => {
  const login = (userData, token) => {
    state.user = userData;
    state.token = token;
    
    // Par défaut, sélectionner le premier groupe si applicable
    if (userData.groupeIds && userData.groupeIds.length > 0) {
      state.activeGroupeId = userData.groupeIds[0];
      localStorage.setItem('activeGroupeId', state.activeGroupeId);
    }
    
    localStorage.setItem('user', JSON.stringify(userData));
    localStorage.setItem('token', token);
  };

  const logout = () => {
    state.user = null;
    state.token = null;
    state.activeGroupeId = null;
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    localStorage.removeItem('activeGroupeId');
  };

  const setActiveGroupeId = (groupeId) => {
    if (state.user?.groupeIds?.includes(Number(groupeId))) {
      state.activeGroupeId = Number(groupeId);
      localStorage.setItem('activeGroupeId', state.activeGroupeId);
    }
  };

  const isAuthenticated = computed(() => !!state.token);
  const currentUser = computed(() => state.user);
  const currentRole = computed(() => state.user?.role);
  const activeGroupeId = computed(() => state.activeGroupeId);
  const userGroups = computed(() => state.user?.groupeIds || []);

  return {
    state,
    login,
    logout,
    setActiveGroupeId,
    isAuthenticated,
    currentUser,
    currentRole,
    activeGroupeId,
    userGroups
  };
};
