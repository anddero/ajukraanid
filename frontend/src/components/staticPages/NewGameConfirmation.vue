/* eslint-disable */
<template>
  <div class="add container" style="padding-top: 8%;">
    <Alert v-if="alert" v-bind:message="alert"/>
    <img id="menubutton" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/pleaseconfirmit.png"/>
    <br>
    <form v-on:submit="createGame" style="padding-top: 12%;">
      <button style="background-color: transparent; outline: none;" type="submit" class="btn center-block .btn-lg"><img id='menubutton' src='http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/yes.png'></button>
    </form>
    <input type="image" id="menubutton" @click="routeToIndex()" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/backtomainmenu.png" class="btn center-block .btn-lg"/>
    <br>

  </div>
</template>

<script>
  import('../../assets/css/main.scss');
  import Alert from './Alert'

  export default {
    props: ['gameCode'],
    name: 'newGame',
    data () {
      return {
        alert: ""
      }
    },
    methods: {
      routeToIndex() {
        this.$router.replace({path: this.$store.state.paths.index})
      },

      createGame (e) {
        this.$store.dispatch('setMyUsername', 'host')
        let requestData = {Action: "CreateGame"}
        this.$http.post(this.$store.state.requestDestination, requestData)
          .then(function (response) {
            this.$store.dispatch('setGameCode', response.body.Code)
            this.$router.replace({path: this.$store.state.paths.lobby})
          })
        e.preventDefault()
      }
    },

    components: {
      Alert
    },

    created: function () {
      let requestData = {Action: 'GetQuestion', 'Code': this.$store.state.gameCode}
      this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
        this.question = response.body.Data
      })
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
