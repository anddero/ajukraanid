
<template>
  <div id="summary" class="center-block">

    <div class="jumbotron">
      <div class="container">
        <!--<h1 class="display-3">Join an existing game</h1>-->
        <img id="menubutton1" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/joinanexistinggame.png"/>
        <p id="text">Enter a nickname and the gamecode that is shown in the host lobby. Remember to have fun!</p>
      </div>
    </div>
    <div class="container">
      <Alert v-if="alert" v-bind:message="alert" style="width: 40%; text-align: center; margin-left: 30%;"/>
      <form v-on:submit.prevent="registerUser">
        <div class="well" style="width: 40%; text-align: center; margin-left: 30%;">
          <input type="text" class="form-control" placeholder="Nickname" v-model="name">
          <br/>
          <input type="text" class="form-control" placeholder="Gamecode" v-model="gameCode">
        </div>
        <button style="background-color: transparent; outline: none;" type="submit" class="btn center-block .btn-lg"><img id='menubutton3' src='http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/join.png'></button>
        <input type="image" id="menubutton1" @click="routeToIndex()" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/backtomainmenu.png" class="btn center-block .btn-lg"/>
      </form>
    </div>
  </div>

</template>

<script>
  import('../../assets/css/main.scss')
  import Alert from './Alert'

  export default {
    data () {
      return {
        name: '',
        gameCode: '',
        alert: ''
      }
    },
    methods: {
      routeToIndex () {
        this.$router.replace('/')
      },
      registerUser () {
        let myuser = {name: 'Max', gameCode: 'gameCode'}
        myuser.name = this.$data.name
        myuser.gameCode = this.$data.gameCode
        localStorage.setItem('gamecode', this.$data.gameCode)
        localStorage.setItem('playername', this.$data.name)
        let requestData = {Action: 'JoinGame', 'Code': this.$data.gameCode, 'Name': this.$data.name}
        if (!this.$data.name || !this.$data.gameCode) {
          this.alert = 'Please fill in all required fields'
        } else {
          this.$http.post(this.$store.state.requestDestination, requestData)
            .then(function (response) {
              if (response.body.Data === 'Did not find such game with game code: ' + this.$data.gameCode) {
                this.alert = 'Game code was not found.'
              } else if (response.body.Data === 'Such username is already taken.') {
                this.alert = 'Such username is already taken.'
              } else {
                this.$store.dispatch('setGameCode', this.$data.gameCode)
                this.$store.dispatch('setMyUsername', this.$data.name)
                this.$router.replace('/lobby')
              }
            })
        }
      }
    },
    components: {
      Alert
    }
  }
</script>

<style scoped>
  .well {
    background-color: transparent !important;
    border-color: transparent !important;

  }

</style>
