<!-- 순서 : 등록된 사진 캐러샐, 평점, 해쉬태그, 장소이름(제목), 설명, 주소, 날짜, 댓글 폼  -->
<!-- 게시글 수정, 삭제 (현재 페이지에서 바로가능하게) -->
<!-- 현재는 라우터로 구현. 메인에서 누를 시 articleNo로 라우터푸시  -->
<!-- comment 데이터를 axios 요청으로 받아올 거라 게시글 상세정보도 axios로 받아옴  -->
<!-- 휴대폰 화면으로 잘 뜨는지 확인 -->
<template>
  <v-card>
    <v-app>
      <div class="detail-main" style="vertical-align: middle">
        <v-btn
          icon
          size="30"
          style="position: fixed; display: flex; top: 25px; z-index: 2"
          @click="goBack"
        >
          <v-icon>mdi-arrow-left</v-icon>
        </v-btn>
        <div
          style="
            position: fixed;
            display: flex;
            top: 25px;
            left: 50px;
            z-index: 2;
            vertical-align: middle;
          "
        >
          <!-- <v-avatar>
            {{ article.userDto.profileImagePath }}
          </v-avatar> -->
          <span class="hover" @click="goToMyPage(article.userDto.uid)">
            {{ article.userDto.username }}
          </span>
        </div>

        <Favorite
          :article="article"
          style="
            position: fixed;
            display: flex;
            right: 60px;
            top: 25px;
            z-index: 2;
          "
        />
        <KakaoSharing
          :article="article"
          size="30"
          style="
            position: fixed;
            display: flex;
            right: 10px;
            top: 13px;
            z-index: 2;
          "
        />
      </div>

      <div class="total-contents">
        <div>
          <label for="title"></label>
          <span class="article-title">
            <b>{{ article.title }}</b>
          </span>
        </div>
        <div style="text-align: center; max-width: 500px; width: 100%">
          <v-carousel v-if="hasImages">
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
          <div>
            <v-rating
              v-model="this.article.evaluation"
              background-color="grey lighten-1"
              color="primary"
              half-increments
              length="5"
              readonly
              large
            >
            </v-rating>
          </div>
        </div>
        <div style="margin-top: 10px">
          <!-- hashtags -->
          <v-icon>mdi-pound</v-icon>
          <span
            class="hashbox"
            v-for="(hashtag, idx) in article.hashtags"
            :key="idx"
          >
            {{ hashtag.hashtagName }}
          </span>
        </div>
        <div style="margin-top: 10px">
          <v-icon>mdi-calendar</v-icon>
          <!-- {{ article.regiTime.split('T')[0] }} -->
          {{ article.visitDate }}
        </div>
        <div style="margin-top: 10px">
          <v-icon>mdi-map-marker</v-icon>
          <span class="map">{{ article.address }}</span>
        </div>
        <br />
        <div class="content-total">
          <span class="article-content"
            ><h6>{{ article.contents }}</h6></span
          >
        </div>
      </div>

      <div class="buttons" v-if="isOwnArticle">
        <!-- <div class="buttons"  > -->
        <v-btn
          fab
          small
          @click="goToUpdateArticle"
          variant="outline-primary"
          class="updatebutton"
        >
          <v-icon>mdi-pencil</v-icon>
        </v-btn>
        <v-btn
          fab
          small
          @click="checkDelete"
          variant="danger"
          class="deletebutton"
        >
          <v-icon>mdi-delete</v-icon>
        </v-btn>
        <v-btn fab small @click="findRoute" style="margin-right: 10px">
          <v-icon>mdi-map</v-icon>
        </v-btn>
      </div>
      <div class="buttons" v-if="!isOwnArticle">
        <v-btn fab small @click="findRoute" style="margin-right: 10px">
          <v-icon>mdi-map</v-icon>
        </v-btn>
      </div>
      <CommentList
        style="margin-bottom: 50px"
        :articleNo="$route.params.articleNo"
        :propsUid="article.userDto.uid"
      />
      <Navigation />
    </v-app>
  </v-card>
</template>

<script>
// import axios from 'axios';
import { getArticle } from '@/api/article.js';
import { deleteArticle } from '@/api/article.js';

import Vue from 'vue';
import constants from '@/lib/constants';
import jwt_decode from 'jwt-decode';
import Navigation from '@/components/Navigation.vue';
import KakaoSharing from '@/components/sns/KakaoSharing.vue';
import Favorite from '@/components/user/Favorite.vue';
import CommentList from '@/views/article/CommentList.vue';
import vueMoment from 'vue-moment';

Vue.use(vueMoment);

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
      hasImages: false,
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
      // window.open(`https://map.kakao.com/link/to/${this.$route.params.article.address},${this.$route.params.article.positionLat},${this.$route.params.article.positionLng}`);
      window.open(
        `https://map.kakao.com/link/to/${this.article.address},${this.article.positionLat},${this.article.positionLng}`
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

    goToMyPage(uid) {
      this.$router.push({
        name: constants.URL_TYPE.USER.MYPAGE,
        params: { uid: uid },
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
  },

  created() {
    const token = localStorage.getItem('jwt');
    let uid = '';
    if (token !== null && token !== undefined) {
      uid = jwt_decode(token).uid;
    }

    getArticle(
      this.$route.params.articleNo,
      (response) => {
        this.article = response.data.object;
        if (
          token !== null &&
          token !== undefined &&
          this.article.userDto.uid === uid
        ) {
          this.isOwnArticle = true;
        }
        for (let i = 0; i < this.article.imagePaths.length; ++i) {
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
        if (this.article.imagePaths[0] !== 'DefaultArticleImage.png') {
          this.hasImages = true;
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

.article-title {
  font-size: 20px;
  font-weight: bold;
}

.hashbox {
  border: 1px #ff70bc solid;
  background-color: #ff70bc;
  color: black;
  border-radius: 10px;
  margin-right: 3px;
}

.map {
  font-weight: bold;
}

.article-content {
  font-size: 20px;
}

.total-contents {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
  /* text-align: center; */
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

.image {
  width: 100%;
  max-width: 400px;
  /* margin: 5px; */
  /* padding: auto; */
  text-align: center;
  /* align-content: center; */
}

.btn {
  margin-right: 5px;
}

.buttons {
  margin-bottom: 40px;
  text-align: right;
}

.deletebutton {
  font-size: 15px;
  margin-right: 10px;
  text-decoration: none;
}

.updatebutton {
  font-size: 15px;
  margin-right: 10px;
  text-decoration: none;
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
  height: 90px;
  align-content: center;
  background-size: cover;
  filter: grayscale(0%);
}

.main-title {
  color: rgb(0, 0, 0);
  position: relative;
  padding-top: 180px;
  font-weight: bold;
}
.hover {
  cursor: pointer;
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
