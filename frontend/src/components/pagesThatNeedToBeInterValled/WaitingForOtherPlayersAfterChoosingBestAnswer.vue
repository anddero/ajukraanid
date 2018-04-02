/* eslint-disable */
<template>
  <div class="about container">
    <h1 class="page-header text-center">Please wait till all players have answered</h1>
    <br>
    <br>
  </div>
</template>

<script>
  import('../../assets/css/main.css');
  export default {
    name: 'about',
    data () {
      return {
        intervalForWaitingScreen: ''
      }
    },

    methods: {
      routeTo (state) {
        console.log('Moving to ' + state + ' from waitingForOtherPlayers1')
        window.clearInterval(window.intervalForWaitingScreen)
        this.$router.replace({path: state})
      },

      checkGameState () {
        let requestData = {Action: 'FetchState', 'Code': this.$store.state.gameCode}
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response){
          if (response.body.State === 'chooseBestAnswer') {
            window.clearInterval(window.intervalForWaitingScreen)
            console.log('Moving to ' + '/chooseBestAnswer' + ' from /waitingForOtherPlayers1')
            this.$router.replace({path: this.$store.state.paths.chooseBestAnswer})
          }
        })
      },

      setIntervalThatChecksGameState () {
        window.intervalForWaitingScreen = setInterval(this.checkGameState, 1000)
      },
    },

    created: function () {
      let requestData = {Action: 'GetQuestion', 'Code': this.$store.state.gameCode}
      this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
        this.question = response.body.Data
      })
      this.setIntervalThatChecksGameState()
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  @import "../../assets/css/main.css";
</style>

