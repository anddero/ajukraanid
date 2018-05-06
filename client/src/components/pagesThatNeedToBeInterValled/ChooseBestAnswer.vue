
<template>
  <div id="summary">
    <div v-if="username !== 'host'">
      <div class="summary">
        <img id="menubutton1" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/choosebestanswer_player.png" style="margin-top: 8%;"/>
        <h5></h5>
      </div>
      <hr>
     <b id="seconds"> {{timeLeft}}  seconds remaining </b>
      <form v-on:submit.prevent="registerUser" style="margin-top: 2%;">
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
      <img id="menubutton13" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/answers.png" style="margin-top: 2%; height: 60px; width: auto;"/>
      <h3 id="text" class="center-block" style="padding-right: 14%;">{{this.$store.state.lastQuestion}}</h3>
      <br>
      <div v-for="item in this.questions">
        <h3 style=" margin-right: 30%; padding-left: 23%;" id="text" class="center-block">{{item.Answer}} </h3>
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
        timeLeft: 30,
        question: '',
        questions: '',
        interval: ''
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
        window.clearInterval(window.interval)
        this.$router.replace('/waitingScreen2')
      },

      checkIfGameShouldStart() {
        let requestData = {Action: 'FetchState', 'Code': this.$store.state.gameCode}
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          if (response.body.State === 'Results') {
            window.clearInterval(window.interval)
            console.log('Moving to /Results from /choose best answer')
            this.$router.replace('/results')
          } else {
            this.timeLeft = response.body.Data.TimeLeft
          }
        })
      },

      setIntervalThatChecksIfGameShouldStart() {
        window.interval = setInterval(this.checkIfGameShouldStart, 1000)
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
          this.questions = response.body.Data.Answers
        })
      let data = {Action: 'FetchState', 'Code': this.gameCode}
      this.$http.post(this.$store.state.requestDestination, data)
        .then(function (response) {
          this.question = response.body.Data.Question
        })
    }
  }
</script>

<style scoped>
</style>
