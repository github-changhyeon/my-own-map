<template>
  <v-bottom-navigation
    :value="value"
    color="primary"
    style="
      textdecoration: none;
      position: fixed;
      bottom: 0px;
      z-index: 2;
      display: flex;
      justify-content: space-around;
      background-color: white;
    "
    class="nav"
  >
    <v-btn
      @click="goToMain"
      replace
      style="background-color: white; margin-top: 8px"
    >
      <v-icon>mdi-home</v-icon>
    </v-btn>

    <v-btn
      @click="goToNewsFeed"
      replace
      style="background-color: white; margin-top: 8px"
    >
      <v-icon>mdi-newspaper-variant</v-icon>
    </v-btn>

    <v-btn
      @click="goToCreateArticle"
      replace
      style="background-color: white; margin-top: 8px"
    >
      <v-icon>mdi-plus</v-icon>
    </v-btn>

    <v-btn
      @click="goToHistory"
      style="background-color: white; margin-top: 8px"
    >
      <v-badge :content="messages" :value="messages" color="red" overlap>
        <v-icon> mdi-bell </v-icon>
      </v-badge>
    </v-btn>

    <v-btn
      @click="goToUserPage"
      replace
      style="background-color: white; margin-top: 8px"
    >
      <v-icon>mdi-account</v-icon>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
import jwt_decode from 'jwt-decode';
import { getUserInfo } from '@/api/user.js';
import { setZero } from '@/api/fcm.js';
import constants from '@/lib/constants.js';

export default {
  data: () => ({
    value: 0,
    uid: 0,
    isSelected: false,
    messages: 0,
  }),
  watch: {},
  methods: {
    goToMain() {
      if (this.uid > 0) {
        this.$router.push({
          name: constants.URL_TYPE.HOME.MAIN,
          params: { uid: this.uid },
        });
      } else {
        this.$router.push({
          name: constants.URL_TYPE.USER.JOIN,
        });
      }
    },
    goToNewsFeed() {
      if (this.uid > 0) {
        this.$router.push({
          name: constants.URL_TYPE.SNS.NEWSFEED,
        });
      } else {
        this.$router.push({
          name: constants.URL_TYPE.USER.JOIN,
        });
      }
    },
    goToCreateArticle() {
      if (this.uid > 0) {
        this.$router.push({
          name: constants.URL_TYPE.ARTICLE.CREATEARTICLE,
        });
      } else {
        this.$router.push({
          name: constants.URL_TYPE.USER.JOIN,
        });
      }
    },
    goToHistory() {
      if (this.uid > 0) {
        setZero(
          (success) => {
            if (success.data.status) {
              this.$router.push({ name: constants.URL_TYPE.USER.HISTORY });
            }
          },
          (error) => {
            console.log(error);
          }
        );
      } else {
        this.$router.push({
          name: constants.URL_TYPE.USER.JOIN,
        });
      }
    },
    goToUserPage() {
      if (this.uid > 0) {
        this.$router.push({
          name: constants.URL_TYPE.USER.MYPAGE,
          params: { uid: this.uid },
        });
      } else {
        this.$router.push({
          name: constants.URL_TYPE.USER.JOIN,
        });
      }
    },
  },
  created() {
    const token = localStorage.getItem('jwt');
    if (token !== null && token !== undefined) {
      this.uid = jwt_decode(token).uid;
    }

    let nowLocation = window.location.pathname.split('/');
    switch (nowLocation[1]) {
      case 'main':
        this.value = 0;
        break;
      case 'newsfeed':
        this.value = 1;
        break;
      case 'articles':
        this.value = 2;
        break;
      case 'history':
        this.value = 3;
        break;
      case 'users':
        this.value = 4;
        break;
      default:
        break;
    }

    if (this.uid > 0) {
      getUserInfo(
        this.uid,
        (success) => {
          if (success.data.status) {
            this.messages = success.data.object.notificationCheck;
          }
        },
        (error) => {
          console.log(error);
        }
      );
    }
  },
};
</script>

<style>
.nav {
  width: 100vw;
  display: flex;
  justify-content: space-around;
}

.button {
  background-color: white;
}
</style>
