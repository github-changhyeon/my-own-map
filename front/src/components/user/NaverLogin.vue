<template>
  <div>
    <a :href="naverLoginURL"
      >네아로
      <!-- <img src="@/assets/Naver_Login.PNG" alt="" /> -->
    </a>
    <div @click="naverLogin">
      네이버로그인
    </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'NaverLogin',
  data() {
    return {
      responseURL: '',
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
  methods: {
    naverLogin() {
      axios
        .get(`https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${this.CLIENT_ID}&redirect_uri=${this.redirectURI}&state=${this.state}`)
        .then((response) => {
<<<<<<< HEAD
          console.log(response);
=======
>>>>>>> f36bf4d26086760e61e8e903eae62f310fd2ff78
          // console.log(response.request);
          this.responseURL = response.request.responseURL;
          // this.$router.push(response.request.responseURL);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  created() {
    this.naverLoginURL += '&client_id=' + this.CLIENT_ID;
    this.naverLoginURL += '&redirect_uri=' + this.redirectURL;
    this.naverLoginURL += '&state=' + this.state;
  },
};
</script>

<style></style>
