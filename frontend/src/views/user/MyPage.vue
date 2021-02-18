<template>
  <div>
    <div style="text-align: right">
      <v-btn v-if="isSameUser" @click="logout">로그아웃</v-btn>
    </div>
    <div>
      <UserPicture
        style="margin-top: 30px"
        :isSameUser="isSameUser"
        :propsUserDto="userDto"
      />
    </div>
    <div class="center">
      <!-- <span>{{ userDto }}</span> -->
      <span style="font-size: 30px; bold;">{{ userDto.username }}</span>
    </div>
    <div class="center">
      <span>{{ userDto.stateMsg }}</span>
    </div>
    <div>
      <UserInfo
        style="margin-top: 50px"
        :isSameUser="isSameUser"
        :followerList="followerList"
        :followingList="followingList"
      />
    </div>
    <!-- <div>
      <TimeLine />
    </div> -->
    <!-- <v-card>
      <v-tabs centered style="z-index: 2">
        <v-tab @click="isOpen = 1" style="width: 50vw">내 게시글 보기</v-tab>
        <v-tab @click="isOpen = 2" style="width: 5vw"><v-icon>mdi-heart</v-icon></v-tab>
        <v-tab @click="isOpen = 3" style="width: 5vw"><v-icon>mdi-lock</v-icon></v-tab>
        <v-tab style="width: 5vw"><v-icon>mdi-cog</v-icon></v-tab>
      </v-tabs>
    </v-card> -->
    <!-- <PublicNewsFeed v-if="isOpen === 1" :propsUid="uid" />
    <FavoriteNewsFeed v-if="isOpen === 2" :propsUid="uid" />
    <PrivateNewsFeed v-if="isOpen === 3" :propsUid="uid" /> -->
    <router-link to="/changeinfo" class="changeInfobutton"
      ><v-icon v-if="isSameUser">mdi-cog</v-icon></router-link
    >
    <div class="bubbles-container"></div>
    <!-- <ChangeInfo :propsUid="uid" /> -->
    <Navigation />
  </div>
</template>

<script>
import jwt_decode from 'jwt-decode';
//npm install vue-moment --save
// import TimeLine from '@/components/user/TimeLine';
import UserInfo from '@/components/user/UserInfo';
import constants from '@/lib/constants.js';
import Navigation from '@/components/Navigation.vue';
// import ChangeInfo from '@/components/user/ChangeInfo.vue';
import UserPicture from '@/components/user/UserPicture.vue';

// //NewsFeed
// import PublicNewsFeed from '@/components/sns/PublicNewsFeed.vue';
// import PrivateNewsFeed from '@/components/sns/PrivateNewsFeed.vue';
// import FavoriteNewsFeed from '@/components/sns/FavoriteNewsFeed.vue';

import { getUserInfo } from '@/api/user.js';
// import { findFollower, findFollowing } from '@/api/user.js';
import { deleteFcmToken } from '@/api/fcm.js';
import {
  deleteToken,
  // receiveMessage,
  // requestPermission,
  // getToken,
} from '@/api/notification.js';
// import axios from 'axios';

// import Vue from 'vue';
// import vueMoment from 'vue-moment';

// Vue.use(vueMoment);

export default {
  name: 'MyPage',
  components: {
    UserInfo,
    // TimeLine,
    Navigation,
    // PublicNewsFeed,
    // PrivateNewsFeed,
    // FavoriteNewsFeed,
    // ChangeInfo,
    UserPicture,
  },
  data() {
    return {
      myImg: '',
      userDto: {},
      tokenData: '',
      isSameUser: true,
      isOpen: 0,
      uid: '',
      followingList: [],
      followerList: [],
    };
  },
  methods: {
    logout() {
      deleteToken(() => {
        deleteFcmToken(
          (success) => {
            if (success.data.status) {
              localStorage.removeItem('jwt');
            }
          },
          (error) => {
            console.log(error);
          }
        );
      });
      // location.reload();
      this.$router.replace({ name: constants.URL_TYPE.USER.LOGIN });
    },
  },
  watch: {
    '$route.params.uid': function () {
      const token = localStorage.getItem('jwt');
      this.tokenData = jwt_decode(token);
      this.uid = this.$route.params.uid;
      if (Number(this.$route.params.uid) === Number(this.tokenData.uid)) {
        this.isSameUser = true;
        // 팔로우버튼 자체를 on/off -> 팔로우버튼이 on -> isfollow -> +, -
        // 토큰 디코드해서 찍힌 uid or email로 article controller에 게시글 요청. 받아서 Userinfo components에 props,emit
      } else {
        this.isSameUser = false;
        this.userDto = this.$route.params;
      }
    },
  },
  created() {
    // const uid = this.$route.params.uid;
    const token = localStorage.getItem('jwt');
    this.tokenData = jwt_decode(token);
    this.uid = this.$route.params.uid;
    // 로그인된상태니까 -> 본인이 본인 페이지 올때는 토큰으로 내 정보를 찾아서 채워야되고
    // (params가 비어있으면) -> jwt로 디코드해서 email axios요청
    // // 하단 네브바로 mypage로 안오고 다른 사람의 페이지를 볼때는 게시글이나 이런걸 타고들어오니까
    // // params가 있을거니까 여기에 userDto이런걸로 axios요청을 보내서 채운다.
    if (Number(this.$route.params.uid) === Number(this.tokenData.uid)) {
      this.isSameUser = true;
    } else {
      this.isSameUser = false;
      this.userDto = this.$route.params;
    }
    getUserInfo(
      this.uid,
      (response) => {
        if (response.data.status) {
          this.userDto = response.data.object;
          this.storePassword = this.userDto.password;
        }
      },
      (error) => {
        console.log(error);
      }
    );
  },
};
</script>

<style scoped>
.changeInfobutton {
  float: right;
  margin-bottom: 70px;
  margin-right: 20px;
  text-decoration: none;
}

.center {
  display: flex;
  justify-content: center;
}
@import url('https://fonts.googleapis.com/css?family=Exo+2:900');
h1 {
  font-family: 'Exo 2', sans-serif;
  font-size: 8.5vw;
  color: white;
  padding: 5rem 0;
  text-shadow: 0px 4px 48px rgba(255, 255, 255, 0.2);
}
.container {
  position: relative;
  display: flex;
  align-content: center;
  justify-content: center;
  background-image: linear-gradient(to bottom, #00c9ff 0%, #92fe9d 100%),
    url(https://images.unsplash.com/photo-1502726299822-6f583f972e02);
  background-blend-mode: multiply;
  background-size: cover;
  overflow: hidden;
}
.bubbles-container {
  position: absolute;
  top: 0;
  left: 50%;
  width: 100%;
  max-width: 15rem;
  transform: translateX(-50%);
  opacity: 0.75;
  overflow: visible;
}
.bubbles {
  width: 100%;
  height: auto;
}
.bubbles circle {
  stroke: white;
  fill: none;
}
.bubbles > g > g:nth-of-type(3n) circle {
  stroke: #87f5fb;
}
.bubbles > g > g:nth-of-type(4n) circle {
  stroke: #8be8cb;
}
.bubbles-large {
  overflow: visible;
}
.bubbles-large > g {
  transform: translateY(2048px);
  opacity: 0;
  will-change: transform, opacity;
}
.bubbles-large g:nth-of-type(1) {
  animation: up 6.5s infinite;
}
.bubbles-large g:nth-of-type(1) g {
  transform: translateX(350px);
}
.bubbles-large g:nth-of-type(1) circle {
  animation: wobble 3s infinite ease-in-out;
}
.bubbles-large g:nth-of-type(2) {
  animation: up 5.25s 250ms infinite;
}
.bubbles-large g:nth-of-type(2) g {
  transform: translateX(450px);
}
.bubbles-large g:nth-of-type(2) circle {
  animation: wobble 3s infinite ease-in-out;
}
.bubbles-large g:nth-of-type(3) {
  animation: up 6s 750ms infinite;
}
.bubbles-large g:nth-of-type(3) g {
  transform: translateX(700px);
}
.bubbles-large g:nth-of-type(3) circle {
  animation: wobble 3s infinite ease-in-out;
}
.bubbles-large g:nth-of-type(4) {
  animation: up 5.5s 1.5s infinite;
}
.bubbles-large g:nth-of-type(4) g {
  transform: translateX(500px);
}
.bubbles-large g:nth-of-type(4) circle {
  animation: wobble 3s infinite ease-in-out;
}
.bubbles-large g:nth-of-type(5) {
  animation: up 6.5s 4s infinite;
}
.bubbles-large g:nth-of-type(5) g {
  transform: translateX(675px);
}
.bubbles-large g:nth-of-type(5) circle {
  animation: wobble 3s infinite ease-in-out;
}
.bubbles-small {
  overflow: visible;
}
.bubbles-small > g {
  transform: translateY(2048px);
  opacity: 0;
  will-change: transform, opacity;
}
.bubbles-small g circle {
  transform: scale(0);
}
.bubbles-small g:nth-of-type(1) {
  animation: up 5.25s infinite;
}
.bubbles-small g:nth-of-type(1) g {
  transform: translateX(350px);
}
.bubbles-small g:nth-of-type(1) circle {
  animation: wobble 3s infinite ease-in-out;
}
.bubbles-small g:nth-of-type(2) {
  animation: up 5.75s infinite;
}
.bubbles-small g:nth-of-type(2) g {
  transform: translateX(750px);
}
.bubbles-small g:nth-of-type(2) circle {
  animation: wobble 3s infinite ease-in-out;
}
.bubbles-small g:nth-of-type(3) {
  animation: up 5.25s 250ms infinite;
}
.bubbles-small g:nth-of-type(3) g {
  transform: translateX(350px);
}
.bubbles-small g:nth-of-type(3) circle {
  animation: wobble 3s 250ms infinite ease-in-out;
}
.bubbles-small g:nth-of-type(4) {
  animation: up 5.75s 325ms infinite;
}
.bubbles-small g:nth-of-type(4) g {
  transform: translateX(180px);
}
.bubbles-small g:nth-of-type(4) circle {
  animation: wobble 3s 325ms infinite ease-in-out;
}
.bubbles-small g:nth-of-type(5) {
  animation: up 6s 125ms infinite;
}
.bubbles-small g:nth-of-type(5) g {
  transform: translateX(350px);
}
.bubbles-small g:nth-of-type(5) circle {
  animation: wobble 3s 250ms infinite ease-in-out;
}
.bubbles-small g:nth-of-type(6) {
  animation: up 5.13s 250ms infinite;
}
.bubbles-small g:nth-of-type(6) g {
  transform: translateX(650px);
}
.bubbles-small g:nth-of-type(6) circle {
  animation: wobble 3s 125ms infinite ease-in-out;
}
.bubbles-small g:nth-of-type(7) {
  animation: up 6.25s 350ms infinite;
}
.bubbles-small g:nth-of-type(7) g {
  transform: translateX(480px);
}
.bubbles-small g:nth-of-type(7) circle {
  animation: wobble 3s 325ms infinite ease-in-out;
}
.bubbles-small g:nth-of-type(8) {
  animation: up 7s 200ms infinite;
}
.bubbles-small g:nth-of-type(8) g {
  transform: translateX(330px);
}
.bubbles-small g:nth-of-type(8) circle {
  animation: wobble 3s 325ms infinite ease-in-out;
}
.bubbles-small g:nth-of-type(9) {
  animation: up 6.25s 233ms infinite;
}
.bubbles-small g:nth-of-type(9) g {
  transform: translateX(230px);
}
.bubbles-small g:nth-of-type(9) circle {
  animation: wobble 3s 275ms infinite ease-in-out;
}
.bubbles-small g:nth-of-type(10) {
  animation: up 6s 900ms infinite;
}
.bubbles-small g:nth-of-type(10) g {
  transform: translateX(730px);
}
.bubbles-small g:nth-of-type(10) circle {
  animation: wobble 2s 905ms infinite ease-in-out;
}
@keyframes wobble {
  33% {
    transform: translateX(-50px);
  }
  66% {
    transform: translateX(50px);
  }
}
@keyframes up {
  0% {
    opacity: 0;
  }
  10%,
  90% {
    opacity: 1;
  }
  100% {
    opacity: 0;
    transform: translateY(-1024px);
  }
}
</style>
