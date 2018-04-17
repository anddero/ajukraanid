/* eslint-disable */
<template>
  <div id="summary">
    <div class="summary">
      <img id="menubutton" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/results.png" style="margin-top: 2%;"/>
      <h5></h5>
    </div>
    <hr>
    <div v-for="playerData in this.points">
      <h3 style="text-align: center" id="text">{{playerData.Name}} earned {{playerData.Points}} points</h3>
    </div>
    <br>
    <br/>
  </div>

</template>

<script>
  import('../../assets/css/main.css');
  export default {
    data() {
      return {
        interval: '',
        points: ''
      }
    },

    methods: {
      checkGameState() {
        let requestData = {Action: 'FetchState', 'Code': this.$store.state.gameCode}
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          if (response.body.State === 'Answering') {
            window.clearInterval(window.interval)
            console.log('Moving to ' + '/question' + ' from /Answering')
            this.$router.replace({path: this.$store.state.paths.question})
          }
        })
      },

      setIntervalThatChecksGameState() {
        window.interval = setInterval(this.checkGameState, 1000)
      }
    },

    computed: {
      registrations() {
        return this.$store.state.registrations
      },
      gameCode() {
        return this.$store.state.gameCode
      }
    },

    created: function () {
      this.setIntervalThatChecksGameState()
      let requestData = {Action: 'FetchState', 'Code': this.gameCode}
      this.$http.post(this.$store.state.requestDestination, requestData)
        .then(function (response) {
          console.log(response.body.Data)
          this.points = response.body.Data
        })
    }
  }
</script>

<style scoped>
  .summary {
    text-align: center;
  }

  .row h4 {
    display: inline-block;
    width: 30%;
    margin: 0 0 10px 0;
    box-sizing: border-box;
  }

  .row span {
    width: 30%;
    color: red;
    cursor: pointer;
  }

  .row span:hover {
    color: darkred;
  }

</style>
