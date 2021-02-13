<!-- 순서 : 등록된 사진 캐러샐, 평점, 해쉬태그, 장소이름(제목), 설명, 주소, 날짜, 댓글 폼  -->
<!-- 게시글 수정, 삭제 (현재 페이지에서 바로가능하게) -->
<!-- 현재는 라우터로 구현. 메인에서 누를 시 articleNo로 라우터푸시  -->
<!-- comment 데이터를 axios 요청으로 받아올 거라 게시글 상세정보도 axios로 받아옴  -->
<!-- 휴대폰 화면으로 잘 뜨는지 확인 -->
<template>
  <v-card>
    <v-app>
      <div class="detail-main">
        <h1 class="main-title">게시글 상세내용</h1>
        <v-btn icon color="black" @click="goBack">
          <v-icon>mdi-arrow-left</v-icon>
        </v-btn>
        <KakaoSharing :article="article" />
        <Favorite :article="article" />
      </div>

      <hr class="line" />
      <div class="total-contents">
        <div>
          <!-- 사진 -->
          <v-carousel>
            <v-carousel-item
              v-for="(item, i) in items"
              :key="i"
              :src="item.src"
              append
              reverse-transition="fade-transition"
              transition="fade-transition"
              multiple="true"
            ></v-carousel-item>
          </v-carousel>
        </div>
        <v-rating
          v-model="this.article.evaluation"
          background-color="orange lighten-3"
          color="orange"
          half-increments
          length="5"
          readonly
          large
        >
        </v-rating>
        <!-- <div>
          <label for="title"><strong>작성자</strong> | </label>
          {{ this.$route.query.user }}
        </div> -->
        <!-- <div>
          <label for="title"><strong>수정일자</strong> | </label>
          {{ this.$route.query.updated_at | moment('YYYY-MM-DD h:mm:ss a') }}
        </div> -->
        <!-- <v-col md="4" offset-md="4">
          <v-combobox v-model="{{article.hashtags}}" :items="items" multiple chips>
            <template v-slot:selection="data">
              <v-chip :key="JSON.stringify(data.item)" v-bind="data.attrs" :input-value="data.selected" :disabled="data.disabled" @click:close="data.parent.selectItem(data.item)">
                <v-avatar class="accent white--text" left v-text="data.item.slice(0, 1).toUpperCase()"></v-avatar>
                {{ data.item }}
              </v-chip>
            </template>
          </v-combobox>
        </v-col> -->
        <div>
          <!-- hashtags -->
          <label for="title"><strong>해쉬태그</strong> | </label>
          <span v-for="(hashtag, idx) in article.hashtags" :key="idx">
            #{{ hashtag.hashtagName }}
          </span>
        </div>
        <div>
          <label for="title"><strong>작성일자</strong> | </label>
          {{ article.regiTime }}
        </div>
        <div>
          <label for="title"><strong>주소</strong> | </label>
          {{ article.address }}
        </div>
        <div>
          <label for="title"><strong>제목</strong> | </label>
          <b>{{ article.title }}</b>
        </div>
        <div class="content-total">
          <label for="content"><strong>내용</strong> </label>
          <h6>{{ article.contents }}</h6>
        </div>
      </div>

      <div class="buttons" v-if="isOwnArticle">
        <!-- <div class="buttons"  > -->
        <button variant="danger">
          <a
            href="javascript:;"
            @click="checkDelete"
            class="btn"
            style="color: black"
            >삭제</a
          >
        </button>
        <button variant="outline-primary">
          <a href="javascript:;" @click="goToUpdateArticle" class="btn">수정</a>
        </button>
        <v-btn @click="findRoute">카카오맵 길찾기</v-btn>
      </div>
      <hr class="line" />
      <!-- <div>
        <h4 style="font-weight: bold">comment ({{ comments.length }}개)</h4>
        <br />
        <div>
          <ul v-for="(comment, idx) in comments" :key="idx">
            <li>
              <strong>🙍🏻‍♂️{{ comment.user }}</strong> - {{ comment.content }} | {{ comment.created_at | moment('YYYY-MM-DD h:mm:ss a') }}
              <button variant="outline-danger" @click="checkDeleteComment(comment)">X</button>
            </li>
          </ul>
        </div>
      </div>
    </div> -->
      <!-- <div>
      댓글 목록
      <Comment />
    </div> -->
      <CommentList
        style="margin-bottom: 50px"
        :articleNo="$route.params.articleNo"
      />
      <Navigation />
    </v-app>
  </v-card>
</template>

<script>
// import axios from 'axios';
import { getArticle } from '@/api/article.js';
import { deleteArticle } from '@/api/article.js';

import constants from '@/lib/constants';
import jwt_decode from 'jwt-decode';
import Navigation from '@/components/Navigation.vue';
import KakaoSharing from '@/components/sns/KakaoSharing.vue';
import Favorite from '@/components/user/Favorite.vue';
import CommentList from '@/views/article/CommentList.vue';

export default {
  name: 'ArticleDetail',
  props: {
    articleNo: [Number, String],
  },
  components: {
    Navigation,
    KakaoSharing,
    Favorite,
    CommentList,
  },
  data() {
    const index = this.$route.query.id;
    const Articles = this.$route.query;
    return {
      id: index,
      Articles: Articles,
      content: 'sample',
      comments: 'sample',
      commentId: Number,
      isOwnArticle: false,
      article: {
        address: '',
        articleNo: 0,
        contents: '',
        evaluation: 0,
        hashtags: [],
        positionLat: '',
        positionLng: '',
        regiTime: '',
        updateTime: '',
        title: '',
        userDto: {},
        imagePaths: null,
      },
      items: [],
    };
  },
  methods: {
    // setToken: function() {
    //   const token = localStorage.getItem('jwt');

    //   const config = {
    //     headers: {
    //       Authorization: `JWT ${token}`,
    //     },
    //   };
    //   return config;
    // },

    findRoute() {
      window.open(
        `https://map.kakao.com/link/to/${this.$route.params.article.address},${this.$route.params.article.positionLat},${this.$route.params.article.positionLng}`
      );
    },

    goBack() {
      // 뒤로가기
      this.$router.go(-1);
    },
    goToList() {
      // 홈으로
      this.$router.push('/');
    },

    goToUpdateArticle() {
      this.$router.push({
        name: constants.URL_TYPE.ARTICLE.UPDATEARTICLE,
        params: { articleNo: this.article.articleNo, article: this.article },
      });
    },

    checkDelete() {
      if (confirm('정말 삭제하시겠습니까?') == true) {
        //확인
        deleteArticle(
          this.article.articleNo,
          () => {
            // 메인으로
            this.$router.push({
              name: constants.URL_TYPE.HOME.MAIN,
              params: { uid: this.article.userDto.uid },
            });
            // this.goToList();
          },
          (error) => {
            console.log(error);
            alert('삭제 실패');
          }
        );
      } else {
        //취소
        document.form.submit();
      }
    },

    // getComment() {
    //   const config = this.setToken();
    //   axios.get(`http://127.0.0.1:8000/articles/${this.id}/comments/`, config).then((res) => {
    //     console.log('getcomment입니다');
    //     console.log(res);
    //     this.comments = res.data;
    //   });
    // },
    // createComment: function() {
    //   const config = this.setToken();

    //   const commentItem = {
    //     content: this.content,
    //   };

    //   if (commentItem.content) {
    //     axios
    //       .post(`http://127.0.0.1:8000/community/${this.id}/comments/`, commentItem, config)
    //       .then((res) => {
    //         console.log(res);

    //         this.content = '';
    //         this.getComment();
    //       })
    //       .catch((err) => {
    //         console.log(err);
    //       });
    //   }
    // },

    // deleteComment: function(comment) {
    //   const config = this.setToken();
    //   const commentId = comment.id;
    //   const reviewId = Number(this.id);
    //   console.log('delete요청');
    //   console.log(reviewId);
    //   console.log(commentId);

    //   axios
    //     .delete(`http://127.0.0.1:8000/community/${reviewId}/comments/delete/${commentId}/`, config)
    //     .then(() => {
    //       this.getComment();
    //     })
    //     .catch((err) => {
    //       console.log('comment delete 에러');
    //       console.log(err);
    //       alert('본인 글이 아닙니다');
    //     });
    // },

    // checkDeleteComment(comment) {
    //   if (confirm('정말 삭제하시겠습니까?🤷‍♂️') == true) {
    //     //확인
    //     this.deleteComment(comment);
    //   } else {
    //     //취소
    //     document.form.submit();
    //   }
    // },
  },

  created() {
    const token = localStorage.getItem('jwt');
    let uid = '';
    if (token !== null && token !== undefined) {
      uid = jwt_decode(token).uid;
    }
    for (var i = 0; i < this.article.imagePaths.length; ++i) {
      this.items.push({
        src:
          'https://i4b107.p.ssafy.io/images/uploads/' +
          this.article.imagePaths[i],
      });
    }
    if (this.article.imagePaths.length === 0) {
      this.items.push({
        src: 'https://cdn.vuetifyjs.com/images/cards/cooking.png',
      });
    }
    getArticle(
      this.$route.params.articleNo,
      (response) => {
        this.article = response.data.object;
        // console.log(this.article, 'article detail');
        if (
          token !== null &&
          token !== undefined &&
          this.article.userDto.uid === uid
        ) {
          this.isOwnArticle = true;
        }
        for (var i = 0; i < this.article.imagePaths.length; ++i) {
          this.items.push({
            src: '@/assets/upload/' + this.article.imagePaths[i],
          });
        }
      },
      (error) => {
        console.log(error);
      }
    );

    // TODO: 새로고침 했을 때 axios요청 생각해보기
    // if(this.$route.params.article === null){

    // }
  },
};
</script>
<style scoped>
.page {
  width: 800px;
}

ul {
  list-style: none;
}
.line {
  width: 60%;
  margin-bottom: 25px;
}
.title {
  width: 45%;
  margin-bottom: 10px;
}

.title-label {
  margin-right: 30px;
}

.total-contents {
  width: 500px;
  margin: 0 auto;
  text-align: justify;
}
.content {
  width: 45%;
}

.content-label {
  margin-right: 30px;
  height: 200px;
  display: inline;
}

.content-total {
  vertical-align: middle;
  margin-bottom: 30px;
}

.btn {
  margin-right: 5px;
}

.buttons {
  margin-bottom: 40px;
}

.commentbox {
  margin-bottom: 180px;
  margin-top: 40px;
}

.comment-input-box {
  margin-right: 3px;
  width: 350px;
  height: 37px;
}

.detail-main {
  height: 400px;
  /* background-image: url(https://extmovie.com/files/attach/images/135/864/625/039/d45f2adb0da9e2490177d26540c2c83d.gif); */
  margin-bottom: 20px;
  background-size: cover;
  filter: grayscale(100%);
}

.main-title {
  color: rgb(0, 0, 0);
  position: relative;
  padding-top: 180px;
  font-weight: bold;
}

strong {
  color: rgb(38, 95, 202);
}
.loading span {
  display: inline-block;
  margin: 0 -0.075em;
  animation: loading 0.7s infinite alternate;
}
.loading span:nth-child(2) {
  animation-delay: 0.1s;
}
.loading span:nth-child(3) {
  animation-delay: 0.2s;
}
.loading span:nth-child(4) {
  animation-delay: 0.3s;
}
.loading span:nth-child(5) {
  animation-delay: 0.4s;
}
.loading span:nth-child(6) {
  animation-delay: 0.5s;
}
.loading span:nth-child(7) {
  animation-delay: 0.6s;
}
@keyframes loading {
  0% {
    transform: scale(1);
  }
  100% {
    transform: scale(0.8);
  }
}
</style>
