/* eslint-disable */
<template>
  <div class="about container">
    <h1 class="page-header text-center">Please wait till all players have answered (15 sec)</h1>
    <br>
    <br>
    <button @click="routeTo('/')" type="button" class="btn btn-secondary center-block">Back to main menu</button>
  </div>
</template>

<script>
  export default {
    name: 'about',
    data() {
      return {
        intervalForWaitingScreen: ''
      }
    },

    methods: {
      routeTo(state) {
        console.log('Moving to ' + state + ' from /waitingForOtherPlayers2')
        this.$router.replace({path: state})
      },

      checkGameState() {
        let requestData = {Action: 'FetchState', 'Code': this.$store.state.gameCode};
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {

          if (response.body.State === 'awarding') {
            window.clearInterval(window.intervalForWaitingScreen);
            console.log('Moving to ' + '/awarding' + ' from /waitingForOtherPlayers2')
            this.$router.replace({path: '/awarding'})
          }
        })
      },

      setIntervalThatChecksGameState() {
        window.intervalForWaitingScreen = setInterval(this.checkGameState, 1000)
      },
    },

    created: function () {
      let requestData = {Action: "GetQuestion", "Code": this.$store.state.gameCode}
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
