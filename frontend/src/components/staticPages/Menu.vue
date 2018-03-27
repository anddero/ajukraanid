/* eslint-disable */
<template>
  <div id="menu" class="container">
    <img src="../../assets/logo.png" class="center-block">
    <button @click="routeTo('/newGameConfirmation')" class="btn btn-primary center-block .btn-lg">Create a new game.
    </button>
    <br/>
    <button @click="routeTo('/registration')" class="btn btn-primary center-block .btn-lg">Join a game</button>
    <br/>
    <button @click="routeTo('/about')" class="btn btn-primary center-block .btn-lg">About</button>
  </div>
</template>
<script>

  export default {
    methods: {
      routeTo(state) {
        console.log("Moving to " + state + " from /menu");
        this.$router.replace({path: state})
      }
    },
    watch: {
      '$route'(to, from) {
        const toDepth = to.path.split('/').length
        const fromDepth = from.path.split('/').length
        this.transitionName = toDepth < fromDepth ? 'slide-right' : 'slide-left'
      }
    }
  }
</script>
<style scoped>


</style>
