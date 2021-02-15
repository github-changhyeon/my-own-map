<template>
  <v-card>
    <v-card-title style="height:150px;">
      <v-text-field v-model="content" outlined label="댓글 작성하기" @keypress.enter="createComment" hide-details>
        <template v-slot:append>
          <v-btn depressed tile color="primary" class="createbutton" @click="createComment">작성</v-btn>
        </template>
      </v-text-field>
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
      content: null,
    };
  },
  methods: {
    createComment() {
      if (this.content !== null) {
        console.log(this.content, typeof this.content);
        this.$emit('create-comment', this.content);
        this.content = '';
      }
    },
  },
};
</script>

<style scoped>
.createbutton {
  margin-bottom: 20px;
  border-radius: 3px;
}
</style>
