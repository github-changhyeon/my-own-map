<template>
  <v-card>
    <v-card-title style="height:200px;">
      <v-text-field v-model="content" height="150" outlined label="댓글 작성하기" @keypress.enter="createComment" hide-details> </v-text-field>
      <v-btn @click="createComment">댓글 작성</v-btn>
    </v-card-title>
  </v-card>
</template>

<script>
export default {
  name: 'CommentCreate',
  props: {
    // index: Number,
  },
  data() {
    return {
      content: '',
    };
  },
  methods: {
    createComment() {
      this.$emit('create-comment', this.content);
      this.content = '';
    },
  },
};
</script>

<style scoped></style>
