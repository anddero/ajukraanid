
<template>
  <div id="registration">
    <h1 id="question" class="center-block" style="padding-right: 15vw;"> {{ question }}</h1>
    <Alert v-if="alert" v-bind:message="alert"/>
    <hr>
    <form v-on:submit.prevent="routeToWaitingScreen()" style="width: 40%; text-align: center; margin-left: 30%;">
      <div  v-if="username !== 'host'" class="well">
        <br/>
        <input type="text" class="form-control" placeholder="Answer" v-model="answer"  pattern="\w+|\s*\w+|\w+\s*" required="required">
      </div>
      <b id="countdown2">{{timeLeft}} seconds remaining</b>
      <button v-if="username !== 'host'" style="background-color: transparent; outline: none;" type="submit" class="btn center-block .btn-lg"><img id='menubutton4' class='btn center-block .btn-lg' src='http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/submit.png'></button>
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
        timeLeft: 30,
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
        let requestData = {Action: "FetchState", "Code": this.$store.state.gameCode, "Token": this.$store.state.token};
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          console.log("Question - checkGameState: ");
          console.log(response);
          if (response.body.State === "Inactive") {
            localStorage.clear()
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
          if (response.body.State === "Results") {
            window.clearInterval(window.interval);
            console.log("Moving to " + "/Results" + " from /Answering");
            this.$router.replace('/results')
          }
          if (response.body.State === "Grading") {
            window.clearInterval(window.interval);
            console.log("Moving to " + "/Grading" + " from /Answering");
            this.$router.replace('/grading')
          } else {
            this.timeLeft = response.body.Data.TimeLeft
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
          "Answer": this.answer,
          "Token": this.$store.state.token
        };
        let q = "";
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          console.log("Question - routeToWaitingScreen: ");
          console.log(response);
          this.$router.replace('/waitingScreen1')
        });
        window.clearInterval(window.interval);

        this.$router.replace('/waitingScreen1')
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
      let requestData = {Action: "FetchState", "Code": this.$store.state.gameCode, "Token": this.$store.state.token};
      this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
        console.log("Question - created: ");
        console.log(response);
        this.$store.dispatch('setLastQuestion', response.body.Data.Question);
        this.question = response.body.Data.Question
      })
    }
  }
</script>

<style scoped>
</style>

