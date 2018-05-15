<template>
  <div class="about container">
    <img id="menubutton12" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/waitforothers.png" style="margin-top: 40vh; height: 60px; width: auto;"/>
    <!--here come seconds-->
    <b id="countdown">{{timeLeft}} seconds remaining</b>
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
        interval: '',
        timeLeft: 30
      }
    },
    methods: {
      routeToIndex () {
        localStorage.clear();
        window.clearInterval(window.interval);
        this.$store.state.Authorization = "";
        this.$store.state.username = "";
        this.$store.state.gameCode = 0;
        this.$store.state.questionNumber = 0;
        this.$store.state.lastQuestion = "";
        this.$store.state.registrations = [];
        this.$store.state.token = "";
        this.$router.replace('/')
      },
      routeToIndex() {
        localStorage.clear();
        window.clearInterval(window.interval);
        this.$store.state.Authorization = "";
        this.$store.state.username = "";
        this.$store.state.gameCode = 0;
        this.$store.state.questionNumber = 0;
        this.$store.state.lastQuestion = "";
        this.$store.state.registrations = [];
        this.$store.state.token = "";
        this.$router.replace('/')
      },
      checkGameState() {
        let requestData = {Action: 'FetchState', 'Code': this.$store.state.gameCode, "Token": this.$store.state.token};
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          if (response.body.State === 'Results') {
            window.clearInterval(window.interval);
            console.log('Moving to ' + '/Results' + ' from /waitingForOtherPlayers2')
            this.$router.replace('/results')
          } else {
            this.timeLeft = response.body.Data.TimeLeft
          }
        })
      },
      setIntervalThatChecksGameState() {
        window.interval = setInterval(this.checkGameState, 1000)
      },
    },
    created: function () {
      let requestData = {Action: "GetQuestion", "Code": this.$store.state.gameCode, "Token": this.$store.state.token}
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
