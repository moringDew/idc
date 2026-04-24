import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/document',
    name: 'Document',
    component: () => import('@/views/Document.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/approval',
    name: 'Approval',
    component: () => import('@/views/Approval.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/knowledge',
    name: 'Knowledge',
    component: () => import('@/views/Knowledge.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/meeting',
    name: 'Meeting',
    component: () => import('@/views/Meeting.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/monitor',
    name: 'Monitor',
    component: () => import('@/views/Monitor.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/settings',
    name: 'Settings',
    component: () => import('@/views/Settings.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

const isLoggedIn = () => {
  const user = localStorage.getItem('user')
  return user !== null && user !== 'undefined'
}

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth && !isLoggedIn()) {
    next('/')
  } else if (to.path === '/' && isLoggedIn()) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router