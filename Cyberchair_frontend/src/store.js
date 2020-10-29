import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    barColor: 'rgba(0, 0, 0, .8), rgba(0, 0, 0, .8)',
    barImage: 'https://demos.creative-tim.com/material-dashboard/assets/img/sidebar-1.jpg',
    drawer: null,
    token: localStorage.getItem('token') || null,
    userDetails: localStorage.getItem('userDetails') || null,
    conferences: [],
  },
  mutations: {
    SET_BAR_IMAGE (state, payload) {
      state.barImage = payload
    },
    SET_DRAWER (state, payload) {
      state.drawer = payload
    },
    login(state, data){
      // console.log(data.responseBody.token)
      localStorage.setItem('token', data.responseBody.token)
      localStorage.setItem('username', data.responseBody.username)
      // localStorage.setItem('userDetails', data.userDetails)
      // state.user = data.userDetails
      state.token = data.responseBody.token
      state.username = data.responseBody.username
    },
    logout(state) {
      // 移除token
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      state.token = null
      state.username = null
    },
    addConference(state, data){
      console.log(data)
      state.conferences.push({
        meetingName: data.meetingName,
        acronym: data.acronym,
        date: data.date,
        location: {
          region: data.region,
          city: data.city,
          venue: data.venue,
        },
        area: {
          primaryArea: data.primaryArea,
          secondaryArea: data.secondaryArea,
          areaNotes: data.areaNotes,
        },
        organizer: data.organizer,
        webPage: data.webPage,
        role: 'chair',
        state: 'Review Completed',
      })
    }
  },
  actions: {

  },
})
