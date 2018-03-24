<template>
  <div id="menu" class="container">

      <img src="../../assets/logo.png" class="center-block">

    <button @click="routeTo('/newGameConfirmation')" class="btn btn-primary center-block">Create a new game.</button>
    <br/>
    <button @click="routeTo('/registration')" class="btn btn-primary center-block">Join a game</button>
    <br/>
    <button @click="routeTo('/about')" class="btn btn-primary center-block">About</button>
  </div>
</template>

<script>
  import Alert from './Alert'
  export default {
    methods: {
      routeTo(state) {
        console.log("Moving to " + state + " from /menu");
        this.$router.replace({ path: state })
      }
    },
  }
</script>
