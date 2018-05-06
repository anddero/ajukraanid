
<template>
  <div class="about container">
    <img id="menubutton1" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/waitforothers.png" style="margin-top: 20%; height: 60px; width: auto;"/>
    <img id="menubutton1" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/15sec.png" style="height: 60px; width: auto;"/>
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
        interval: ''
      }
    },

    methods: {
      routeTo (state) {
        console.log('Moving to ' + state + ' from waitingForOtherPlayers1')
        window.clearInterval(window.interval)
        this.$router.replace({path: state})
      },

      checkGameState () {
        let requestData = {Action: 'FetchState', 'Code': this.$store.state.gameCode}
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response){
          if (response.body.State === 'Grading') {
            window.clearInterval(window.interval)
            console.log('Moving to ' + '/chooseBestAnswer' + ' from /waitingForOtherPlayers1111!')
            this.$router.replace('/grading')
          }
        })
      },

      setIntervalThatChecksGameState () {
        window.interval = setInterval(this.checkGameState, 1000)
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

