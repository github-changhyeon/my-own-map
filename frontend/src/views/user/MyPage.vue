<template>
  <div>
    My Page
    <v-btn v-if="isSameUser" @click="logout">로그아웃</v-btn>
    <div>
      <UserInfo :isSameUser="isSameUser" />
    </div>
    <div>
      <TimeLine />
    </div>
    <Navigation />
  </div>
</template>

<script>
import jwt_decode from 'jwt-decode';
//npm install vue-moment --save
import TimeLine from '@/components/user/TimeLine';
import UserInfo from '@/components/user/UserInfo';
import constants from '@/lib/constants.js';
import Navigation from '@/components/Navigation.vue';

// import { getUserInfo } from '@/api/user.js';

// import axios from 'axios';

// import Vue from 'vue';
// import vueMoment from 'vue-moment';

// Vue.use(vueMoment);

export default {
  name: 'MyPage',
  components: {
    UserInfo,
    TimeLine,
    Navigation,
  },
  data() {
    return {
      myImg: '',
      userDto: {},
      tokenData: '',
      isSameUser: true,
    };
  },
  methods: {
    logout() {
      localStorage.removeItem('jwt');
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

      // 로그인된상태니까 -> 본인이 본인 페이지 올때는 토큰으로 내 정보를 찾아서 채워야되고
      // (params가 비어있으면) -> jwt로 디코드해서 email axios요청

      // // 하단 네브바로 mypage로 안오고 다른 사람의 페이지를 볼때는 게시글이나 이런걸 타고들어오니까
      // // params가 있을거니까 여기에 userDto이런걸로 axios요청을 보내서 채운다.

      console.log(this.$route.params.uid, 'param <-> ', this.tokenData.uid);
      if (Number(this.$route.params.uid) === Number(this.tokenData.uid)) {
        console.log('본인입니다');
        this.isSameUser = true;

        // 팔로우버튼 자체를 on/off -> 팔로우버튼이 on -> isfollow -> +, -
        // 토큰 디코드해서 찍힌 uid or email로 article controller에 게시글 요청. 받아서 Userinfo components에 props,emit
      } else {
        console.log('본인이아님');
        this.isSameUser = false;
        console.log(this.$route.params);
        this.userDto = this.$route.params;
      }
    },
  },
  created() {
    const token = localStorage.getItem('jwt');
    this.tokenData = jwt_decode(token);
    // 로그인된상태니까 -> 본인이 본인 페이지 올때는 토큰으로 내 정보를 찾아서 채워야되고
    // (params가 비어있으면) -> jwt로 디코드해서 email axios요청

    // // 하단 네브바로 mypage로 안오고 다른 사람의 페이지를 볼때는 게시글이나 이런걸 타고들어오니까
    // // params가 있을거니까 여기에 userDto이런걸로 axios요청을 보내서 채운다.

    console.log(this.$route.params.uid, 'param <-> ', this.tokenData.uid);
    if (Number(this.$route.params.uid) === Number(this.tokenData.uid)) {
      console.log('본인입니다');
      this.isSameUser = true;

      // 팔로우버튼 자체를 on/off -> 팔로우버튼이 on -> isfollow -> +, -
      // 토큰 디코드해서 찍힌 uid or email로 article controller에 게시글 요청. 받아서 Userinfo components에 props,emit
    } else {
      console.log('본인이아님');
      this.isSameUser = false;
      console.log(this.$route.params);
      this.userDto = this.$route.params;
    }
  },
};
</script>

<style></style>
