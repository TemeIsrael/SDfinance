import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { hasPermission } from '@/utils/permissions'

const routes = [
  {
    path: '/',
    redirect: { name: 'home' },
  },
  {
    path: '/connexion',
    component: () => import('@/pages/LoginPage.vue'),
    name: 'connexion',
  },
  {
    path: '/inscription',
    component: () => import('@/pages/InscriptionPage.vue'),
    name: 'inscription',
  },
  {
    path: '/accueil',
    component: () => import('@/pages/HomePage.vue'),
    name: 'home',
  },
  {
    path: '/membres',
    component: () => import('@/pages/AppLayout.vue'),
    meta: { requiresAuth: true, module: 'members' },
    children: [
      {
        path: '',
        component: () => import('@/pages/MembresPage.vue'),
        name: 'membres',
      },
    ],
  },
  {
    path: '/transactions',
    component: () => import('@/pages/AppLayout.vue'),
    meta: { requiresAuth: true, module: 'finance' },
    children: [
      {
        path: '',
        component: () => import('@/pages/TransactionsPage.vue'),
        name: 'transactions',
      },
    ],
  },
  {
    path: '/evenements',
    component: () => import('@/pages/AppLayout.vue'),
    meta: { requiresAuth: true, module: 'events' },
    children: [
      {
        path: '',
        component: () => import('@/pages/EvenementsPage.vue'),
        name: 'evenements',
      },
    ],
  },
  {
    path: '/discussion',
    component: () => import('@/pages/AppLayout.vue'),
    meta: { requiresAuth: true, module: 'messaging' },
    children: [
      {
        path: '',
        component: () => import('@/pages/DiscussionPage.vue'),
        name: 'discussion',
      },
    ],
  },
  {
    path: '/admin',
    component: () => import('@/pages/AppLayout.vue'), // ou AdminLayout si vous en créez un
    meta: { requiresAuth: true, module: 'users' },
    children: [
      {
        path: 'utilisateurs',
        component: () => import('@/pages/AdminUsersPage.vue'),
        name: 'admin-users',
      },
    ],
  },
  {
    path: '/contact',
    component: () => import('@/pages/ContactPage.vue'),
    name: 'contact',
  },
  {
    path: '/a-propos',
    component: () => import('@/pages/AProposPage.vue'),
    name: 'apropos',
  },
  {
    path: '/:pathMatch(.*)*',
    component: () => import('@/pages/NotFound.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  
  if (to.meta.requiresAuth) {
    if (!authStore.isAuthenticated.value) {
      return next({ name: 'connexion' })
    }
    
    if (to.meta.module) {
      const role = authStore.currentRole.value
      // We check if they have at least view access for the module
      const canAccess = hasPermission(role, to.meta.module, 'canView') || 
                        hasPermission(role, to.meta.module, 'canViewGlobal') ||
                        hasPermission(role, to.meta.module, 'canManage') ||
                        hasPermission(role, to.meta.module, 'canUse') ||
                        hasPermission(role, to.meta.module, 'canViewPersonal');
                        
      if (!canAccess) {
        // Rediriger vers une route par défaut si pas accès
        if (role === 'ADMIN') return next({ name: 'admin-users' })
        if (role === 'LEADER') return next({ name: 'membres' })
        return next({ name: 'home' })
      }
    }
  }
  next()
})

export default router
