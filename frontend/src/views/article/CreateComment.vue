<template>
  <div>
    <input type="text">
    <button @click="crateComment">댓글 작성</button>
  </div>
</template>

<script>
export default {
  name:'CreateComment',
  data() {
    return {
      contents: '',
    }
  },
  methods: {
    createComment() {
      this.$emit('create-comment', this.contents)
      this.contents = ''
    }
  }
}
</script>

<style>

</style>