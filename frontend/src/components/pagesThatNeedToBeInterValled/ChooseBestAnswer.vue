/* eslint-disable */
<template>
  <div id="summary">
    <div v-if="username !== 'host'">
      <div class="summary">
        <h3>Choose best answer</h3>
        <h5></h5>
      </div>
      <hr>
      <form v-on:submit.prevent="registerUser">
        <div v-for="item in this.questions">
          <button @click="awardPlayer(item.name)" type="button" class="btn btn-success center-block">
            {{item.answer}}
          </button>
          <br/>
        </div>
      </form>
      <button @click="routeTo('/')" type="button" class="btn btn-secondary center-block">Back to main menu</button>
      <br/>
    </div>
    <div v-if="username === 'host'">
      <h3>Choose best answer for the question:</h3>
      <h3>{{this.question}}</h3>
    </div>
  </div>
</template>

<script>

  export default {
    data () {
      return {
        question: '',
        questions: '',
        intervalForState: ''
      }
    },

    methods: {
      awardPlayer (Awardee) {
        let requestData = {
          Action: 'GivePoints',
          'Code': this.gameCode,
          Name: this.$store.state.username,
          Target: Awardee
        }
        this.$http.post(this.$store.state.requestDestination, requestData)
        console.log('Moving to ' + '/waitingScreen1' + ' from /choosebestAnswer')
        this.$router.replace({path: '/waitingScreen2'})
      },

      checkIfGameShouldStart () {
        let requestData = {Action: 'FetchState', 'Code': this.$store.state.gameCode}
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          if (response.body.State === 'awarding') {
            window.clearInterval(window.intervalForState)
            console.log('Moving to /awarding from /choose best answer')
            this.$router.replace({path: '/awarding'})
          }
        })
      },

      setIntervalThatChecksIfGameShouldStart () {
        window.intervalForState = setInterval(this.checkIfGameShouldStart, 1000)
      }
    },

    computed: {
      registrations () {
        return this.$store.state.registrations
      },

      username () {
        return this.$store.state.username
      },

      gameCode () {
        return this.$store.state.gameCode
      }
    },

    created: function () {
      this.setIntervalThatChecksIfGameShouldStart()
      let requestData = {Action: 'GetAnswers', 'Code': this.gameCode}
      this.$http.post(this.$store.state.requestDestination, requestData)
        .then(function (response) {
          this.questions = response.body.Data
        })
      let data = {Action: 'GetQuestion', 'Code': this.gameCode}
      this.$http.post(this.$store.state.requestDestination, data)
        .then(function (response) {
          this.question = response.body.Data
        })
    }
  }
</script>

<style>
</style>
