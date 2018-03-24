<template>
  <div id="registration">
    <h3 v-if="username === 'host'"> {{ question }}</h3>
    <Alert v-if="alert" v-bind:message="alert"/>
    <hr>
    <form v-on:submit.prevent="routeTo('/waitingScreen1')">
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
          if (response.body.State === "awarding") {
            window.clearInterval(window.interval);
            console.log("Moving to " + "/awarding" + " from /question");
            this.$router.replace({path: "/awarding"})
          }
          if (response.body.State === "chooseBestAnswer") {
            window.clearInterval(window.interval);
            console.log("Moving to " + "/chooseBestAnwer" + " from /question");
            this.$router.replace({path: "/chooseBestAnswer"})
          }
        });
      },

      setIntervalThatChecksGameState() {
        window.interval = setInterval(this.checkGameState, 1000)
      },

      routeTo(state) {
        let requestData = {
          Action: "SubmitAnswer",
          "Code": this.$store.state.gameCode,

          "Name": this.$store.state.username,
          "Answer": this.answer
        };
        let q = "";
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
        });
        window.clearInterval(window.interval);
        console.log("Moving to " + state + " from /question");
        this.$router.replace({path: state})
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
      let requestData = {Action: "GetQuestion", "Code": this.$store.state.gameCode};
      let q = "";
      this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
        this.question = response.body.Data
      });
    }
  }
</script>

<style scoped>
</style>
