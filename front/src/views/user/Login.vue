<template>
  <div>
    안녕
    <form action="/login" method="POST">
      <input type="text" name="username" v-model="loginForm.username" /><br />
      <input type="password" name="password" v-model="loginForm.username" /><br />
      <button @click="login">로그인</button>
    </form>
    <NaverLogin />
  </div>
</template>

<script>
import { login } from '@/api/user.js';
import NaverLogin from '@/components/user/NaverLogin';

export default {
  name: 'Login',
  components: {
    NaverLogin,
  },
  data() {
    return {
      // CLIENT_ID: 'yPZ8zfbupxQS3jRvZDvP',
      // redirectURI: 'http://localhost:8080/naver/login',
      // //  FIXME state 값 random string 으로 변경
      // state: 123,
      // naverLoginURL: 'https://nid.naver.com/oauth2.0/authorize?response_type=code',

      loginForm: {
        username: '',
        password: '',
      },
    };
  },
  methods() {
    login(
      this.loginForm,
      (response) => {
        console.log(response);
      },
      (error) => {
        console.log(error);
        alert('실패.');
      }
    );
  },
  created() {
    // this.naverLoginURL += '&client_id=' + this.CLIENT_ID;
    // this.naverLoginURL += '&redirect_uri=' + this.redirectURI;
    // this.naverLoginURL += '&state=' + this.state;
  },
};
</script>

<style></style>
