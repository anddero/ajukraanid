/* eslint-disable */
<template>

  <div id="menu" class="container">
    <img src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/ajukraanid.png" style="margin-top: 50px" class="center-block">
    <br>
    <input id="menubutton" class="btn center-block .btn-lg" type="image" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/createanewgame.png" @click="routeToGameConfirmation"/>
    <input id="menubutton" type="image" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/joinagame.png" @click="routeToRegistration" class="btn center-block .btn-lg"/>
    <input id="menubutton" @click="routeToAbout" class="btn center-block .btn-lg btn" type="image" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/about.png"/>
  </div>
</template>
<script>
  /* if (localStorage.getItem('gamecode') !== null) { // Here comes functionality to join crashed game.
    var playerName = localStorage.getItem('playername')
    var gameCode = localStorage.getItem('gamecode')
    let requestData = {Action: "FetchState", "Code": gameCode};
    this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
      if (response.body.State !== "Error") {
        this.$router.replace({path: this.$store.state.paths.question})
      } else if (response.body.State === "Error") {
        console.log("Error: " + response.body.Data)
        this.alert = response.body.Data;
      }
    })
  } else {
    console.log('tühi')
  } */
  export default {
    methods: {
      routeToGameConfirmation () {
        this.$router.replace({path: this.$store.state.paths.confirmation})
      },
      routeToAbout () {
        this.$router.replace({path: this.$store.state.paths.about})
      },
      routeToRegistration () {
        this.$router.replace({path: this.$store.state.paths.registration})
      }
    }
  }
</script>
<style scoped>

</style>
