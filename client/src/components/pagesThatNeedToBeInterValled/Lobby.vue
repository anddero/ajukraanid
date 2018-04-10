/* eslint-disable */
<template>
  <div id="summary">
    <div style="padding-left: 30%; padding-top: 2%;">
      <table>
        <tr>
          <td>
            <img id="menubutton" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/gamecode.png"/></td>
          <td><b class="code">{{this.gameCode}}</b>
          </td>
        </tr>
      </table>
    </div>
    <div v-if="username === 'host'" style="padding-top: 15%;">
      <div class="container" style="width: 27%;">
        <transition-group class="ui horizontal list" name="list" tag="p">
          <div v-for="registration in items" :key="registration" class="row">

            <div class="row">
              <div class="col-sm-3 col-md-6">
                <div class="col" style='font-family: "Comic Sans MS", cursive, sans-serif;
              font-style: italic;
              font-weight: bold;
              font-size: 25px; margin-left: 50%;'>{{ registration }}
                </div>
              </div>
              <div class="col-sm-9 col-md-6">

                <button v-if="username === 'host'" @click="unregister(registration)" class="btn pull-right btn-danger">
                  Unregister
                </button>
              </div>
            </div>
            <div style="background-color: black; height: 1px; margin-top: 10px; margin-bottom: 10px"></div>
          </div>
        </transition-group>
      </div>
      <br>
      <input type="image" id="menubutton" @click="startGame()"
             src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/startgame.png" class="btn center-block .btn-lg"/>
    </div>
    <div v-if="username !== 'host'">
      <div class="container" style="padding-top: 10%;">
        <transition-group class="ui horizontal list" name="list" tag="p">
          <div v-for="registration in items" :key="registration" class="row">

            <div class="center-block"><b><h3 align="center" style='font-family: "Comic Sans MS", cursive, sans-serif;
              font-style: italic;
              font-weight: bold;
              font-size: 25px;'>{{ registration }}</h3></b></div>
            <div align="center"
                 style="width: 150px; background-color: black; height: 1px; margin-top: 10px; margin-bottom: 10px"
                 class="center-block"></div>
          </div>
        </transition-group>
      </div>
    </div>
  </div>
</template>

<script>
  import('../../assets/css/main.css');
  export default {

    data() {
      return {
        items: [],
        loadPlayerInterval: "",
        gameStatusInterval: "",

      }
    },

    methods: {
      checkIfGameShouldStart() {
        let requestData = {Action: "FetchState", "Code": this.$store.state.gameCode};
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          if (response.body.State === "Answering") {
            window.clearInterval(window.gameStatusInterval);
            window.clearInterval(window.loadPlayerInterval);
            console.log("Moving to /question from /lobby vol 2");
            this.$router.replace({path: this.$store.state.paths.question})
          } else if (response.body.State === "Error") {
            console.log("Error: " + response.body.Data)
            this.alert = response.body.Data;
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
            if (this.items !== response.body.Data.length) {
              this.items = this.$store.state.registrations;
              this.$store.dispatch('updatePlayers', response.body.Data);
            }
          });
      },

      setIntervalThatLoadsPlayersEverySecond() {
        window.loadPlayerInterval = setInterval(this.loadPlayers, 1000)
      },

      unregister(userName) {
        let requestData = {Action: "RemovePlayer", "Code": this.gameCode, "Name": userName};
        console.log(requestData)
        this.$http.post(this.$store.state.requestDestination, requestData);

        this.$store.commit({
          type: 'unregister',
          name: userName
        });
      },

      startGame() {
        let requestData = {Action: "StartGame", "Code": this.$store.state.gameCode};
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          if (response.body.State === "Answering") {
            window.clearInterval(window.gameStatusInterval);
            window.clearInterval(window.loadPlayerInterval);
            console.log("Moving to /question from /lobby vol 2");
            this.$router.replace({path: this.$store.state.paths.question})
          } else if (response.body.State === "Error") {
            console.log("Error: " + response.body.Data)
            this.alert = response.body.Data;
          }
          console.log(response.body)
        });
      }
    },

    computed: {
      registrations() {
        this.items = this.$store.state.registrations
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
      this.setIntervalThatChecksIfGameShouldStart()
      this.setIntervalThatLoadsPlayersEverySecond()
    }
  }
</script>

<style scoped>

  .code {
    font-size: 90px;
  }
  /* Enter and leave animations can use different */
  /* durations and timing functions.              */
  .slide-fade-enter-active {
    transition: all .2s ease;
  }

  .slide-fade-leave-active {
    transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
  }

  .slide-fade-enter, .slide-fade-leave-to
    /* .slide-fade-leave-active below version 2.1.8 */
  {
    transform: translateX(10px);
    opacity: 0;
  }

  .list-item {
    display: inline-block;
    margin-right: 10px;
  }

  .list-enter-active, .list-leave-active {
    transition: all 1s;
  }

  .list-enter, .list-leave-to /* .list-leave-active below version 2.1.8 */
  {
    opacity: 0;
    transform: translateY(30px);
  }

</style>
