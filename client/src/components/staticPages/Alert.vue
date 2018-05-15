
<template>
  <div class="alert alert-warning alert-dismissible" role="alert">
    <button  v-on:mousedown="close" class="close" data-dismiss="alert" aria-label="Close"><span   aria-hidden="true">&times;</span>
    </button>
    {{message}}
  </div>
</template>

<script>
  import('../../assets/css/main.scss');
  export default {
    name: 'alert',
    props: ['message'],
    data () {
      return {}
    },
    methods: {
      close: function () {
       this.$emit('close-alert')
      }
    }
  }
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
z
