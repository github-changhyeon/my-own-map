<template>
  <v-bottom-navigation
    :value="value"
    color="primary"
    style="position: fixed; bottom: 0px; z-index: 2; display: flex; justify-content: space-around; background-color:white;"
    class="nav"
  >
    <router-link to='/newsfeed' style=textDecoration:none;>
      <v-btn style="background-color:white; margin-top:8px;">
        <span>NewsFeed</span>
        <v-icon>mdi-newspaper-variant</v-icon>
      </v-btn>
    </router-link>
    <router-link to='/main/:uid' style=textDecoration:none;>
      <v-btn style="background-color:white; margin-top:8px;">
        <span>Home</span>
        <v-icon>mdi-home</v-icon>
      </v-btn>
    </router-link>
    <router-link to=/users/:uid style=textDecoration:none;>
      <v-btn style="background-color:white; margin-top:8px;">
        <span>MyPage</span>
        <v-icon>mdi-account</v-icon>
      </v-btn>
    </router-link>
  </v-bottom-navigation>
</template>

<script>
  export default {
    data: () => ({ value: 1 }),
    methods: {
      // goToNewsFeed() {

      // },
      // goToMypage() {
        
      // },
    }
  }
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