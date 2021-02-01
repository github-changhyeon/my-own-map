<template>
  <div>
    <a :href="naverLoginURL"
      >네아로
      <!-- <img src="@/assets/Naver_Login.PNG" alt="" /> -->
    </a>
  </div>
</template>

<script>
export default {
  name: 'NaverLogin',
  data() {
    return {
      CLIENT_ID: 'yPZ8zfbupxQS3jRvZDvP',
      redirectURI: 'http://localhost:8080/naver/login',
      //  FIXME state 값 random string 으로 변경
      state: 123,
      naverLoginURL: 'https://nid.naver.com/oauth2.0/authorize?response_type=code',

      loginForm: {
        username: '',
        password: '',
      },
    };
  },
  created() {
    this.naverLoginURL += '&client_id=' + this.CLIENT_ID;
    this.naverLoginURL += '&redirect_uri=' + this.redirectURI;
    this.naverLoginURL += '&state=' + this.state;
  },
};
</script>

<style></style>
