/* eslint-disable */
<template>
  <div id="summary">
    <div v-if="username !== 'host'">
      <div class="summary">
        <img id="menubutton" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/choosebestanswer_player.png" style="margin-top: 2%;"/>
        <h5></h5>
      </div>
      <hr>
      <form v-on:submit.prevent="registerUser">
        <div v-for="item in this.questions">
          <button v-if="item.Name!==username" @click="awardPlayer(item.Name)" type="button" class="btn btn-success center-block">
            {{item.Answer}}
          </button>
          <br/>
        </div>
      </form>
      <br/>
    </div>
    <div v-if="username === 'host'">
      <img id="menubutton" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/choosebestanswer.png" style="margin-top: 2%; height: 60px; width: auto;"/>
      <h3 id="text" class="center-block">{{this.$store.state.lastQuestion}}</h3>
      <br>
      <div v-for="item in this.questions">
        <h3 id="text" class="center-block">{{item.Answer}} </h3>
        <br/>
      </div>
    </div>
  </div>
</template>

<script>
  import('../../assets/css/main.scss');
  export default {
    data() {
      return {
        question: '',
        questions: '',
        intervalForState: ''
      }
    },

    methods: {
      awardPlayer(Awardee) {
        let requestData = {
          Action: 'GivePoints',
          'Code': this.gameCode,
          Name: this.$store.state.username,
          Target: Awardee
        }
        this.$http.post(this.$store.state.requestDestination, requestData)
        console.log('Moving to ' + '/waitingScreen1' + ' from /choosebestAnswer')
        this.$router.replace({path: this.$store.state.paths.waitingScreen2})
      },

      checkIfGameShouldStart() {
        let requestData = {Action: 'FetchState', 'Code': this.$store.state.gameCode}
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          if (response.body.State === 'Results') {
            window.clearInterval(window.intervalForState)
            console.log('Moving to /Results from /choose best answer')
            this.$router.replace({path: this.$store.state.paths.awarding})
          }
        })
      },

      setIntervalThatChecksIfGameShouldStart() {
        window.intervalForState = setInterval(this.checkIfGameShouldStart, 1000)
      }
    },

    computed: {
      registrations() {
        return this.$store.state.registrations
      },

      username() {
        return this.$store.state.username
      },

      gameCode() {
        return this.$store.state.gameCode
      }
    },

    created: function () {
      this.setIntervalThatChecksIfGameShouldStart()
      let requestData = {Action: 'FetchState', 'Code': this.gameCode}
      this.$http.post(this.$store.state.requestDestination, requestData)
        .then(function (response) {
          this.questions = response.body.Data
        })
      let data = {Action: 'FetchState', 'Code': this.gameCode}
      this.$http.post(this.$store.state.requestDestination, data)
        .then(function (response) {
          this.question = response.body.Data
        })
    }
  }
</script>

<style scoped>
</style>
