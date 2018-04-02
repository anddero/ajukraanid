/* eslint-disable */
<template>

  <div id="menu" class="container">
    <img src="https://raw.githubusercontent.com/anddero/ajukraanid/master/frontend/logo.png" style="margin-top: 50px" class="center-block">

    <button @click="routeToGameConfirmation" class="btn btn-primary center-block .btn-lg">Create a new game.
    </button>
    <br/>
    <button @click="routeToRegistration" class="btn btn-primary center-block .btn-lg">Join a game</button>
    <br/>
    <button @click="routeToAbout" class="btn btn-primary center-block .btn-lg btn">About</button>
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
