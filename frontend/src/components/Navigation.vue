<template>
  <v-bottom-navigation
    :value="value"
    color="primary"
    style="textDecoration:none; position: fixed;bottom: 0px; z-index: 2; display: flex; justify-content: space-around; background-color:white;"
    class="nav"
  >
    <!-- 버튼을 router link로 해보자! -->

    <v-btn
      :to="`/main/${this.uid}`"
      replace
      style="background-color:white; margin-top:8px;"
    >
      <v-icon>mdi-home</v-icon>
    </v-btn>

    <v-btn
      to="/newsfeed"
      replace
      style="background-color:white; margin-top:8px;"
    >
      <v-icon>mdi-newspaper-variant</v-icon>
    </v-btn>

    <v-btn
      to="/articles/create"
      replace
      style="background-color:white; margin-top:8px;"
    >
      <v-icon>mdi-plus</v-icon>
    </v-btn>

    <v-btn @click="goToHistory" style="background-color:white; margin-top:8px;">
      <v-badge :content="messages" :value="messages" color="red" overlap>
        <v-icon>
          mdi-bell
        </v-icon>
      </v-badge>
    </v-btn>

    <v-btn
      :to="`/users/${this.uid}`"
      replace
      style="background-color:white; margin-top:8px;"
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
    // uid:jwt_decode(localStorage.getItem('jwt')),
  }),
  // watch: {

  // },
  methods: {
    goToHistory() {
      if (this.uid > 0) {
        setZero(
          (success) => {
            if (success.data.status) {
              console.log('zero 만들기 성공');
            } else {
              console.log('zero 만들기 실패');
            }
          },
          (error) => {
            console.log(error);
            alert('서버 에러');
          }
        );
      }

      this.$router.push({ name: constants.URL_TYPE.USER.HISTORY });
    },
  },
  created() {
    const token = localStorage.getItem('jwt');
    if (token !== null && token !== undefined) {
      this.uid = jwt_decode(token).uid;
    }
    console.log(token, 'uid는', this.uid);
    // this.value = 2;

    console.log(window.location.pathname, '패쓰네임쓰');
    let nowLocation = window.location.pathname.split('/');
    console.log(nowLocation[1], 'helllo');
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
          } else {
            console.log('유저 정보 받아오기 실패');
          }
        },
        (error) => {
          console.log(error);
          alert('서버 에러');
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
