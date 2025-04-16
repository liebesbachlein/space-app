import { createRouter, createWebHistory } from 'vue-router'
import LandingPage from '../views/landing/index.vue'
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

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'LandingPage',
      component: LandingPage,
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
    },
    {
      path: '/register',
      name: 'Register',
      component: Register,
    },
    {
      path: '/user',
      redirect: '/user/reservations',
    },
    {
      path: '/user',
      name: 'UserDashboard',
      component: UserDashboard,
      children: [
        {
          path: 'account',
          name: 'Account',
          component: Account,
        },
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
      redirect: '/admin/spaces',
    },
    {
      path: '/admin',
      name: 'AdminDashboard',
      component: AdminDashboard,
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
        {
          path: 'users',
          name: 'Users',
          component: Users,
        },
      ],
    },
  ],
})

export default router
