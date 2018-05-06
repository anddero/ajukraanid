<template>
  <div class="container">
    <div class="jumbotron">
      <h1>Admin panel</h1>
      <p>You can edit old and save new questions here.</p>
    </div>
    <div class="input-group" style="margin-bottom: 2vh">
      <input type="text" class="form-control" v-model="message">
      <span class="input-group-btn">
        <button  v-on:click="createQuestion()" type="button" class="btn btn-primary">Add new question</button>
      </span>
    </div>
    <div v-for="question in questions">
      <question :question="question"></question>
    </div>

  </div>
</template>

<script>
  import Question from '../admin/DatabaseQuestion'

  export default {
    name: 'about',
    components: {
      Question
    },
    data() {
      return {
        message: "",
        questions: ["If donald Trump and Vladimir Putin met, what would they to eachother?",
          "if a horse and a duck had a child, what would you name it?"]
      }
    },
    methods: {
      routeToIndex() {
        this.$router.replace('/')
      },
      createQuestion() {
        let requestData = {Action: "CreateQuestion", data: this.message}
        this.$http.post(this.$store.state.requestDestination, requestData).then(function (response) {
          // Should handle response somehow
        })
      }

    },
    created: function () {
      let requestData = {Action: 'getQuestions'}
      this.$http.get(this.$store.state.requestDestination, requestData).then(function (response) {
        this.questions = response.body.Data;
      })
    }

  }
</script>

<style scoped>


  .jumbotron {
    background: #eee !important;
    padding-top: 30px !important;
    padding-bottom: 30px !important;
    margin-bottom: 30px !important;
  }

  .btn {
    display: inline-block;
    padding: 6px 12px;
    margin-bottom: 0;
    font-size: 14px;
    font-weight: normal;
    line-height: 1.42857143;
    text-align: center;
    white-space: nowrap;
    vertical-align: middle;
    -ms-touch-action: manipulation;
    touch-action: manipulation;
    cursor: pointer;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    background-image: none;
    border: 1px solid transparent;
    border-radius: 4px;
  }

  .btn:hover {
    transform: scale(1);
  }

  body {
    font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    font-size: 14px;
    line-height: 1.42857143;
    color: #333;
    background-color: #fff;
    background-image: none !important;
  }

  #text {
    font-family: "Comic Sans MS", cursive, sans-serif;
    font-style: italic;
    font-weight: bold;
    width: 85%;
    padding-left: 15%;
    font-size: 25px;
    text-align: justify;
  }

  #menu img {
    margin-top: 50px;
    width: 70%;
    height: auto;
  }

  #lobbytop {
    padding-left: 30%;
    padding-top: 2%;
  }

  .code {
    font-size: 400%;
  }

  .col {
    font-family: "Comic Sans MS", cursive, sans-serif;
    font-style: italic;
    font-weight: bold;
    font-size: 25px;
    margin-left: 50%;
  }


</style>
