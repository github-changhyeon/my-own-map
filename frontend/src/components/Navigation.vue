<template>
  <v-bottom-navigation :value="value" color="primary" style="position: fixed;bottom: 0px; z-index: 2; display: flex; justify-content: space-around; background-color:white;" class="nav">
    <!-- <router-link :to="`/main/${this.uid}`" style="textDecoration:none;">
    <v-btn @click="$router.push(`/main/${uid}`)" style="background-color:white; margin-top:8px;">
      <v-icon>mdi-home</v-icon>
    </v-btn>
    </router-link>
    <router-link to="/newsfeed" style="textDecoration:none;">
    <v-btn @click="$router.push(`/newsfeed`)" style="background-color:white; margin-top:8px;">
      <v-icon>mdi-newspaper-variant</v-icon>
    </v-btn>
    </router-link>
    <router-link :to="`/articles/create`" style="textDecoration:none;">
    <v-btn @click="$router.push(`/articles/create`)" style="background-color:white; margin-top:8px;">
      <img width="40px" height="44px" src="@/assets/MOM_Icon.png" />
    </v-btn>
    </router-link>
    <router-link :to="`/articles/create`" style="textDecoration:none;">
    <v-btn @click="$router.push(`/articles/create`)" style="background-color:white; margin-top:8px;">
      <v-icon>mdi-plus</v-icon>
    </v-btn>
    </router-link>
    <router-link :to="`/users/${this.uid}`" style="textDecoration:none;">
    <v-btn @click="$router.push(`/user/${uid}`)" style="background-color:white; margin-top:8px;">
      <v-icon>mdi-account</v-icon>
    </v-btn>
    </router-link> -->
    <!-- 버튼을 router link로 해보자! -->

    <!-- <router-link :to="`/main/${this.uid}`" style="textDecoration:none;"> -->
    <v-btn :to="`/main/${this.uid}`" replace style="background-color:white; margin-top:8px;">
      <v-icon>mdi-home</v-icon>
    </v-btn>
    <!-- </router-link> -->
    <!-- <router-link to="/newsfeed" style="textDecoration:none;"> -->
    <v-btn to="/newsfeed" replace style="background-color:white; margin-top:8px;">
      <v-icon>mdi-newspaper-variant</v-icon>
    </v-btn>
    <!-- </router-link> -->
    <!-- <router-link :to="`/articles/create`" style="textDecoration:none;"> -->
    <v-btn to="`/articles/create`" replace style="background-color:white; margin-top:8px;">
      <v-icon>mdi-plus</v-icon>
    </v-btn>
    <!-- </router-link> -->
    <!-- <router-link :to="`/articles/create`" style="textDecoration:none;"> -->
    <v-btn to="`/articles/create`" replace style="background-color:white; margin-top:8px;">
      <img width="40px" height="44px" src="@/assets/MOM_Icon.png" />
    </v-btn>
    <!-- </router-link> -->

    <!-- <router-link :to="`/users/${this.uid}`" style="textDecoration:none;"> -->
    <v-btn :to="`/users/${this.uid}`" replace style="background-color:white; margin-top:8px;">
      <v-icon>mdi-account</v-icon>
    </v-btn>
    <!-- </router-link> -->
  </v-bottom-navigation>
</template>

<script>
import jwt_decode from 'jwt-decode';
export default {
  data: () => ({
    value: 0,
    uid: 0,
    isSelected: false,
    // uid:jwt_decode(localStorage.getItem('jwt')),
  }),
  // watch: {

  // },
  methods: {
    // goToNewsFeed() {
    // },
    // goToMypage() {
    // },
    // refreshToken(){
    // const token = localStorage.getItem('jwt');
    // this.uid = jwt_decode(token).uid;
    // }
    // one() {
    //   console.log('one', window.location.pathname);
    // },
    // two() {
    //   console.log('two', window.location.pathname);
    // },
    // three() {
    //   console.log('three', window.location.pathname);
    // },
    // four() {
    //   console.log('four', window.location.pathname);
    // },
    // five() {
    //   console.log('five', window.location.pathname);
    // },
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
  },
};
</script>

<style>
/* .nav{
    width: 100vw;
    display: flex;
    justify-content: space-around;
  } */

/* .button {
    background-color:white;
  } */
</style>
