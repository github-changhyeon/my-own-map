<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="9">
        <v-text-field
          v-model="searchData"
          label="제목, 내용을 검색해주세요"
          outlined
          dense
          background-color="white"
          @keypress.enter="searchFunc"
        ></v-text-field>
      </v-col>
    </v-row>

    <v-row>
      <v-col v-for="(article, i) in listData" :key="i" cols="6">
        <!-- <v-card class="card-size">  -->

        <v-card class="mx-auto" max-width="344">
          <v-img
            @click="goToDetail(article)"
            :src="
              article.imagePaths.length === 0
                ? 'https://cdn.vuetifyjs.com/images/cards/sunshine.jpg'
                : 'https://i4b107.p.ssafy.io/images/uploads/' +
                  article.imagePaths[0]
            "
            width="100%"
            height="150px"
          ></v-img>

          <v-card-title @click="goToDetail(article)"
            ><span
              style="
                width: 300px;
                display: block;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
              "
            >
              {{ article.title }}</span
            >
          </v-card-title>

          <v-card-subtitle style="padding-bottom: 0">
            <v-rating
              center
              v-model="article.evaluation"
              readonly
              background-color="orange lighten-3"
              color="orange"
              dense
              half-increments
              size="20"
            ></v-rating>
          </v-card-subtitle>
          <v-card-actions>
            <h5>작성자</h5>

            <v-spacer></v-spacer>

            <v-btn
              color="orange lighten-2"
              @click="goToMyPage(article.userDto.uid)"
              text
            >
              {{ article.userDto.username }}</v-btn
            >
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>

    <infinite-loading ref="inf" @infinite="infiniteHandler" spinner="waveDots">
      <div
        slot="no-more"
        style="color: rgb(102, 102, 102); font-size: 14px; padding: 10px 0px"
      >
        목록의 끝입니다
      </div>
      <div
        slot="no-results"
        style="color: rgb(102, 102, 102); font-size: 14px; padding: 10px 0px"
      >
        데이터가 없습니다
      </div>
    </infinite-loading>
  </v-container>
</template>

<script>
import constants from '@/lib/constants.js';
import { getAllArticles } from '@/api/article.js';
import InfiniteLoading from 'vue-infinite-loading';

export default {
  name: 'AllNewsFeed',
  components: {
    InfiniteLoading,
  },
  props: [],
  computed: {},
  watch: {},
  created() {
    getAllArticles(
      (response) => {
        if (response.data.status) {
          this.articles = response.data.object;
          this.backupArticles = this.articles;
        }
      },
      (error) => {
        console.log(error);
        alert('모든 유저의 모든 게시글 받아오기 실패');
      }
    );
  },
  methods: {
    searchFunc() {
      this.startIdx = 0;
      this.listData = [];
      let tempList = [];
      for (let i = 0; i < this.backupArticles.length; ++i) {
        if (
          this.backupArticles[i].title.includes(this.searchData) ||
          this.backupArticles[i].contents.includes(this.searchData)
        ) {
          tempList.push(this.backupArticles[i]);
        }
      }
      // alert(tempList.length);
      this.articles = tempList;
      // this.listData.push(this.articles[0]);
      // this.listData.push(this.articles[1]);
      // this.listData.push(this.articles[2]);
      // this.listData.push(this.articles[3]);
      this.$refs.inf.stateChanger.reset();
      this.searchData = '';
    },
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
    infiniteHandler($state) {
      const EACH_LEN = 6;

      setTimeout(() => {
        let cnt = 0;
        // let isEnd = false;
        let list = [];
        while (cnt++ < EACH_LEN && this.startIdx < this.articles.length) {
          list.push(this.articles[this.startIdx++]);
        }
        if (list.length > 0) {
          this.listData = this.listData.concat(list);
          $state.loaded();
          if (list.length / EACH_LEN < 1) {
            $state.complete();
          }
        } else {
          $state.complete();
        }
      }, 250);
    },
  },
  data() {
    return {
      searchData: '',
      backupArticles: [],
      startIdx: 0,
      listData: [],
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

<style scoped>
.card-size {
  max-width: 344px;
  max-height: 270px;
  margin: 0 auto;
}

.card-title {
  display: block;
  text-overflow: ellipsis;
}
</style>
