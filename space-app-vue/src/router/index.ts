import { createRouter, createWebHistory } from 'vue-router'
import Landing from '../views/landing/index.vue'
import Login from '@/views/auth/Login.vue'
import Register from '@/views/auth/Register.vue'
import UserDashboard from '@/views/dashboard/user/index.vue'
import Account from '@/views/dashboard/user/account/index.vue'
import Reservations from '@/views/dashboard/user/reservations/index.vue'
import CreateReservation from '@/views/dashboard/user/create-reservation/index.vue'
import AdminDashboard from '@/views/dashboard/admin/index.vue'
import Spaces from '@/views/dashboard/admin/spaces/index.vue'
import CreateSpace from '@/views/dashboard/admin/create-space/index.vue'
import Users from '@/views/dashboard/admin/users/index.vue'
import { useAuthStore } from '@/stores/auth'
import NotFound from '@/views/error/NotFound.vue'
import Unauthorized from '@/views/error/Unauthorized.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Landing',
      component: Landing,
      meta: { requiresAuth: false },
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: { requiresAuth: false },
    },
    {
      path: '/register',
      name: 'Register',
      component: Register,
      meta: { requiresAuth: false },
    },
    {
      path: '/user',
      name: 'UserDashboard',
      component: UserDashboard,
      meta: { requiresAuth: true },
      redirect: '/user/reservations',
      children: [
        /*{
          path: 'account',
          name: 'Account',
          component: Account,
        },*/
        {
          path: 'reservations',
          name: 'Reservations',
          component: Reservations,
        },
        {
          path: 'reservation/create',
          name: 'CreateReservation',
          component: CreateReservation,
        },
      ],
    },
    {
      path: '/admin',
      name: 'AdminDashboard',
      component: AdminDashboard,
      meta: { requiresAuth: true, isAdmin: true },
      redirect: '/admin/spaces',
      children: [
        {
          path: 'spaces',
          name: 'Spaces',
          component: Spaces,
        },
        {
          path: 'space/create',
          name: 'CreateSpace',
          component: CreateSpace,
        },
        /*{
          path: 'users',
          name: 'Users',
          component: Users,
        },*/
      ],
    },
    {
      path: '/unauthorized',
      name: 'Unauthorized',
      component: Unauthorized,
    },
    {
      path: '/:catchAll(.*)',
      name: 'NotFound',
      component: NotFound,
    },
  ],
})

router.beforeEach((to) => {
  const { auth, isAdmin } = useAuthStore()

  const isLoggedIn = auth()
  if (to.path === '/login') {
    if (isLoggedIn) {
      if (isAdmin()) return { name: 'AdminDashboard' }
      else return { name: 'UserDashboard' }
    }
  }

  if (to.meta.requiresAuth) {
    if (!isLoggedIn) return { name: 'Login' }

    if (to.meta.isAdmin) {
      if (!isAdmin())
        return {
          name: 'Unauthorized',
        }
    } else {
      if (isAdmin())
        return {
          name: 'Unauthorized',
        }
    }
  }
})

export default router
