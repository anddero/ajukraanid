import Vue from 'vue';
import Vuex from 'vuex';

import actions from './actions';
import mutations from './mutations';

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    requestDestination: "http://18.220.101.130:8080",
    username: "",
    gameCode: 0,
    questionNumber: 0,
    lastQuestion: "",
    paths: {
      gameState: "menu",
      registrations: [],
      index: '/~nilill/tarkvaratehnika/',
      about: '/~nilill/tarkvaratehnika/about',
      registration: '/~nilill/tarkvaratehnika/registration',
      menu: '/~nilill/tarkvaratehnika//menu',
      question: '/~nilill/tarkvaratehnika/question',
      confirmation: '/~nilill/tarkvaratehnika/newGameConfirmation',
      lobby: '/~nilill/tarkvaratehnika/lobby',
      awarding: '/~nilill/tarkvaratehnika/results',
      chooseBestAnswer: '/~nilill/tarkvaratehnika/grading',
      waitingScreen1: '/~nilill/tarkvaratehnika/waitingScreen1',
      waitingScreen2: '/~nilill/tarkvaratehnika/waitingScreen2'
    }
  },

  mutations,
  actions
});
