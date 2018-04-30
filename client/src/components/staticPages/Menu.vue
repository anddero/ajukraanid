
<template>

  <div id="menu" class="container">
    <img src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/ajukraanid.png" class="center-block">
    <br>
    <input id="menubutton1" class="btn center-block .btn-lg" type="image" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/createanewgame.png" @click="routeToGameConfirmation"/>
    <input id="menubutton2" type="image" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/joinagame.png" @click="routeToRegistration" class="btn center-block .btn-lg"/>
    <input id="menubutton3" @click="routeToAbout" class="btn center-block .btn-lg btn" type="image" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/about.png"/>
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
        let requestData = {Action: 'GetPlayers', 'Code': gameCode}
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
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
        var requestData = {Action: 'FetchState', 'Code': gameCode}
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

</style>
