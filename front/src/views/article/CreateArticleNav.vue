<template>
  <div>
    <!-- <router-link to="/">
    </router-link> -->
    <button @click="goToMain" class="back">메인으로 가기</button>
  </div>
</template>

<script>
import constants from '@/lib/constants';
import jwt_decode from 'jwt-decode';

export default {
  name: 'CreateArticleNav',
  methods: {
    goToMain(){
      const token = localStorage.getItem('jwt');
      let uid = jwt_decode(token).uid;
      this.$router.replace({ name: constants.URL_TYPE.HOME.MAIN, params: { uid: uid }});
    }
  },
};
</script>
