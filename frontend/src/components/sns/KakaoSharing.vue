<template>
  <div>
    <v-btn
      v-if="this.article !== undefined"
      icon
      @click="share"
      class="ma-2"
      fab
      small
      x-light
    >
      <v-icon>mdi-share-variant</v-icon>
    </v-btn>
    <v-btn v-else @click="share" class="ma-2" fab x-small light>
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
      username: '',
    };
  },
  methods: {
    share() {
      // this.articles !== undefined || this.articles !== null || this.articles.length !== 0
      if (this.articles !== undefined && this.articles.length > 0) {
        this.tmpArticle = this.articles[0];

        this.baseUrl = `https://i4b107.p.ssafy.io/main/${
          this.articles[0].userDto.uid
        }?jsonQueryData=${JSON.stringify(this.filteredHashtagSwitches)}`;
      } else if (this.article !== undefined) {
        this.tmpArticle = this.article;
        this.baseUrl = `https://i4b107.p.ssafy.io/articles/${this.tmpArticle.articleNo}`;
      } else {
        alert('공유할 게시물이 없습니다.');
        return;
      }
      Kakao.Link.sendDefault({
        objectType: 'feed',
        content: {
          title: this.tmpArticle.userDto.username + '님의 소중한 지도 기록🗺',
          // title: this.tmpArticle.title,
          // title: '',
          description: this.tmpArticle.contents,
          imageUrl: '',
          // TODO : 배포할때 주소 변경
          // imageUrl: `@/assets/this.tmpArticle.imagePaths`,
          link: {
            // mobileWebUrl: 'https://i4b107.p.ssafy.io/',
            webUrl: this.baseUrl,
            mobileWebUrl: `https://i4b107.p.ssafy.io/articles/${this.tmpArticle.articleNo}`,
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
