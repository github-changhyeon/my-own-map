<template>
  <div style="margin-bottom:50px;">
    <v-container>
      <v-row v-if="articles.length > 0">
        <v-col v-for="(article, i) in articles" :key="i" cols="6">
          <v-card class="mx-auto" max-width="344">
            <v-img
              @click="goToDetail(article)"
              :src="article.imagePaths.length === 0 ? 'https://cdn.vuetifyjs.com/images/cards/sunshine.jpg' : 'https://i4b107.p.ssafy.io/images/uploads/' + article.imagePaths[0]"
              width="100%"
              height="150px"
            ></v-img>

            <v-card-title @click="goToDetail(article)">
              <span style="width:300px; display:block; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">{{ article.title }}</span>
            </v-card-title>

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
    <v-btn @click="goToMain" style="position: fixed; bottom: 100px; z-index: 2" depressed><v-icon>mdi-map-search-outline</v-icon></v-btn>

    <Navigation />
  </div>
</template>
<script>
// import FollowNewsFeed from '@/components/sns/FollowNewsFeed';
import Navigation from '@/components/Navigation.vue';
import constants from '@/lib/constants.js';
import jwt_decode from 'jwt-decode';

export default {
  name: 'ListView',
  created() {
    this.articles = this.$route.params.filteredData;
    console.log(this.articles);
  },
  components: {
    // FollowNewsFeed,
    Navigation,
  },
  data() {
    return {
      articles: [],
    };
  },
  methods: {
    goToDetail(article) {
      this.$router.push({
        name: constants.URL_TYPE.ARTICLE.ARTICLEDETAIL,
        params: { articleNo: article.articleNo, article: article },
      });
    },
    goToMyPage(uid) {
      this.$router.push({
        name: constants.URL_TYPE.USER.MYPAGE,
        params: { uid: uid },
      });
    },
    goToMain() {
      const token = localStorage.getItem('jwt');
      let uid = jwt_decode(token).uid;
      this.$router.replace({ name: constants.URL_TYPE.HOME.MAIN, params: { uid: uid } });
    },
  },
};
</script>

<style></style>
