/* eslint-disable */
<template>
  <div id="registration">
    <h3 v-if="username === 'host'"> {{ question }}</h3>
    <Alert v-if="alert" v-bind:message="alert"/>
    <hr>
    <form v-on:submit.prevent="routeToWaitingScreen()">
      <div  v-if="username !== 'host'" class="well">
        <br/>
        <input type="text" class="form-control" placeholder="Answer" v-model="answer">
      </div>
      <button  v-if="username !== 'host'" type="submit" class="btn btn-primary center-block">Submit</button>
    </form>
    <br>
  </div>
</template>

<script>
  import Alert from '../staticPages/Alert';
  import('../../assets/css/main.scss');
  export default {
    data() {
      return {
        answer: "",
        name: "",
        gameCode: "",
        alert: '',
        question: "",
        interval: ""
      }
    },

    methods: {
      checkGameState() {
        let requestData = {Action: "FetchState", "Code": this.$store.state.gameCode};
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          if (response.body.State === "Results") {
            window.clearInterval(window.interval);
            console.log("Moving to " + "/Results" + " from /Answering");
            this.$router.replace({path: this.$store.state.paths.awarding})
          }
          if (response.body.State === "Grading") {
            window.clearInterval(window.interval);
            console.log("Moving to " + "/Grading" + " from /Answering");
            this.$router.replace({path: this.$store.state.paths.chooseBestAnswer})
          }
        });
      },

      setIntervalThatChecksGameState() {
        window.interval = setInterval(this.checkGameState, 1000)
      },

      routeToWaitingScreen() {
        let requestData = {
          Action: "SubmitAnswer",
          "Code": this.$store.state.gameCode,

          "Name": this.$store.state.username,
          "Answer": this.answer
        };
        let q = "";
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          this.$router.replace({path: this.$store.state.paths.waitingScreen1})
        });
        window.clearInterval(window.interval);

        this.$router.replace({path: this.$store.state.paths.waitingScreen1})
      }
    },

    computed: {
      username() {
        return this.$store.state.username
      }
    },

    components: {
      Alert
    },

    created: function () {
      this.setIntervalThatChecksGameState();
      let requestData = {Action: "FetchState", "Code": this.$store.state.gameCode};
      this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
        console.log(response.body)
        this.$store.dispatch('setLastQuestion', response.body.Data);
        this.question = response.body.Data
      })
    }
  }
</script>

<style scoped>
</style>

