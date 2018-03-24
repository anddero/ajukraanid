<template>
  <div class="about container">
    <h1 class="page-header">About</h1>
    <p>Mäng toimub mitmes seadmes korraga. On 2 vaadet: mängija ja host. Hosti ekraani pealt kuvatakse küsimus,
      millele kõik mängijad peavad vastama. Seejärel kui kõik on vastanud kuvatakse kõik vastused hosti ekraanil
      anonüümselt ning seejärel peavad kõik mängijad valima parima vastuse. Mida rohkem saab mängija hääli, seda
      rohkem saab ta punkte.
    </p>
    <p>Version 1.Nils.2</p>

    <br>
    <button @click="routeTo('/')" type="button" class="btn btn-secondary center-block">Back to main menu</button>
  </div>
</template>

<script>
  export default {
    name: 'about',
    data() {
      return {}
    },
    methods: {
      routeTo(state) {
        console.log("Moving to " + state + " from /about");
        this.$router.replace({path: state})
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
