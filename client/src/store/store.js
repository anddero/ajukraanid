import Vue from 'vue'
import Vuex from 'vuex'

import actions from './actions'
import mutations from './mutations'

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {
    Authorization: '',
    requestDestination: process.env.requestDestination,
    adminDestination: process.env.adminDestination,
    loginDestination: process.env.loginDestination,
    username: '',
    gameCode: 0,
    questionNumber: 0,
    lastQuestion: '',
    registrations: [],
    token: ""
  },
  mutations,
  actions
})
