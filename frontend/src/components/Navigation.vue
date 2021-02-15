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
    <router-link :to="`/main/${this.uid}`" style="textDecoration:none;">
      <v-btn style="background-color:white; margin-top:8px;">
        <v-icon>mdi-home</v-icon>
      </v-btn>
    </router-link>
    <router-link to="/newsfeed" style="textDecoration:none;">
      <v-btn style="background-color:white; margin-top:8px;">
        <v-icon>mdi-newspaper-variant</v-icon>
      </v-btn>
    </router-link>
    <router-link :to="`/articles/create`" style="textDecoration:none;">
      <v-btn style="background-color:white; margin-top:8px;">
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </router-link>
    <router-link :to="`/articles/create`" style="textDecoration:none;">
      <v-btn style="background-color:white; margin-top:8px;">
        <img width="40px" height="44px" src="@/assets/MOM_Icon.png" />
      </v-btn>
    </router-link>

    <router-link :to="`/users/${this.uid}`" style="textDecoration:none;">
      <v-btn style="background-color:white; margin-top:8px;">
        <v-icon>mdi-account</v-icon>
      </v-btn>
    </router-link>
  </v-bottom-navigation>
</template>

<script>
import jwt_decode from 'jwt-decode';
export default {
  data: () => ({
    value: 1,
    uid: 0,
    // uid:jwt_decode(localStorage.getItem('jwt')),
  }),
  methods: {
    // goToNewsFeed() {
    // },
    // goToMypage() {
    // },
    // refreshToken(){
    // const token = localStorage.getItem('jwt');
    // this.uid = jwt_decode(token).uid;
    // }
  },
  created() {
    const token = localStorage.getItem('jwt');
    if (token !== null && token !== undefined) {
      this.uid = jwt_decode(token).uid;
    }
    console.log(token, 'uid는', this.uid);
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
