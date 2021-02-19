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
      doFavorite(
        this.article.articleNo,
        config,
        () => {
          this.isFavorited = !this.isFavorited;
          let body = {
            uid: this.article.userDto.uid,
            articleNo: this.article.articleNo,
            message: 'LIKE',
          };
          notifyAction(
            body,
            (success) => {
              if (!success.data.status) {
                console.log('알림을 할 수 없습니다.');
              }
            },
            (error) => {
              console.log(error);
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
