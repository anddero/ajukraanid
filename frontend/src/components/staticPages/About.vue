/* eslint-disable */
<template>
  <div>
    <div class="jumbotron">
      <div class="container">
        <h1 class="display-3">Welcome to about!</h1>
        <p>Mäng toimub mitmes seadmes korraga. On 2 vaadet: mängija ja host. Hosti ekraani pealt kuvatakse küsimus,
          millele kõik mängijad peavad vastama. Seejärel kui kõik on vastanud kuvatakse kõik vastused hosti ekraanil
          anonüümselt ning seejärel peavad kõik mängijad valima parima vastuse. Mida rohkem saab mängija hääli, seda
          rohkem saab ta punkte.</p>
      </div>
    </div>
    <div class="container">
      <br>
      <button @click="routeToIndex()" type="button" class="btn btn-primary center-block .btn-lg">Back to main menu
      </button>
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
  @import "../../assets/css/main.css";
</style>
