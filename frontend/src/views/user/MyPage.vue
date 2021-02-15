<template>
  <div>
    <div style="float:right">
      OO 님
      <v-btn v-if="isSameUser" @click="logout">로그아웃</v-btn>
    </div>
    <div>
      <UserInfo
        :isSameUser="isSameUser"
        :followerList="followerList"
        :followingList="followingList"
      />
    </div>
    <!-- <div>
      <TimeLine />
    </div> -->
    <v-card>
      <v-tabs centered style="z-index: 2">
        <!-- <v-tab @click="isOpen = 1" style="width: 50vw">내 게시글 보기</v-tab>
        <v-tab @click="isOpen = 2" style="width: 5vw"><v-icon>mdi-heart</v-icon></v-tab>
        <v-tab @click="isOpen = 3" style="width: 5vw"><v-icon>mdi-lock</v-icon></v-tab> -->
        <v-tab style="width: 5vw"><v-icon>mdi-cog</v-icon></v-tab>
      </v-tabs>
    </v-card>
    <!-- <PublicNewsFeed v-if="isOpen === 1" :propsUid="uid" />
    <FavoriteNewsFeed v-if="isOpen === 2" :propsUid="uid" />
    <PrivateNewsFeed v-if="isOpen === 3" :propsUid="uid" /> -->
    <ChangeInfo :propsUid="uid" />
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
import ChangeInfo from '@/components/user/ChangeInfo.vue';

// //NewsFeed
// import PublicNewsFeed from '@/components/sns/PublicNewsFeed.vue';
// import PrivateNewsFeed from '@/components/sns/PrivateNewsFeed.vue';
// import FavoriteNewsFeed from '@/components/sns/FavoriteNewsFeed.vue';

// import { getUserInfo } from '@/api/user.js';
import { findFollower, findFollowing } from '@/api/user.js';
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
    ChangeInfo,
  },
  data() {
    return {
      myImg: '',
      userDto: {},
      tokenData: '',
      isSameUser: true,
      isOpen: '',
      uid: '',
      followingList: [],
      followerList: [],
    };
  },
  methods: {
    logout() {
      deleteToken(() => {
        console.log('token delete');
        deleteFcmToken(
          (success) => {
            if (success.data.status) {
              console.log('토큰 delete 성공');
              localStorage.removeItem('jwt');
            } else {
              console.log('fcm 토큰 delete 실패');
            }
          },
          (error) => {
            console.log(error);
            alert('서버 에러');
          }
        );
      });
      // location.reload();
      // this.$router.replace({ name: constants.URL_TYPE.USER.LOGIN });
      // this.$router.go()
      this.$router.replace({ name: constants.URL_TYPE.USER.LOGIN });
    },
  },
  watch: {
    '$route.params.uid': function(uid) {
      console.log(uid);
      const token = localStorage.getItem('jwt');
      this.tokenData = jwt_decode(token);
      this.uid = this.$route.params.uid;

      // console.log(this.$route.params.uid, 'param <-> ', this.tokenData.uid);
      if (Number(this.$route.params.uid) === Number(this.tokenData.uid)) {
        console.log('본인입니다');
        this.isSameUser = true;

        // 팔로우버튼 자체를 on/off -> 팔로우버튼이 on -> isfollow -> +, -
        // 토큰 디코드해서 찍힌 uid or email로 article controller에 게시글 요청. 받아서 Userinfo components에 props,emit
      } else {
        console.log('본인이아님');
        this.isSameUser = false;
        this.userDto = this.$route.params;
      }
      findFollowing(
        this.uid,
        (response) => {
          this.followingList = response.data.object;
          // this.$emit('followingList', this.followingList);
        },
        (error) => {
          console.log(error);
          console.log('findfollowing');
        }
      );

      findFollower(
        this.uid,
        (response) => {
          this.followerList = response.data.object;
          console.log(this.followerList);
          // this.$emit('followerList', this.followerList);
        },
        (error) => {
          console.log(error);
        }
      );
    },
  },
  created() {
    // const uid = this.$route.params.uid
    const token = localStorage.getItem('jwt');
    this.tokenData = jwt_decode(token);
    this.uid = this.$route.params.uid;
    // 로그인된상태니까 -> 본인이 본인 페이지 올때는 토큰으로 내 정보를 찾아서 채워야되고
    // (params가 비어있으면) -> jwt로 디코드해서 email axios요청

    // // 하단 네브바로 mypage로 안오고 다른 사람의 페이지를 볼때는 게시글이나 이런걸 타고들어오니까
    // // params가 있을거니까 여기에 userDto이런걸로 axios요청을 보내서 채운다.

    console.log(this.$route.params.uid, '히스토리에서온 파람 uid');
    console.log(this.tokenData.uid, 'jwt uid');

    // console.log(this.$route.params.uid, 'param <-> ', this.tokenData.uid);
    if (Number(this.$route.params.uid) === Number(this.tokenData.uid)) {
      console.log('본인입니다');
      this.isSameUser = true;
    } else {
      console.log('본인이아님');
      this.isSameUser = false;
      console.log(this.$route.params);
      this.userDto = this.$route.params;
    }
    findFollowing(
      this.uid,
      (response) => {
        this.followingList = response.data.object;
        // this.$emit('followingList', this.followingList);
      },
      (error) => {
        console.log(error);
        console.log('findfollowing');
      }
    );

    findFollower(
      this.uid,
      (response) => {
        this.followerList = response.data.object;
        console.log(this.followerList);
        // this.$emit('followerList', this.followerList);
      },
      (error) => {
        console.log(error);
      }
    );
  },
};
</script>

<style></style>
