
<template>
  <div class="about container">
    <img id="menubutton12" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/waitforothers.png" style="margin-top: 40vh; height: 60px; width: auto;"/>
    <!--here come seconds-->
    <b id="countdown">{{timeLeft}} seconds remaining</b>
    <br>
    <br>
  </div>
</template>

<script>
  import('../../assets/css/main.scss');
  export default {
    name: 'about',
    data () {
      return {
        interval: '',
        timeLeft: 30
      }
    },

    methods: {
      routeTo (state) {
        console.log('Moving to ' + state + ' from waitingForOtherPlayers1')
        window.clearInterval(window.interval)
        this.$router.replace({path: state})
      },

      checkGameState () {
        let requestData = {Action: 'FetchState', 'Code': this.$store.state.gameCode, "Token": this.$store.state.token}
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response){
          if (response.body.State === 'Grading') {
            window.clearInterval(window.interval)
            console.log('Moving to ' + '/chooseBestAnswer' + ' from /waitingForOtherPlayers1111!')
            this.$router.replace('/grading')
          } else {
            this.timeLeft = response.body.Data.TimeLeft
          }
        })
      },

      setIntervalThatChecksGameState () {
        window.interval = setInterval(this.checkGameState, 1000)
      },
    },

    created: function () {
      let requestData = {Action: 'GetQuestion', 'Code': this.$store.state.gameCode, "Token": this.$store.state.token}
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

