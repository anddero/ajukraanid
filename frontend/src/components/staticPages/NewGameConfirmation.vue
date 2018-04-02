/* eslint-disable */
<template>
  <div class="add container">
    <Alert v-if="alert" v-bind:message="alert"/>
    <h1 class="page-header text-center">Are you sure you want to create a new game?</h1>
    <form v-on:submit="createGame">
      <button type="submit" class="btn btn-primary center-block">Yes</button>
    </form>
    <br>
    <button @click="routeToIndex()" type="button" class="btn btn-primary center-block .btn-lg">Back to main menu
    </button>
    <br>

  </div>
</template>

<script>
  import('../../assets/css/main.scss');
  import Alert from './Alert'

  export default {
    props: ['gameCode'],
    name: 'newGame',
    data () {
      return {
        alert: ""
      }
    },
    methods: {
      routeToIndex() {
        this.$router.replace({path: this.$store.state.paths.index})
      },

      createGame (e) {
        this.$store.dispatch('setMyUsername', 'host')
        let requestData = {Action: "CreateGame"}
        this.$http.post(this.$store.state.requestDestination, requestData)
          .then(function (response) {
            this.$store.dispatch('setGameCode', response.body.Code)
            this.$router.replace({path: this.$store.state.paths.lobby})
          })
        e.preventDefault()
      }
    },

    components: {
      Alert
    },

    created: function () {
      let requestData = {Action: 'GetQuestion', 'Code': this.$store.state.gameCode}
      this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
        this.question = response.body.Data
      })
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
