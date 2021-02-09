<template>
  <v-card>
    <v-card-title>
      <v-text-field v-model="contents" style="height:150px;" outlined label="댓글 작성하기" @keypress.enter="createComment" hide-details></v-text-field>
      <v-btn @click="createComment">댓글 작성</v-btn>
    </v-card-title>
    <div>

    </div>
  </v-card>
</template>

<script>
export default {
  name:'Comment',
  props: {
    index: Number,
  },
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

<style scoped>


</style>