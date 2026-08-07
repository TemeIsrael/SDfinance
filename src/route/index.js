import { createRouter, createWebHistory } from 'vue-router'

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
    children: [
      {
        path: '',
        component: () => import('@/pages/DiscussionPage.vue'),
        name: 'discussion',
      },
    ],
  },
  {
    path: '/app',
    component: () => import('@/pages/AppLayout.vue'),
    redirect: { name: 'membres' },
    children: [
      {
        path: 'membres',
        component: () => import('@/pages/MembresPage.vue'),
        name: 'membres-app',
      },
      {
        path: 'transactions',
        component: () => import('@/pages/TransactionsPage.vue'),
        name: 'transactions-app',
      },
      {
        path: 'evenements',
        component: () => import('@/pages/EvenementsPage.vue'),
        name: 'evenements-app',
      },
      {
        path: 'discussion',
        component: () => import('@/pages/DiscussionPage.vue'),
        name: 'discussion-app',
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

export default router
