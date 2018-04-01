<template>
  <div id="app">
    <router-view></router-view>
  </div>

</template>

<script>
  export default {
    name: 'App'

  };
</script>

<style>
  body {

  }

  .btn {
    background-color: #321180;
    border-color: Transparent;
    font-family: 'Roboto', sans-serif;
  }

  .btn:hover {
    transform: scale(1.25);
    -webkit-transition: transform .35s ease-in-out;
    border-color: Transparent;
    background-color: #321180;
  }
  p, .display-3 {
    font-family: 'Roboto', sans-serif;
  }
  body {
    width: 100%;
  }



</style>
