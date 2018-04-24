/* eslint-disable */
<template>
  <div>
    <div class="jumbotron">
      <div class="container">
        <img id="menubutton1" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/welcometoabout.png">
        <br>
        <p id="text">The game is running in multiple devices simultaneously. There are 2 views of game: a player
          and a host. On host's screen there will be displayed a question that should be answered by everybody. After the answering round
          all answers will be displayed anonymously on host's screen. Following that everybody must choose the best answer. The more
          player gets votes the more he receives points. After all questions will be answered a player with most points will win.</p>
      </div>
    </div>
    <div class="container">
      <br>
      <input type="image" id="menubutton1" @click="routeToIndex()" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/backtomainmenu.png" class="btn center-block .btn-lg"/>
    </div>
  </div>
</template>

<script>

  export default {
    name: 'about',
    data() {
      return {
        show: true
      }
    },
    methods: {
      routeToIndex() {
        this.$router.replace({path: this.$store.state.paths.index})
      }
    }
  }
</script>

<style scoped>
</style>
