<template>
  <div>
    <div v-for="(comment, idx) in comments" :key="idx">
      <button></button>
    </div>
    <CreateComment
      @create-comment="createComment"
    />
  </div>
</template>

<script>
import CreateComment from './CreateComment.vue'

export default {
  name:Comment,
  components: {
    CreateComment,
  },
  data() {
    return {

    }
  },
  methods: {
    getComment() {

    },
    createComment() {

    },
    deleteComment() {
      
    }
  },
  created() {
    this.getComment()
  }
}
</script>

<style>

</style>