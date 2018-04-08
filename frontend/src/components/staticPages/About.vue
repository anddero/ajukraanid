/* eslint-disable */
<template>
  <div>
    <div class="jumbotron">
      <div class="container">
        <img id="menubutton" class="center-block" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/welcometoabout.png">
        <br>
        <p id="text">Mäng toimub mitmes seadmes korraga. On 2 vaadet: mängija ja host. Hosti ekraani pealt kuvatakse küsimus,
          millele kõik mängijad peavad vastama. Seejärel kui kõik on vastanud kuvatakse kõik vastused hosti ekraanil
          anonüümselt ning seejärel peavad kõik mängijad valima parima vastuse. Mida rohkem saab mängija hääli, seda
          rohkem saab ta punkte.</p>
      </div>
    </div>
    <div class="container">
      <br>
      <input type="image" id="menubutton" @click="routeToIndex()" src="http://dijkstra.cs.ttu.ee/~ailoop/tarkvara/pildid/backtomainmenu.png" class="btn center-block .btn-lg"/>
    </div>
  </div>
</template>

<script>

  export default {
    name: 'about',
    data() {
      return {
        show: true
      }
    },
    methods: {
      routeToIndex() {
        this.$router.replace({path: this.$store.state.paths.index})
      }
    }
  }
</script>

<style scoped>
</style>
