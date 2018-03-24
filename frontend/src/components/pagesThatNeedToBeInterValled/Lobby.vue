<template>
  <div id="summary">
    <div class="summary">
      <h3>Enter game {{this.gameCode}}</h3>
    </div>
    <hr>
    <div class="row" v-for="registration in registrations">
      <h4 style="margin-top: 6px">{{ registration }}</h4>
      <button v-if="username === 'host'" @click="unregister(registration)" class="btn pull-right btn-danger">
        Unregister
      </button>
      <div style="background-color: black; height: 1px"></div>
    </div>

    <br/>
    <button v-if="username === 'host'" @click="startGame()" class="btn btn-primary center-block">Start game</button>
    <br>
    <button @click="routeTo('/')" type="button" class="btn btn-secondary center-block">Back to main menu</button>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex';

  export default {

    data() {
      return {
        loadPlayerInterval: "",
        gameStatusInterval: ""
      }
    },

    methods: {
      routeTo(state) {
        let requestData = {Action: "StartGame", "Code": this.gameCode};
        this.$http.post(this.$store.state.requestDestination, requestData);
        console.log("Moving to " + state + " from /lobby vol 1");
        window.clearInterval(window.gameStatusInterval);
        window.clearInterval(window.loadPlayerInterval);
        this.$router.replace({path: state})
      },

      checkIfGameShouldStart() {
        let requestData = {Action: "FetchState", "Code": this.$store.state.gameCode};
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          if (response.body.State === "question") {
            window.clearInterval(window.gameStatusInterval);
            window.clearInterval(window.loadPlayerInterval);
            console.log("Moving to /question from /lobby vol 2");
            this.$router.replace({path: "/question"})
          }
        });
      },

      setIntervalThatChecksIfGameShouldStart() {
        window.gameStatusInterval = setInterval(this.checkIfGameShouldStart, 1000)
      },

      loadPlayers() {
        let requestData = {Action: "FetchState", "Code": this.gameCode};
        this.$http.post(this.$store.state.requestDestination, requestData)
          .then(function (response) {
            this.$store.dispatch('updatePlayers', response.body.Data);
          });
      },

      setIntervalThatLoadsPlayersEverySecond() {
        window.loadPlayerInterval = setInterval(this.loadPlayers, 1000)
      },

      unregister(registration) {
        let requestData = {Action: "RemovePlayer", "Code": this.gameCode, "Name": registration.name};
        this.$http.post(this.$store.state.requestDestination, requestData);
        this.$store.commit({
          type: 'unregister',
          name: registration.name
        });
      },

      startGame() {
        window.clearInterval(window.gameStatusInterval);
        window.clearInterval(window.loadPlayerInterval);
        console.log("Moving to " + "/question" + " from /question vol 3");
        this.$router.replace({path: "/question"})
        let requestData = {Action: "StartGame", "Code": this.gameCode};
        this.$http.post(this.$store.state.requestDestination, requestData);
      }
    },

    computed: {
      registrations() {
        return this.$store.state.registrations
      },

      gameCode() {
        return this.$store.state.gameCode
      },

      username() {
        return this.$store.state.username
      }
    },

    created: function () {
      this.setIntervalThatChecksIfGameShouldStart();
      this.setIntervalThatLoadsPlayersEverySecond();
    }
  }
</script>

<style scoped>
  #registrations {
    box-shadow: 1px 1px 2px 1px #ccc;
    margin: 20px;
    padding: 20px;
    display: inline-block;
    width: 500px;
    vertical-align: top;
    text-align: left;
  }
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
