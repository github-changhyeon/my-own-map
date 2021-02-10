<template>
  <div>
    <v-card
        width="800"
        class="mx-auto"
      >
        <v-list three-line>
          <template v-for="(item, index) in items">
            <v-list-item
              :key="index"
            >
              <v-list-item-avatar>
                <v-img :src="item.avatar"></v-img>
              </v-list-item-avatar>
              <v-list-item-content>
                <v-list-item-title v-html="item.content"></v-list-item-title>
              </v-list-item-content>
              <v-btn @click="updateComment">수정</v-btn>
              <v-btn @click="checkDeleteComment">삭제</v-btn>
            </v-list-item>
            <v-divider
            :key="index"
            inset
            ></v-divider>
          </template>
        </v-list>
      </v-card>
      <CommentCreate 
      :index="index"
      @create-comment="checkCreateComment"  
      />
    </div>
</template>

<script>
import { getComment, createComment, deleteComment } from '@/api/comment.js'
import CommentCreate from '@/views/article/CommentCreate.vue';

export default {
  name: 'CommentList',
  components: { 
    CommentCreate,
  },
  data() {
    return {
      items: [
        {
          avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
          content: '밤까지 코딩 굳입니다.',
        },
        {
          avatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
          content: '참 아름다운 곳이군요',
        },
      ]
    }
  },
  props: {
    index: Number,
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
    checkGetComment(articleNo) {
      getComment(
      articleNo,
      (response) => {
        if (response.data.status) {
          this.items = response.data.object;
          console.log(this.items)
          console.log('댓글 가져오기 성공.');
        } else {
          console.log('실패')
        }
      },
      (error) => {
        console.log(error);
        alert('댓글 가져오기 실패');
      })
    },
    checkCreateComment(content) {
      let tempContent = {
          avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
          content: content
        }
      this.items.push(tempContent)
      createComment(
        content, 
        (response) => {
          console.log(response)
        },
        (error) => {
          console.log(error)
        }
      )  
    },
    checkDeleteComment() {
      if (confirm('삭제하시겠습니까?') == true) {
        // OK
        deleteComment(
          this.items.commentNo,
          () => {
            // 기존 게시글로 이동하는거 만들기
          },
          (error) => {
            console.log(error);
            alert('삭제 실패');
          }
        )
      } else {
        // Fail
        document.form.submit();
      }
    },
    updateComment() {
      

    },
  },
  created() {
    this.checkGetComment()
  },
  watch: {

  }
}
</script>