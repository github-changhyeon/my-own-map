<template>
  <div>
    My Page
    <div>
      <UserInfo />
    </div>
    <div>
      <TimeLine />
    </div>
  </div>
</template>

<script>
//npm install vue-moment --save
import TimeLine from '@/components/user/TimeLine';
import UserInfo from '@/components/user/UserInfo';

import Vue from 'vue';
// import axios from 'axios';
import vueMoment from 'vue-moment';

Vue.use(vueMoment);

export default {
  name: 'MyPage',
  components: {
    UserInfo,
    TimeLine,
  },
  data() {
    return {
      myImg: '',
    };
  },
  methods: {},
  created() {
    // 하단 네브바로 mypage로 오면 본인의 uid로 axios요청
    //    => const data = response.data
    //
    // else -> 상대방 페이지 => params에 userDto 등의 데이터가 있으니까 필요할때 axios하던지말던지
  },
};
</script>

<style></style>
