<template>
  <div>
    TimeLine
    <div class="listWrap">
      <table class="table">
        <tr class="tableTitle">
          <th>No.</th>
          <th class="title">제목</th>
          <th>아이디</th>
          <th>날짜</th>
        </tr>
        <tr v-for="(post, idx) in articleList" :key="idx">
          <td>{{ post.id }}</td>
          <td class="txt_left">
            <a href="javascript:;" @click="goToDetail(post)">{{ post.title }}</a>
          </td>
          <td>{{ post.user }}</td>
          <!-- <td>{{ post.updated_at | moment('YYYY-MM-DD') }}</td> -->
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
//npm install vue-moment --save

// import axios from 'axios';

// import Vue from 'vue';
// import vueMoment from 'vue-moment';
// Vue.use(vueMoment);

export default {
  name: 'TimeLine',
  props: {
    articleList: [Array, Object],
  },
  data() {
    return {
      articles: '',
    };
  },
  methods: {
    goToDetail() {
      const articleNo = this.$route.query.articleNo;
      this.$router.push({ name: 'mypage', params: { articleNo } });
    },
    getArticles() {
      // 마이페이지의 타임라인에 게시글 불러올때 axois 쓸지 props받을지 결정할 것.
      // axios
      //   .get('')
      //   .then((res) => {
      //     console.log(res);
      //   })
      //   .catch((err) => {
      //     console.log(err);
      //   });
    },
  },
  created() {
    // this.getArticles()
  },
};
</script>

<style scoped>
.line {
  width: 60%;
  margin-bottom: 25px;
}

.post-btn {
  margin-left: 10px;
}

.completed {
  text-decoration: line-through;
  color: rgb(112, 112, 112);
}

ul {
  list-style: none;
}

colgroup {
  width: 60%;
}

.listWrap {
  margin-bottom: 30px;
}

.title {
  width: 300px;
}

.tableTitle {
  margin-bottom: 30px;
}

.main {
  height: 400px;
  background-image: url(https://media2.giphy.com/media/xUA7b3v67J4zaqi6GI/source.gif);
  margin-bottom: 20px;
  background-size: 1920px;
  filter: grayscale(100%);
}

.main-title {
  color: white;
  position: relative;
  padding-top: 180px;
  font-weight: bold;
}

.button {
  margin-bottom: 150px;
}
</style>
