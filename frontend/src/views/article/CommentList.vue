<template>
  <div>
    <div>
      <h4 style="font-weight: bold ">댓글 ({{ items.length }}개)</h4>
      <br />
    </div>
    <v-card width="800" class="mx-auto">
      <template>
        <v-list-item class="commentlist" v-for="(item, index) in items" :key="index">
          <v-list-item-avatar>
            <v-img :src="item.userDto.profileImagePath"></v-img>
          </v-list-item-avatar>
          <v-list-item-content>
            {{ item.userDto.username }}
            <v-list-item-title v-if="!isModify[index]" v-html="item.content"></v-list-item-title>
            <CommentCreate v-if="isModify[index]" :updateContent="item.content" :index="index" @create-comment="checkUpdateComment" />
          </v-list-item-content>
          <v-btn x-small v-if="!isModify[index]" @click="checkModify(index)"><v-icon small>mdi-pencil-outline</v-icon></v-btn>
          <v-btn x-small v-if="!isModify[index]" @click="checkDeleteComment(item)"><v-icon small>mdi-trash-can</v-icon></v-btn>
        </v-list-item>
      </template>
    </v-card>
    <CommentCreate @create-comment="checkCreateComment" />
  </div>
</template>

<script>
import { getComment, createComment, deleteComment, updateComment } from '@/api/comment.js';
import CommentCreate from '@/views/article/CommentCreate.vue';
import { notifyAction } from '@/api/fcm.js';
import jwt_decode from 'jwt-decode';

export default {
  name: 'CommentList',
  components: {
    CommentCreate,
  },
  data() {
    return {
      isModify: [],
      commentDto: {},
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
        content: null,
        articleNo: 0,
      },
    };
  },
  props: {
    index: Number,
    articleNo: [Number, String],
    propsUid: Number,
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
            this.isModify = [];
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
          let body = {
            // uid: this.propsUid,
            uid: jwt_decode(localStorage.getItem('jwt')).uid,
            articleNo: this.articleNo,
            message: 'COMMENT',
          };
          notifyAction(
            body,
            (success) => {
              if (success.data.status) {
                console.log('알림 ok');
              } else {
                console.log('알림을 할 수 없습니다.');
              }
            },
            (error) => {
              console.log(error);
              alert('서버에러');
            }
          );
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
    checkUpdateComment(content) {
      const config = this.setToken();
      // const comment = content;
      this.commentDto.content = content;
      // item.preventDefault();
      updateComment(
        this.commentDto,
        config,
        () => {
          console.log(this.commentDto, '수정후dto');

          this.checkGetComment();
        },
        (error) => {
          console.log(error);
        }
      );
    },
    checkModify(index) {
      this.isModify.splice(index, 1, true);
      this.commentDto.commentNo = this.items[index].commentNo;
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

<style scoped>
.commentlist {
  border-bottom: 1px solid rgb(209, 209, 209);
}
/* h4:hover {
  cursor: pointer;
} */
</style>
