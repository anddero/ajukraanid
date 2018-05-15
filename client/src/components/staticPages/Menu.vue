
<template>

  <div id="menu" class="container">
    <img src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/ajukraanid.png" class="center-block">
    <br>
    <input id="menubutton1" class="btn center-block .btn-lg" type="image" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/createanewgame.png" @click="routeToGameConfirmation"/>
    <input id="menubutton2" type="image" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/joinagame.png" @click="routeToRegistration" class="btn center-block .btn-lg"/>
    <input id="menubutton3" @click="routeToAbout" class="btn center-block .btn-lg btn" type="image" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/about.png"/>


    <button @click="routeToLogin" id="myBtn" title="Go to top">Admin</button>
  </div>
</template>
<script>
  export default {
    methods: {
      routeTest () {
        this.$router.replace('/test')
      },
      routeToGameConfirmation () {
        this.$router.replace('/newGameConfirmation')
      },
      routeToAbout () {
        this.$router.replace('/about')
      },
      routeToRegistration () {
        this.$router.replace('/registration')
      },
      routeToLogin () {
        this.$router.replace('/login')
      },
      joinGame () {
        var state = localStorage.getItem('gamestate')
        if (state === 'Answering') {
            this.$router.replace('/question')
          } else if (state === 'Grading') {
            this.$router.replace('/grading')
          } else if (state === 'Results') {
            this.$router.replace('/results')
          } else if (state === 'Lobby') {
            this.$router.replace('/lobby')
          } else {
            localStorage.clear()
          }
      },
      getPlayers () {
        var gameCode = localStorage.getItem('gamecode')
        let requestData = {Action: 'GetPlayers', 'Code': gameCode, "Token": this.$store.state.token}
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          console.log("Menu - getPlayers: " + response)
          var playerName = localStorage.getItem('playername')
          var players = response.body.Players
          console.log('Players')
          console.log(players)
          if (players.includes(playerName)) {
            this.joinGame()
          } else {
            localStorage.clear()
          }
        })
      }
    },
    created: function () {
      if (localStorage.getItem('gamecode') !== null) {
        this.$store.state.token = localStorage.getItem("token");
        this.$store.state.gameCode = localStorage.getItem('gamecode');
        this.$store.state.username = localStorage.getItem("playername");
        var requestData = {Action: 'FetchState', 'Code': this.$store.state.gameCode, "Token": this.$store.state.token}
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          if (response.body.State !== 'Error' && response.body.State !== 'Inactive') {
            localStorage.setItem('gamestate', response.body.State)
            this.getPlayers()
          }
          if (response.body.State === "Inactive") {
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
          }
          if (response.body.State === "Answering") {
            window.clearInterval(window.gameStatusInterval);
            window.clearInterval(window.loadPlayerInterval);
            console.log("Moving to /question from /lobby vol 2");
            this.$router.replace('/question')
          } else if (response.body.State === "Error") {
            console.log("Error: " + response.body.Data)
            this.alert = response.body.Data;
          }
          if (response.body.State === "Results") {
            window.clearInterval(window.interval);
            console.log("Moving to " + "/Results" + " from /Answering");
            this.$router.replace('/results')
          }
          if (response.body.State === "Grading") {
            window.clearInterval(window.interval);
            console.log("Moving to " + "/Grading" + " from /Answering");
            this.$router.replace('/grading')
          }
        })
      } else {
        console.log('tühi')
      }
    }
  }
</script>
<style scoped>
  #myBtn {
    display: block;
    position: fixed;
    bottom: 20px;
    right: 30px;
    z-index: 99;
    font-size: 18px;
    border: none;
    outline: none;
    background-color: transparent;
    color: white;
    cursor: pointer;
    padding: 15px;
    border-radius: 4px;
  }

</style>
