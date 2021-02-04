<template>
  <v-app id="inspire">
    <v-container>
      <v-row no-gutters>
        <v-col @click="goToMypage(idx)" v-for="(article, idx) in articles" :key="idx" md="6" offset-md="3">
          <v-card height="200" class="mt-10" outlined tile>
            <div>
              <!-- <router-link :to="`/users/${article.uid}`">작성자: {{ article.uid }}</router-link> -->
              <div>작성자: {{ article.uid }}</div>
            </div>
            <div>rating: {{ article.rating }}</div>
            <div>
              {{ article.img }}
            </div>
            <div>
              해쉬태그
            </div>
            <div>
              작성일자
            </div>
            <div>
              주소
            </div>
            <div>
              title
            </div>
            <div>
              내용
            </div>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-app>
</template>
<script>
import constants from '@/lib/constants.js';

export default {
  name: 'NewsFeed',
  data() {
    return {
      articles: [
        {
          uid: 1,
          rating: 5,
          img: 'https://cdn.vuetifyjs.com/images/cards/cooking.png',
        },
        {
          uid: 2,
        },
        {
          uid: 3,
        },
      ],
    };
  },
  methods: {
    goToMypage(idx) {
      console.log(this.$route.params);
      // console.log(event);
      console.log(this.articles[idx]);
      this.$router.push({ name: constants.URL_TYPE.USER.MYPAGE, params: { uid: this.articles[idx].uid } });
    },
  },
};
</script>

<style></style>
