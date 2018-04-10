/* eslint-disable */
<template>
  <div class="about container">
    <img id="menubutton" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/pleasewaittillall.png" style="margin-top: 20%; height: 60px; width: auto;"/>
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
          if (response.body.State === 'Grading') {
            window.clearInterval(window.intervalForWaitingScreen)
            console.log('Moving to ' + '/chooseBestAnswer' + ' from /waitingForOtherPlayers1111!')
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
</style>

