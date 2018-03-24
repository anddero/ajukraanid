<template>
  <div id="summary" class="center-block">
    <h3>Join game</h3>
    <Alert v-if="alert" v-bind:message="alert"/>
    <hr>
    <form v-on:submit.prevent="registerUser">
      <div class="well">
        <input type="text" class="form-control" placeholder="Nickname" v-model="name">
        <br/>
        <input type="text" class="form-control" placeholder="Gamecode" v-model="gameCode">
      </div>
      <button type="submit" class="btn btm-success center-block">Join</button>
      <br>
      <button @click="routeTo('/')" type="button" class="btn btn-secondary center-block">Back to main menu</button>
    </form>
  </div>

</template>

<script>
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
      routeTo(state) {
        console.log("Moving to " + state + " from /joinGame");
        this.$router.replace({ path: state })
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
                this.$store.dispatch('register', myuser);
                this.$store.dispatch('setGameCode', this.$data.gameCode);
                this.$store.dispatch('setMyUsername', this.$data.name);
                console.log("Moving to " + "/lobby" + " from /joinGame");
                this.$router.replace({path: '/lobby'})
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
  #registration {
    box-shadow: 1px 1px 2px 1px #ccc;
    margin: 20px;
    padding: 20px;
    display: inline-block;
    width: 300px;
    vertical-align: top;
  }

  .row h4 {
    display: inline-block;
    width: 70%;
    text-align: left;
    margin: 0 0 10px 0;
  }

  button {
    background-color: lightgreen;
    border: none;
    box-shadow: 1px 1px 1px black;
    font-size: inherit;
    text-align: right;
    cursor: pointer;
  }

  button:hover {
    background-color: green;
  }
</style>
