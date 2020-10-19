// =========================================================
// * Vuetify Material Dashboard - v2.1.0
// =========================================================
//
// * Product Page: https://www.creative-tim.com/product/vuetify-material-dashboard
// * Copyright 2019 Creative Tim (https://www.creative-tim.com)
//
// * Coded by Creative Tim
//
// =========================================================
//
// * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

import Vue from 'vue'
import App from './App.vue'
import {router} from './router'
import store from './store'
import './plugins/base'
import './plugins/chartist'
import './plugins/vee-validate'
import './plugins/vuetifyToastSnackbar'
import vuetify from './plugins/vuetify'
import i18n from './i18n'
import 'material-design-icons-iconfont/dist/material-design-icons.css'
import 'vuetify/dist/vuetify.min.css'
Vue.config.productionTip = false
Vue.use(vuetify)

//axios 配置
var axios = require('axios')
// Axios挂载到prototype，全局可以使用this.$axios访问
Vue.prototype.$axios = axios
axios.defaults.baseURL = ''
// axios.defaults.withCredentials = true
axios.defaults.headers.post['Content-Type'] = "application/json;charset=UTF-8"

Vue.config.productionTip = false

// http request 拦截器
axios.interceptors.request.use(
  config => {
    console.log(config);
    var site = config.url.split('http://127.0.0.1:8080/');
    if(localStorage.token && (site.length==1 || (site.length==2 && (site[1]!='login' && site[1]!='register')))) {
      // 判断是否有token，若存在，每个http header加上token
      config.headers.Authorization = 'Bearer ' + localStorage.token;
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// http response 拦截器
axios.interceptors.response.use(
  response => {
    return response
  },
  error => {
    // console.log(error)
    // console.log(error.response)
    if(error) {
      // 清除token 如果不是register/login, 跳转至login
      store.commit('logout')
      router.currentRoute.path !== '/login' &&
      router.currentRoute.path !== '/register' &&
      router.replace({
        path: '/login',
        query: { redirect: router.currentRoute.path }
      })
    }
    return Promise.reject(error.response.data)
  }
)

new Vue({
  router,
  store,
  vuetify,
  i18n,
  render: h => h(App),
}).$mount('#app')
