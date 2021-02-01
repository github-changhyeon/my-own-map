<template>
  <div>
    <div class="main">
      <h1 class="main-title">Log In</h1>
    </div>
    <div class="login-form">
      <div>
        <label for="email">이메일 </label>
        <input placeholder="아이디를 입력해주세요" type="text" id="email" v-model="loginForm.email" />
      </div>
      <div class="password-input">
        <label for="password">비밀번호 </label>
        <input placeholder="비밀번호를 입력해주세요" type="password" id="password" v-model="loginForm.password" @keypress.enter="login(loginForm)" />
      </div>
      <v-button variant="primary" @click="checkLogin">로그인</v-button>
      <NaverLogin />
    </div>
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
        email: '',
        password: '',
      },
    };
  },
  methods: {
    checkLogin() {
      login(
        this.loginForm,
        (response) => {
          console.log(response);
          console.log('111111');
          localStorage.setItem('jwt', response.data.object);
          this.$router.push('/');
        },
        (error) => {
          console.log(error);
          alert('실패.');
        }
      );
    },
  },
  created() {
    // this.naverLoginURL += '&client_id=' + this.CLIENT_ID;
    // this.naverLoginURL += '&redirect_uri=' + this.redirectURI;
    // this.naverLoginURL += '&state=' + this.state;
  },
};
</script>

<style scoped>
.main {
  height: 400px;
  /* background-image: url(https://cdn.wadiz.kr/ft/images/green001/2019/1114/20191114141744658_46316.gif); */
  margin-bottom: 80px;
  background-size: 1920px;
  filter: grayscale(100%);
}

.main-title {
  color: white;
  position: relative;
  padding-top: 180px;
  font-weight: bold;
}

.login-form {
  margin-bottom: 80px;
}

.password-input {
  margin-bottom: 20px;
}

label {
  font-weight: bold;
  color: rgb(41, 109, 236);
  margin-right: 5px;
}

input {
  width: 250px;
}
</style>
