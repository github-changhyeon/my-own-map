<template>
  <v-container>
    <v-row>
      <v-col v-for="(article, i) in articles" :key="i" cols="4">
        <v-card class="mx-auto" max-width="344">
          <v-img
            @click="goToDetail(article)"
            :src="article.imagePaths.length === 0 ? 'https://cdn.vuetifyjs.com/images/cards/sunshine.jpg' : 'https://i4b107.p.ssafy.io/images/uploads/' + article.imagePaths[0]"
            width="100%"
            height="150px"
          ></v-img>

          <v-card-title @click="goToDetail(article)"> {{ article.title }} </v-card-title>

          <v-card-subtitle style="padding-bottom: 0">
            <v-rating center v-model="article.evaluation" readonly background-color="orange lighten-3" color="orange" dense half-increments size="20"></v-rating>
          </v-card-subtitle>
          <v-card-actions>
            <h5>작성자</h5>

            <v-spacer></v-spacer>

            <v-btn color="orange lighten-2" @click="goToMyPage(article.userDto.uid)" text> {{ article.userDto.username }}</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import constants from '@/lib/constants.js';
import { getPrivateArticles } from '@/api/article.js';

export default {
  name: 'AllSecretNewsFeed',
  components: {},
  props: [],
  computed: {},
  watch: {},
  created() {
    getPrivateArticles(
      uid,
      (response) => {
        if (response.data.status) {
          this.articles = response.data.object;
        } else {
          console.log('게시글을 받아올 수 없습니다.');
        }
      },
      (error) => {
        console.log(error);
        alert('게시글 받아오기 실패');
      }
    );
  },
  methods: {
    goToDetail(article) {
      this.$router.push({ name: constants.URL_TYPE.ARTICLE.ARTICLEDETAIL, params: { articleNo: article.articleNo, article: article } });
    },
    goToMyPage(uid) {
      this.$router.push({ name: constants.URL_TYPE.USER.MYPAGE, params: { uid: uid } });
    },
  },
  data() {
    return {
      articles: [
        // { title: 's', articleNo: 1, contents: 's', evaluation: 3.5, imagePaths: [], userDto: {} },
        // { title: 's', articleNo: 1, contents: 's', evaluation: 3.5, imagePaths: [], userDto: {} },
        // { title: 's', articleNo: 1, contents: 's', evaluation: 3.5, imagePaths: [], userDto: {} },
        // { title: 's', articleNo: 1, contents: 's', evaluation: 3.5, imagePaths: [], userDto: {} },
        // { title: 's', articleNo: 1, contents: 's', evaluation: 3.5, imagePaths: [], userDto: {} },
        // { title: 's', articleNo: 1, contents: 's', evaluation: 3.5, imagePaths: [], userDto: {} },
        // { title: 's', articleNo: 1, contents: 's', evaluation: 3.5, imagePaths: [], userDto: {} },
        // { title: 's', articleNo: 1, contents: 's', evaluation: 3.5, imagePaths: [], userDto: {} },
      ],
    };
  },
};
</script>
