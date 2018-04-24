/* eslint-disable */
<template>
  <div class="about container">
    <img id="menubutton1" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/pleasewaittillall.png" style="margin-top: 5%; height: 60px; width: auto;"/>
    <img id="menubutton1" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/15sec.png" style="height: 60px; width: auto;"/>
    <br>
    <br>
    <input type="image" id="menubutton1" @click="routeToIndex()" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/backtomainmenu.png" class="btn center-block .btn-lg"/>
  </div>
</template>

<script>
  import('../../assets/css/main.scss');
  export default {
    name: 'about',
    data() {
      return {
        interval: ''
      }
    },

    methods: {
      routeToIndex() {
        this.$router.replace({path: this.$store.state.paths.index})
      },

      checkGameState() {
        let requestData = {Action: 'FetchState', 'Code': this.$store.state.gameCode};
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          if (response.body.State === 'Results') {
            window.clearInterval(window.interval);
            console.log('Moving to ' + '/Results' + ' from /waitingForOtherPlayers2')
            this.$router.replace({path: this.$store.state.paths.awarding})
          }
        })
      },

      setIntervalThatChecksGameState() {
        window.interval = setInterval(this.checkGameState, 1000)
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

