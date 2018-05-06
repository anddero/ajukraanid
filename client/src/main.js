import Vue from 'vue'
import VueRouter from 'vue-router'
import vueResource from 'vue-resource'
import About from './components/staticPages/About.vue'
import Question from './components/pagesThatNeedToBeInterValled/Question.vue'
import Menu from './components/staticPages/Menu.vue'
import JoinGame from './components/staticPages/JoinGame.vue'
import NewGameConfirmation from './components/staticPages/NewGameConfirmation.vue'
import Lobby from './components/pagesThatNeedToBeInterValled/Lobby.vue'
import Awarding from './components/pagesThatNeedToBeInterValled/Awarding.vue'
import ChooseBestAnswer from './components/pagesThatNeedToBeInterValled/ChooseBestAnswer.vue'
import WaitingForOtherPlayersAfterChoosingBestAnswer from './components/pagesThatNeedToBeInterValled/WaitingForOtherPlayersAfterChoosingBestAnswer.vue'
import WaitingForOtherPlayersAfterQuestion from './components/pagesThatNeedToBeInterValled/WaitingForOtherPlayersAfterQuestion.vue'
import {store} from './store/store'
import App from './App'
import VueChartkick from 'vue-chartkick'
import Chart from 'chart.js'

import '../node_modules/bootstrap/dist/css/bootstrap.min.css'
import './assets/css/main.css'
import Alert from './components/staticPages/Alert'

Vue.use(vueResource)
Vue.use(VueChartkick)
Vue.use(VueRouter)

const router = new VueRouter({
  mode: 'history',
  base: '/~nilill/tarkvaratehnika/',
  routes: [
    {path: '/', component: Menu},
    {path: '/about', component: About},
    {path: '/registration', component: JoinGame},
    {path: '/menu', component: Menu},
    {path: '/question', component: Question},
    {path: '/about', component: About},
    {path: '/newGameConfirmation', component: NewGameConfirmation},
    {path: '/lobby', component: Lobby},
    {path: '/results', component: Awarding},
    {path: '/grading', component: ChooseBestAnswer},
    {path: '/waitingScreen1', component: WaitingForOtherPlayersAfterChoosingBestAnswer},
    {path: '/waitingScreen2', component: WaitingForOtherPlayersAfterQuestion}
  ]
})

new Vue({
  router,
  el: '#app',
  components: {App},
  template: '<App/>',
  store
})
