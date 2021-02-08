<template>
  <div>
    <v-icon @click="share">mdi-share-variant</v-icon>
  </div>
</template>

<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<script>
export default {
  name: 'KakaoSharing',
  props: {
    article: {
      type: [Object, Array],
    },
    articles: {
      type: Array,
    },
    uid: {
      type: Number,
    },
  },
  data() {
    return {
      tmpArticle: null,
      baseUrl: '',
    };
  },
  methods: {
    share() {
      console.log(this.articles, '메서드의 articles');
      // this.articles !== undefined || this.articles !== null || this.articles.length !== 0
      if (this.articles !== undefined && this.articles.length > 0) {
        this.tmpArticle = this.articles[0];
        this.baseUrl = `http://localhost:8081/main/${this.articles[0].userDto.uid}`;
      } else if (this.article !== undefined) {
        console.log(this.article, 'this.article?');
        this.tmpArticle = this.article;
        this.baseUrl = `http://localhost:8081/articles/${this.tmpArticle.articleNo}`;
      } else {
        alert('공유할 게시물이 없습니다.');
        return;
      }
      Kakao.Link.sendDefault({
        objectType: 'feed',
        content: {
          title: this.tmpArticle.title,
          description: this.tmpArticle.contents,
          imageUrl: '',
          // imageUrl: this.tmpArticle.imagePaths,
          link: {
            // mobileWebUrl: 'https://i4b107.p.ssafy.io/',
            mobileWebUrl: `http://localhost:8081/articles/${this.tmpArticle.articleNo}`,
          },
        },
        // social: {
        //   likeCount: 12,
        //   commentCount: 34,
        //   sharedCount: 56,
        // },
        buttons: [
          {
            title: '웹으로 이동',
            link: {
              mobileWebUrl: `http://localhost:8081/articles/${this.tmpArticle.articleNo}`,
              // webUrl: `http://localhost:8081/articles/${this.tmpArticle.articleNo}`,
              webUrl: this.baseUrl,
            },
          },
        ],
      });
    },
  },
  created() {},
};
</script>

<style></style>
