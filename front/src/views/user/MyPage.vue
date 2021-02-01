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
  created() {},
};
</script>

<style></style>
