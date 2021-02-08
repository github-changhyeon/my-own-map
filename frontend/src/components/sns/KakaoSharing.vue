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
      type: Object,
    },
  },
  data() {
    return {
      // article: {
      //   address: '',
      //   articleNo: 0,
      //   contents: '',
      //   evaluation: '',
      //   positionLat: '',
      //   positionLng: '',
      //   regiTime: '',
      //   updateTime: '',
      //   title: '',
      //   userDto: {},
      //   imagePaths: null,
      // },
    };
  },
  methods: {
    share() {
      Kakao.Link.sendDefault({
        objectType: 'feed',
        content: {
          title: this.article.title,
          description: this.article.contents,
          imageUrl: '',
          // imageUrl: this.article.imagePaths,
          link: {
            // mobileWebUrl: 'https://i4b107.p.ssafy.io/',
            mobileWebUrl: `http://localhost:8081/articles/${this.article.articleNo}`,
          },
        },
        social: {
          likeCount: 12,
          commentCount: 34,
          sharedCount: 56,
        },
        buttons: [
          {
            title: '웹으로 이동',
            link: {
              mobileWebUrl: `http://localhost:8081/articles/${this.article.articleNo}`,
              webUrl: `http://localhost:8081/articles/${this.article.articleNo}`,
            },
          },
        ],
      });
    },
  },
  created() {
    // this.article = article;
    console.log('공유하기의 콘솔');
    // console.log(this.article);
    console.log(this.article);
  },
};
</script>

<style></style>
