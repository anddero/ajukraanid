<template>
  <div>


    <div class="imgcontainer">
      <img src="https://www.shareicon.net/data/2016/06/27/787465_man_512x512.png" alt="Avatar"  style="height: 230px; width: 230px;" class="avatar">
    </div>

    <div class="container">
      <label><b>Username</b></label>
      <input v-model="username" type="text" placeholder="Enter Username"  required  autocomplete="off">

      <label><b>Password</b></label>
      <input v-model="password" type="password" placeholder="Enter Password"  required  autocomplete="off">

      <button @click="login()">Login</button>
      <button @click="routeToIndex()">Back</button>
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
        username: '',
        password: ''
      }
    },
    methods: {
      routeToIndex() {
        localStorage.clear();
        window.clearInterval(window.interval);
        this.$store.state.Authorization = "";
        this.$store.state.username = "";
        this.$store.state.gameCode = 0;
        this.$store.state.questionNumber = 0;
        this.$store.state.lastQuestion = "";
        this.$store.state.registrations = [];
        this.$store.state.token = "";
        this.$router.replace('/')
      },
      login() {
        let requestData = {'username': this.username, 'password': this.password}
        console.info(this.$store.state.loginDestination)
        this.$http.post("http://18.188.242.2:8080/login", requestData)
          .then(response => {
            this.$store.dispatch('updateAuthorization', response.body.Authorization)
            this.$router.replace('/admin')

          })

      }

    },
    created: function () {

    }

  }
</script>

<style scoped>

  input[type=text], input[type=password] {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    box-sizing: border-box;
  }

  button {
    background-color: #4CAF50;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    cursor: pointer;
    width: 100%;
  }

  button:hover {
    opacity: 0.8;
  }

  .cancelbtn {
    width: auto;
    padding: 10px 18px;
    background-color: #f44336;
  }

  .imgcontainer {
    text-align: center;
    margin: 24px 0 12px 0;
  }

  img.avatar {
    width: 40%;
    border-radius: 50%;
  }

  .container {
    padding: 16px;
  }

  span.psw {
    float: right;
    padding-top: 16px;
  }

  /* Change styles for span and cancel button on extra small screens */
  @media screen and (max-width: 300px) {
    span.psw {
      display: block;
      float: none;
    }

    .cancelbtn {
      width: 100%;
    }
  }

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
