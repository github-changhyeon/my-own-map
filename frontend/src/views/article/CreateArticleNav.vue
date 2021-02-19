<template>
  <div style=" padding-bottom:10px; position: fixed; z-index: 2;">
    <!-- <router-link to="/">
    </router-link> -->
    <button @click="goToMain" class="back"><v-icon>mdi-arrow-left</v-icon></button>
  </div>
</template>

<script>
import constants from '@/lib/constants';
import jwt_decode from 'jwt-decode';

export default {
  name: 'CreateArticleNav',
  methods: {
    goToMain() {
      const token = localStorage.getItem('jwt');
      let uid = jwt_decode(token).uid;
      this.$router.replace({ name: constants.URL_TYPE.HOME.MAIN, params: { uid: uid } });
    },
  },
};
</script>
