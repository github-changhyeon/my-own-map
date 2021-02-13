<template>
  <div>
    <v-btn @click="share" class="ma-2" fab small light>
      <v-icon>mdi-share-variant</v-icon>
    </v-btn>
  </div>
</template>

<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<script>
export default {
  name: 'KakaoSharing',
  props: {
    filteredHashtagSwitches: {
      type: [Object, Array],
    },
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
      tmpArticle: Object,
      baseUrl: '',
    };
  },
  methods: {
    share() {
      console.log(this.articles, '메서드의 articles');
      // this.articles !== undefined || this.articles !== null || this.articles.length !== 0
      if (this.articles !== undefined && this.articles.length > 0) {
        this.tmpArticle = this.articles[0];

        this.baseUrl = `https://i4b107.p.ssafy.io:80/main/${
          this.articles[0].userDto.uid
        }?jsonQueryData=${JSON.stringify(this.filteredHashtagSwitches)}`;
      } else if (this.article !== undefined) {
        console.log(this.article, 'this.article?');
        console.log(this.$route.params, 'route 파람스');
        this.tmpArticle = this.article;
        console.log(this.tmpArticle);
        this.baseUrl = `https://i4b107.p.ssafy.io:80/articles/${this.tmpArticle.articleNo}`;
      } else {
        alert('공유할 게시물이 없습니다.');
        return;
      }
      console.log(this.baseUrl);
      Kakao.Link.sendDefault({
        objectType: 'feed',
        content: {
          // title: this.tmpArticle.title,
          title: '',
          description: this.tmpArticle.contents,
          imageUrl: '',
          // imageUrl: this.tmpArticle.imagePaths,
          link: {
            // mobileWebUrl: 'https://i4b107.p.ssafy.io/',
            webUrl: this.baseUrl,
            mobileWebUrl: `https://i4b107.p.ssafy.io:80/articles/${this.tmpArticle.articleNo}`,
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
              mobileWebUrl: this.baseUrl,
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
