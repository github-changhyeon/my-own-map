<template>
  <v-card>
    <v-card-title style="height:150px;">
      <v-textarea @keydown.enter.exact="createComment" @keydown.enter.shift.exact="newline" auto-grow rows="1" v-model="content" outlined label="댓글 작성하기" hide-details>
        <template v-slot:append>
          <v-btn depressed tile color="primary" class="createbutton" @click="createComment">작성</v-btn>
        </template>
      </v-textarea>
    </v-card-title>
  </v-card>
</template>

<script>
export default {
  name: 'CommentCreate',
  props: {
    // index: Number,
    updateContent: String,
  },
  data() {
    return {
      content: null,
    };
  },
  methods: {
    createComment() {
      if (this.content !== null && this.content !== undefined) {
        this.$emit('create-comment', this.content);
        this.content = '';
      }
    },
    newline() {
      this.content = `${this.content}\n`;
    },
  },
  created() {
    this.content = this.updateContent;
  },
};
</script>

<style scoped>
.createbutton {
  margin-bottom: 20px;
  border-radius: 3px;
}
</style>
