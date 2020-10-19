import Vue from 'vue'
import Router from 'vue-router'
import store from './store'
Vue.use(Router)

export const router = new Router({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '',
      redirect: '/dashboard',
      meta: {
        requireAuth: true // 需要登录权限
      },
    },
    {
      path: '/login',
      component: () => import('@/views/Login')
    },
    {
      path: '/register',
      component: () => import('@/views/Register')
    },
    {
      path: '/dashboard',
      component: () => import('@/views/dashboard/Index'),
      meta: {
        requireAuth: true // 需要登录权限
      },
      children: [
        // DashboardAdmin
        {
          name: 'DashboardAdmin',
          path: '/dashboardAdmin',
          component: () => import('@/views/dashboard/DashboardAdmin'),
        },
        // Dashboard
        {
          name: 'Dashboard',
          path: '/dashboard',
          component: () => import('@/views/dashboard/Dashboard'),
        },
        // Addmeeting
        {
          name: 'AddMeeting',
          path: '/Addmeeting',
          component: () => import('@/views/dashboard/conferenceApplier')
        },
        // Pages
        {
          name: 'User Profile',
          path: '/pages/user',
          component: () => import('@/views/dashboard/pages/UserProfile'),
        },

        {
          name: 'Notifications',
          path: '/pages/notifications',
          component: () => import('@/views/dashboard/notification')
        },
        
        {
          name: 'NewSecretInfo',
          path: '/newsecret',
          component: () => import('@/views/dashboard/BMBComponents/secretFileInfo')
        },
        {
          name: 'newDistribution',
          path: '/newdistribution',
          component: () => import('@/views/dashboard/BMBComponents/newDistribution')
        }
      ],
    },
  ],
})

// 前端登录拦截
router.beforeEach(function (to, from ,next) {
  if (to.matched.some(record => record.meta.requireAuth)) {
    // console.log('lanjie' + store.state.token)
    if (store.state.token != "undefined" && store.state.token != null) {
      next()
    } else {
      console.log('case2')
      next({
        path: '/login',
        query: {redirect: to.fullPath} // 登录成功之后重新跳转到该路由
      })
    }
  } else {
    next()
  }
})
