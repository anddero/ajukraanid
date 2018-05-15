
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
    }
  }
</script>

<style scoped>
  #text {
    font-family: "Comic Sans MS", cursive, sans-serif;
    font-style: italic;
    font-weight: bold;
    width: 85%;
    padding-left: 15%;
    font-size: 25px;
    text-align: justify;
  }
  @media screen and (min-width:320px) and (max-width:600px) {
    #text {
      font-family: "Comic Sans MS", cursive, sans-serif;
      font-style: italic;
      font-weight: bold;
      width: 85%;
      padding-left: 15%;
      font-size: 15px;
      text-align: justify;
    }
  }
</style>
