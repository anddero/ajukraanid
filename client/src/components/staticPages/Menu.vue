/* eslint-disable */
<template>

  <div id="menu" class="container">
    <img src="https://raw.githubusercontent.com/anddero/ajukraanid/master/frontend/logo.png" style="margin-top: 50px" class="center-block">
    <br>
    <input id="menubutton" class="btn center-block .btn-lg" type="image" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/createanewgame.png" @click="routeToGameConfirmation"/>
    <input id="menubutton" type="image" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/joinagame.png" @click="routeToRegistration" class="btn center-block .btn-lg"/>
    <input id="menubutton" @click="routeToAbout" class="btn center-block .btn-lg btn" type="image" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/about.png"/>
  </div>
</template>
<script>
  export default {
    methods: {
      routeToGameConfirmation() {
        this.$router.replace({path: this.$store.state.paths.confirmation})
      },
      routeToAbout() {
        this.$router.replace({path: this.$store.state.paths.about})
      },
      routeToRegistration() {
        this.$router.replace({path: this.$store.state.paths.registration})
      }
    }
  }
</script>
<style scoped>

</style>
