<template>
  <div style="color: #ff70bc">
    <v-btn icon color="primary">
      <v-icon size="30" v-if="isFavorited" @click="checkFavorited"
        >mdi-heart</v-icon
      >
      <v-icon size="30" v-if="!isFavorited" @click="checkFavorited"
        >mdi-heart-outline</v-icon
      >
    </v-btn>
  </div>
</template>

<script>
import jwt_decode from 'jwt-decode';
import { doFavorite, isFavorite } from '@/api/user.js';
import { notifyAction } from '@/api/fcm.js';
export default {
  name: 'Favorite',
  props: {
    article: {
      type: Object,
    },
  },
  data() {
    return {
      isFavorited: false,
    };
  },
  methods: {
    setToken: function () {
      const token = localStorage.getItem('jwt');
      const config = {
        headers: {
          jwt: `${token}`,
        },
      };
      return config;
    },
    checkFavorited() {
      const config = this.setToken();
      console.log(this.article.articleNo, '클릭하고 articleNO');
      doFavorite(
        this.article.articleNo,
        config,
        (response) => {
          console.log(response, '좋아요');
          this.isFavorited = !this.isFavorited;
          let body = {
            uid: this.article.userDto.uid,
            articleNo: this.article.articleNo,
            // uid: jwt_decode(localStorage.getItem('jwt')).uid,
            message: 'LIKE',
          };
          notifyAction(
            body,
            (success) => {
              if (success.data.status) {
                console.log('알림 ok');
              } else {
                console.log('알림을 할 수 없습니다.');
              }
            },
            (error) => {
              console.log(error);
              alert('서버에러');
            }
          );
        },
        (error) => {
          console.log(error);
        }
      );
    },
  },
  created() {
    const config = this.setToken();

    isFavorite(
      this.$route.params.articleNo,
      config,
      (response) => {
        // console.log(response.data.status, 'is favorite?');
        this.isFavorited = response.data.status;
      },
      (error) => {
        console.log(error);
      }
    );
  },
};
</script>

<style scoped></style>
