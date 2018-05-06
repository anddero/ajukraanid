<template>
  <div class="container">
    <div class="jumbotron">
      <h1>Bootstrap Tutorial</h1>
      <p>Bootstrap is the most popular HTML, CSS, and JS framework for developing responsive, mobile-first projects on the web.</p>
    </div>
    <p>This is some text.</p>
    <p>This is another text.</p>
  </div>
</template>

<script>
  import('../../assets/css/main.scss');
  export default {
    name: 'about',
    data() {
      return {
        interval: ''
      }
    },

    methods: {
      checkGameState() {
        let requestData = {Action: 'FetchState', 'Code': this.$store.state.gameCode};
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          if (response.body.State === 'Results') {
            window.clearInterval(window.interval);
            console.log('Moving to ' + '/Results' + ' from /waitingForOtherPlayers2')
            this.$router.replace('/results')
          }
        })
      },

      routeToIndex() {
        this.$router.replace('/')
      },

      setIntervalThatChecksGameState() {
        window.interval = setInterval(this.checkGameState, 1000)
      },
    },

    created: function () {
      let requestData = {Action: "GetQuestion", "Code": this.$store.state.gameCode}
      this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
        this.question = response.body.Data
      })
      this.setIntervalThatChecksGameState()
    }
  }

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>

