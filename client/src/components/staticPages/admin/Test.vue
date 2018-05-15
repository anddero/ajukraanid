
<template>
  <div id="summary">
    <div class="summary">
      <img id="menubutton1" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/results.png" style="margin-top: 2%;"/>
      <h5></h5>
    </div>
    <hr>
    <div >
      <column-chart :library="{backgroundColor: '#eee'}" xtitle="Players" ytitle="Points" :colors="['#8b0000', '#666']" :discrete="true" :data="pointsForGraph" style="height: 65vh"></column-chart>
    </div>
    <br>
    <br/>
  </div>

</template>

<script>
  import('../../../assets/css/main.scss')
  import {VueChartkick} from 'vue-chartkick'
  export default {
    data () {
      return {
        interval: '',
        points: '',
        pointsForGraph: [["Nils", 2000],["4693", 100],["469346934693", 0]]
    }
    },


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
