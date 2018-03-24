<template>
  <div class="add container">
    <Alert v-if="alert" v-bind:message="alert"/>
    <h1 class="page-header text-center">Are you sure you want to create a new game?</h1>
    <form v-on:submit="createGame">
      <button type="submit" class="btn btn-primary center-block">Yes</button>
    </form>
    <br>

  </div>
</template>

<script>
  import Alert from './Alert'

  export default {
    props: ['gameCode'],
    name: 'newGame',
    data() {
      return {
        alert: ""
      }
    },
    methods: {
      routeTo(state) {
        console.log("Moving to " + state + " from /newGameConfirmation");
        this.$router.replace({ path: state })
      },

      createGame(e) {
        this.$store.dispatch('setMyUsername', "host");
        let requestData = {Action: "CreateGame",};
        this.$http.post(this.$store.state.requestDestination, requestData)
          .then(function (response) {
            this.$store.dispatch('setGameCode', response.body.Code);
            console.log("Moving to " + "/lobby" + " from /newGameConfirmation");
            this.$router.replace({path: "/lobby"})
          });
        e.preventDefault();
      }
    },

    components: {
      Alert,
    },

    created: function () {
      let requestData = {Action: "GetQuestion", "Code": this.$store.state.gameCode};
      this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
        this.question = response.body.Data;
      })
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
