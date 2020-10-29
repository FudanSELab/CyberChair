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
    // {
    //   path: '/test',
    //   component: () => import('@/views/dashboard/cyberchairComponents/post')
    // },
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
          name: 'PcMember Invitation',
          path: '/pages/pcminvite',
          component: () => import('@/views/dashboard/invitation')
        },
        {
          name: 'Notifications',
          path: '/pages/notifications',
          component: () => import('@/views/dashboard/notification')
        },
        {
          name: 'essay submission',
          path: '/essaySubmission',
          component: () => import('@/views/dashboard/essaySubmission'),
        },

        {
          name: 'Essay Detail',
          path: '/essaydetail',
          component: () => import('@/views/dashboard/EssayDetail'),
        },

        {
          name: 'Review Detail',
          path: '/reviewDetail',
          component: () => import('@/views/dashboard/reviewDetail'),
        },

        {
          name: 'Essay Submission List',
          path: '/submission/list',
          component: () => import('@/views/dashboard/SubmissionEssayList'),
        },

        {
          name: 'Review Articles',
          path: '/meeting/reviewArticles',
          component: () => import('@/views/dashboard/ReviewList'),
        },

        {
          name: 'Review Article',
          path: '/meeting/reviewArticle',
          component: () => import('@/views/dashboard/ReviewArticle'),
        },

        {
          name: 'Conference Detail',
          path: '/pages/chairMeeting',
          component: () => import('@/views/dashboard/conferenceDetail')
        },
        {
          name: 'post test',
          path: '/post/test',
          component: () => import('@/views/dashboard/cyberchairComponents/post')
        },
        {
          name: 'discussion',
          path: '/article/discussion',
          component: () => import('@/views/dashboard/PostDisplay')
          //http://localhost/#/article/discussion?posterName=qwert1&articleId=42&reviewStatus=alreadyReviewed
        },
        // {
        //   name: 'temp test',
        //   path: '/temp',
        //   component: () => import('@/views/temp_test')
        // }
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
      // console.log('case2')
      next({
        path: '/login',
        query: {redirect: to.fullPath} // 登录成功之后重新跳转到该路由
      })
    }
  } else {
    next()
  }
})
