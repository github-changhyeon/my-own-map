<template>
  <div>
    <v-card width="800" class="mx-auto">
      <v-list three-line>
        <template>
          <v-list-item v-for="(item, index) in items" :key="index">
            <v-list-item-avatar>
              <v-img :src="item.avatar"></v-img>
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title v-html="item.content"></v-list-item-title>
              <CommentCreate v-if="isModify[index]" :index="index" @create-comment="checkUpdateComment" />
            </v-list-item-content>
            <v-btn v-if="!isModify[index]" @click="checkModify(index)">수정</v-btn>
            <v-btn v-if="!isModify[index]" @click="checkDeleteComment(item)">삭제</v-btn>
          </v-list-item>
          <v-divider :key="index" inset></v-divider>
        </template>
      </v-list>
    </v-card>
    <CommentCreate @create-comment="checkCreateComment" />
  </div>
</template>

<script>
import { getComment, createComment, deleteComment, updateComment } from '@/api/comment.js';
import CommentCreate from '@/views/article/CommentCreate.vue';
import jwt_decode from 'jwt-decode';

export default {
  name: 'CommentList',
  components: {
    CommentCreate,
  },
  data() {
    return {
      isModify: [],
      items: [
        {
          avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
          content: '밤까지 코딩 굳입니다.',
        },
        {
          avatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
          content: '참 아름다운 곳이군요',
        },
      ],
      comment: {
        uid: 0,
        content: '',
        articleNo: 0,
      },
    };
  },
  props: {
    index: Number,
    articleNo: Number,
  },
  methods: {
    setToken: function() {
      const token = localStorage.getItem('jwt');
      const config = {
        headers: {
          jwt: `${token}`,
        },
      };
      return config;
    },
    checkGetComment() {
      getComment(
        this.articleNo,
        (response) => {
          if (response.data.status) {
            this.items = response.data.object;
            for (let i = 0; i < this.items.length; i++) {
              this.isModify.push(false);
            }
            console.log(response.data.object, 'getcomment의 items');
            console.log('댓글 가져오기 성공.');
          } else {
            console.log('실패');
          }
        },
        (error) => {
          console.log(error);
          alert('댓글 가져오기 실패');
        }
      );
    },
    checkCreateComment(content) {
      // let tempContent = {
      //   avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
      //   content: content,
      // };
      // alert(content);
      this.comment.content = content;
      // this.items.push(tempContent);
      createComment(
        this.comment,
        (response) => {
          console.log(response, '작성성공');
          this.checkGetComment();
        },
        (error) => {
          console.log(error, '작성실패');
        }
      );
    },
    checkDeleteComment(item) {
      if (confirm('삭제하시겠습니까?') == true) {
        // OK
        const config = this.setToken();
        console.log(item, '삭제할comment');
        const commentNo = item.commentNo;
        deleteComment(
          commentNo,
          config,
          () => {
            // 기존 게시글로 이동하는거 만들기
            this.checkGetComment();
          },
          (error) => {
            console.log(error);
            alert('삭제 실패');
          }
        );
      } else {
        // Fail
        document.form.submit();
      }
    },
    checkUpdateComment(item) {
      const config = this.setToken();
      const comment = item;
      updateComment(
        comment,
        config,
        (response) => {
          console.log(response);
        },
        (error) => {
          console.log(error);
        }
      );
    },
    checkModify(index) {
      console.log(index);
      this.isModify[index] = true;
      console.log(this.isModify[index]);
    },
  },
  created() {
    this.checkGetComment();
    const token = localStorage.getItem('jwt');
    let uid = jwt_decode(token).uid;
    this.comment.uid = uid;
    this.comment.articleNo = this.articleNo;
  },
  watch: {},
};
</script>
