
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
    }
    /*
    created: function () {
      if (localStorage.getItem('gamecode') !== null) {
        var gameCode = localStorage.getItem('gamecode')
        var requestData = {Action: 'FetchState', 'Code': gameCode, "Token": localStorage.getItem("token")}
        this.$store.state.token = localStorage.getItem("token");
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          if (response.body.State !== 'Error' && response.body.State !== 'Inactive') {
            localStorage.setItem('gamestate', response.body.State)
            this.getPlayers()
          } else {
            localStorage.clear()
          }
        })
      } else {
        console.log('tühi')
      }
    }
    */
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
