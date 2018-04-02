/* eslint-disable */
<template>
  <div id="summary" class="center-block">

    <div class="jumbotron">
      <div class="container">
        <h1 class="display-3">Join an existing game</h1>
        <p>Enter a nickname and the gamecode that is shown in the host lobby. Remember to have fun!</p>
      </div>
    </div>
    <div class="container">
      <Alert v-if="alert" v-bind:message="alert"/>
      <hr>
      <form v-on:submit.prevent="registerUser">
        <div class="well">
          <input type="text" class="form-control" placeholder="Nickname" v-model="name">
          <br/>
          <input type="text" class="form-control" placeholder="Gamecode" v-model="gameCode">
        </div>
        <button type="submit" class="btn btn-primary center-block .btn-lg">Join</button>
        <br>
        <button @click="routeToIndex()" type="button" class="btn btn-primary center-block .btn-lg">Back to main menu
        </button>
      </form>
    </div>
  </div>

</template>

<script>
  import('../../assets/css/main.scss');
  import Alert from './Alert';

  export default {
    data() {
      return {
        name: "",
        gameCode: "",
        alert: ''
      }
    },
    methods: {
      routeToIndex() {
        this.$router.replace({path: this.$store.state.paths.index})
      },
      registerUser() {
        let myuser = {name: 'Max', gameCode: "gameCode"};
        myuser.name = this.$data.name;
        myuser.gameCode = this.$data.gameCode;
        let requestData = {Action: "JoinGame", "Code": this.$data.gameCode, "Name": this.$data.name};
        if (!this.$data.name || !this.$data.gameCode) {
          this.alert = 'Please fill in all required fields';
        } else {
          this.$http.post(this.$store.state.requestDestination, requestData)
            .then(function (response) {
              if (response.body.Data === "Did not find such game with game code: " + this.$data.gameCode) {
                this.alert = "Game code was not found.";
              } else if (response.body.Data === "Such username is already taken.") {
                this.alert = "Such username is already taken.";
              } else {
                this.$store.dispatch('setGameCode', this.$data.gameCode);
                this.$store.dispatch('setMyUsername', this.$data.name);
                this.$router.replace({path: this.$store.state.paths.lobby})
              }
            });
        }
      }
    },
    components: {
      Alert
    }
  }
</script>

<style scoped>
</style>
