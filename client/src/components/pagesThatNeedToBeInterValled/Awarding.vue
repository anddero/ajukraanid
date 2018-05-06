
<template>
  <div id="summary">
    <div class="summary">
      <img id="menubutton1" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/results.png" style="margin-top: 2%;"/>
      <h5></h5>
    </div>
    <hr>
    <div >
      <column-chart :data="pointsForGraph"></column-chart>
    </div>
    <br>
    <br/>
  </div>

</template>

<script>
  import('../../assets/css/main.scss')
  import {VueChartkick} from 'vue-chartkick'
  export default {
    data () {
      return {
        interval: '',
        points: '',
        pointsForGraph: []
      }
    },

    methods: {
      checkGameState () {
        let requestData = {Action: 'FetchState', 'Code': this.$store.state.gameCode}
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          if (response.body.State === 'Answering') {
            window.clearInterval(window.interval)
            console.log('Moving to ' + '/question' + ' from /Answering')
            this.$router.replace('/question')
          }
        })
      },

      setIntervalThatChecksGameState () {
        window.interval = setInterval(this.checkGameState, 1000)
      }
    },

    computed: {
      registrations () {
        return this.$store.state.registrations
      },
      gameCode () {
        return this.$store.state.gameCode
      }
    },

    created: function () {
      this.setIntervalThatChecksGameState()
      let requestData = {Action: 'FetchState', 'Code': this.gameCode}
      this.$http.post(this.$store.state.requestDestination, requestData)
        .then(function (response) {
          this.points = response.body.Data.Scores
          console.log(this.points)
          this.points.forEach(element => {
            console.info('Element:')
            console.info(element)
            let el = []
            el.push(element.Name)
            el.push(element.Points)
            this.pointsForGraph.push(el)
          })
          console.log(this.pointsForGraph)
        })
    }
  }
</script>

<style scoped>
  .summary {
    text-align: center;
  }

  .row h4 {
    display: inline-block;
    width: 30%;
    margin: 0 0 10px 0;
    box-sizing: border-box;
  }

  .row span {
    width: 30%;
    color: red;
    cursor: pointer;
  }

  .row span:hover {
    color: darkred;
  }

</style>
