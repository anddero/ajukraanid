import Vue from 'vue';
import Vuex from 'vuex';

import actions from './actions';
import mutations from './mutations';
Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    requestDestination: "http://localhost:8080",
    username: "",
    gameCode: 0,
    questionNumber: 0,
    gameState: "menu",
    registrations: [],
  },
  mutations,
  actions
});
