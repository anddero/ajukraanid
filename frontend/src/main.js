import Vue from 'vue'
import App from './App.vue'
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

Vue.use(vueResource);
Vue.use(VueRouter);

const router = new VueRouter({
  mode: 'history',
  base: __dirname,
  routes: [
    {path: '/', component: Menu},
    {path: '/about', component: About},
    {path: '/registration', component: JoinGame},
    {path: '/menu', component: Menu},
    {path: '/question', component: Question},
    {path: '/about', component: About},
    {path: '/newGameConfirmation', component: NewGameConfirmation},
    {path: '/lobby', component: Lobby},
    {path: '/awarding', component: Awarding},
    {path: '/chooseBestAnswer', component: ChooseBestAnswer},
    {path: '/waitingScreen1', component: WaitingForOtherPlayersAfterChoosingBestAnswer},
    {path: '/waitingScreen2', component: WaitingForOtherPlayersAfterQuestion}
  ]
})

new Vue({
  router,
  template: `
    <div id="app" class="container">
    <router-view></router-view>
    </div id>  
  `,
  store,
}).$mount('#app');
