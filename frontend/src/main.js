import Vue from 'vue'
import VueRouter from 'vue-router'
import vueResource from 'vue-resource'
import About from "./components/staticPages/About.vue"
import Question from "./components/pagesThatNeedToBeInterValled/Question.vue"
import Menu from "./components/staticPages/Menu.vue"
import JoinGame from "./components/staticPages/JoinGame.vue"
import NewGameConfirmation from "./components/staticPages/NewGameConfirmation.vue"
import Lobby from "./components/pagesThatNeedToBeInterValled/Lobby.vue"
import Awarding from "./components/pagesThatNeedToBeInterValled/Awarding.vue"
import ChooseBestAnswer from "./components/pagesThatNeedToBeInterValled/ChooseBestAnswer.vue"
import WaitingForOtherPlayersAfterChoosingBestAnswer from "./components/pagesThatNeedToBeInterValled/WaitingForOtherPlayersAfterChoosingBestAnswer.vue"
import WaitingForOtherPlayersAfterQuestion from "./components/pagesThatNeedToBeInterValled/WaitingForOtherPlayersAfterQuestion.vue"
import {store} from './store/store';
import App from './App';

Vue.use(vueResource);
Vue.use(VueRouter);


const router = new VueRouter({
  mode: 'history',
  base: __dirname,
  routes: [
    {path: '/~nilill/tarkvaratehnika/', component: Menu},
    {path: '/~nilill/tarkvaratehnika/about', component: About},
    {path: '/~nilill/tarkvaratehnika/registration', component: JoinGame},
    {path: '/~nilill/tarkvaratehnika/menu', component: Menu},
    {path: '/~nilill/tarkvaratehnika/question', component: Question},
    {path: '/~nilill/tarkvaratehnika/about', component: About},
    {path: '/~nilill/tarkvaratehnika/newGameConfirmation', component: NewGameConfirmation},
    {path: '/~nilill/tarkvaratehnika/lobby', component: Lobby},
    {path: '/~nilill/tarkvaratehnika/awarding', component: Awarding},
    {path: '/~nilill/tarkvaratehnika/chooseBestAnswer', component: ChooseBestAnswer},
    {path: '/~nilill/tarkvaratehnika/waitingScreen1', component: WaitingForOtherPlayersAfterChoosingBestAnswer},
    {path: '/~nilill/tarkvaratehnika/waitingScreen2', component: WaitingForOtherPlayersAfterQuestion},
  ]
})
new Vue({

  router,
  el: '#app',
  components: {App},
  template: '<App/>',
  store,
});
