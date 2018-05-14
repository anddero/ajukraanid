
<template>
  <div class="add container" style="padding-top: 8%;">

    <img id="menubutton1" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/pleaseconfirmit.png"/>
    <br>
    <Alert v-if="alert" v-bind:message="alert" style="width: 40%; text-align: center; margin-left: 30%;"/>
    <form v-on:submit="createGame" style="padding-top: 12%;">
      <button style="background-color: transparent; outline: none;" type="submit" class="btn center-block .btn-lg"><img id='menubutton3' src='http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/yes.png'></button>
    </form>
    <input type="image" id="menubutton1" @click="routeToIndex()" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/backtomainmenu.png" class="btn center-block .btn-lg"/>
    <br>

  </div>
</template>

<script>
  import('../../assets/css/main.scss')
  import Alert from './Alert'

  export default {
    props: ['gameCode'],
    name: 'newGame',
    data () {
      return {
        alert: ''
      }
    },
    methods: {
      routeToIndex () {
        this.$router.replace('/')
      },

      createGame (e) {
        this.$store.dispatch('setMyUsername', 'host')
        let requestData = {Action: 'CreateGame'}
        this.$http.post(this.$store.state.requestDestination, requestData)
          .then(function (response) {
            console.log("NewGameConfirmation - createGame: ");
            console.log(response.body.Token);
            localStorage.setItem("token", response.body.Token.toString());
            this.$store.state.token = response.body.Token.toString();
            if (response.body.State === 'Error') {
              this.alert = "Error: " + response.body.Data
              console.log('Error: ' + response.body.Data)
            } else {
              this.$store.dispatch('setGameCode', response.body.Code)
              this.$router.replace('/lobby')
            }
          })
        e.preventDefault()
      }
    },

    components: {
      Alert
    },

    created: function () {
      let requestData = {Action: 'GetQuestion', 'Code': this.$store.state.gameCode, "Token": this.$store.state.token}
      this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
        console.log("NewGameConfirmation - created: ");
        console.log(response);
        this.question = response.body.Data
      })
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
