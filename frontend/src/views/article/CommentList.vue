<template>
  <div>
    <div>
      <h4 style="font-weight: bold">댓글 ({{ items.length }}개)</h4>
      <br />
    </div>
    <v-card width="800" class="mx-auto">
      <template>
        <v-list-item
          class="commentlist"
          v-for="(item, index) in items"
          :key="index"
        >
          <v-list-item-avatar>
            <v-img
              :src="`https://i4b107.p.ssafy.io/images/profileImages/${items[index].userDto.profileImagePath}`"
            ></v-img>
          </v-list-item-avatar>
          <v-list-item-content>
            {{ item.userDto.username }}
            <v-list-item-title
              v-if="!isModify[index]"
              v-html="item.content"
            ></v-list-item-title>
            <CommentCreate
              v-if="isModify[index]"
              :updateContent="item.content"
              :index="index"
              @create-comment="checkUpdateComment"
            />
          </v-list-item-content>
          <v-btn
            x-small
            v-if="!isModify[index] && uid === item.userDto.uid"
            @click="checkModify(index)"
            ><v-icon small>mdi-pencil-outline</v-icon></v-btn
          >
          <v-btn
            x-small
            v-if="!isModify[index] && uid === item.userDto.uid"
            @click="checkDeleteComment(item)"
            ><v-icon small>mdi-trash-can</v-icon></v-btn
          >
        </v-list-item>
      </template>
    </v-card>
    <CommentCreate @create-comment="checkCreateComment" />
  </div>
</template>

<script>
import constants from '@/lib/constants.js';
import {
  getComment,
  createComment,
  deleteComment,
  updateComment,
} from '@/api/comment.js';
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
        // {
        //   avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
        //   content: '밤까지 코딩 굳입니다.',
        // },
        // {
        //   avatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
        //   content: '참 아름다운 곳이군요',
        // },
      ],
      comment: {
        uid: 0,
        content: null,
        articleNo: 0,
      },
      uid: 0,
    };
  },
  props: {
    index: Number,
    articleNo: [Number, String],
    propsUid: Number,
  },
  methods: {
    setToken: function () {
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
          }
        },
        (error) => {
          console.log(error);
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
        () => {
          this.checkGetComment();
          let body = {
            uid: this.propsUid,
            articleNo: this.articleNo,
            message: 'COMMENT',
          };
          notifyAction(
            body,
            (success) => {
              if (!success.data.status) {
                console.log('알림을 할 수 없습니다.');
              }
            },
            (error) => {
              console.log(error);
            }
          );
        },
        (error) => {
          console.log(error);
        }
      );
    },
    checkDeleteComment(item) {
      if (confirm('삭제하시겠습니까?') == true) {
        // OK
        const config = this.setToken();
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
    goToMyPage(item) {
      this.$router.push({
        name: constants.URL_TYPE.USER.MYPAGE,
        params: { uid: item.userDto.uid },
      });
    },
  },
  created() {
    this.checkGetComment();
    const token = localStorage.getItem('jwt');
    let uid = jwt_decode(token).uid;
    this.uid = uid;
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
