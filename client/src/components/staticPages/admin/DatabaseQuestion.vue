<template>
  <div class="card" style="margin-bottom: 5vh">


    <div id="collapseOne" class="collapse show" aria-labelledby="headingOne" data-parent="#accordion">
      <div class="card-body" v-if="editing===false">
        {{question}}
        <button type="button"  v-on:click="deleteQuestion()" class="btn btn-danger pull-right">Delete</button>
        <button type="button" v-on:click="startEditing()" class="btn btn-success pull-right" style="margin-right: 1vw">Edit</button>
      </div>

      <div class="card-body" v-if="editing===true">

        {{question}}<br>
        <input type="text" class="form-control" id="usr" style="margin-bottom: 1vh"  v-model="newText">
        <button type="button"  v-on:click="updateQuestionText()" class="btn btn-danger pull-left" style="margin-right: 1vw">Save</button>
        <button type="button"  v-on:click="stopEditing()" class="btn btn-success pull-left">Cancel</button>
      </div>

    </div>
  </div>
</template>

<script>


  export default {
    name: 'databaseQuestion',
    props: ['question'],
    data() {
      return {
        editing: false,
        newText: "",
        questions: ["If donald Trump and Vladimir Putin met, what would they to eachother?",
          "if a horse and a duck had a child, what would you name it?"]
      }
    },
    methods: {
      startEditing() {
        this.editing = true
      },
      stopEditing() {
        this.editing = false
      },
      routeToIndex() {
        this.$router.replace('/')
      },
      updateQuestionText() {
        let requestData = {Action: 'updateQuestion', oldText: this.question, newText: this.newText}
        console.info("Admin sent request: " + requestData)
        this.$http.put(this.$store.state.requestDestination, requestData).then(function (response) {
          // Should handle response somehow
        })
      },
      deleteQuestion() {
        let requestData = {data: this.question}
        this.$http.delete(this.$store.state.requestDestination, requestData).then(function (response) {
          // Should handle response somehow
        })
      }

    },
    created: function() {
      //SHOULD GET THE QUESTIONS FROM DATABASE
      // let requestData = {Action: 'getQuestions'}
      // this.$http.get(this.$store.state.requestDestination, requestData).then(function (response) {
      //   this.questions = response.body.Data;
      // })
    }

  }
</script>

<style scoped>



</style>
