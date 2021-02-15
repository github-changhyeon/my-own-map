<template>
  <div id="app">
    <v-app class="center">
      <main class="center" style="margin-top:50px; width:350px;">
        <img class="logo" src="@/assets/MOM_Logo.png" alt="" />
        <div class="titlebar">
          회원이 아니신가요?
          <router-link to="/join"><a href="">회원가입 하기</a></router-link>
        </div>
        <!-- <v-form v-model="valid" ref="form"> -->
        <v-form ref="form">
          <div class="InfoName">
            이메일
          </div>
          <v-text-field style="padding-top:0px;" name="email" type="email" placeholder="이메일을 입력해 주세요." v-model="loginForm.email"></v-text-field>
          <div class="InfoName">
            비밀번호
          </div>
          <v-text-field style="padding-top:0px;" name="password" type="password" placeholder="비밀번호를 입력해 주세요." v-model="loginForm.password" min="8"></v-text-field>
          <v-layout justify-space-between>
            <v-btn @click="checkLogin()" color="primary" height="50" class="loginbar">로그인 하기</v-btn>
          </v-layout>
          <!-- <NaverLogin /> -->
          <div class="findPassword">
            <a href="">비밀번호 찾기</a>
          </div>
        </v-form>
      </main>
    </v-app>
  </div>
</template>

<script>
import jwt_decode from 'jwt-decode';

import { login } from '@/api/user.js';
import NaverLogin from '@/components/user/NaverLogin';
import { registFcmToken } from '@/api/fcm.js';

import constants from '@/lib/constants.js';
import {
  // deleteToken,
  receiveMessage,
  requestPermission,
  getToken,
} from '@/api/notification.js';

export default {
  name: 'Login',
  components: {
    // NaverLogin,
  },
  data() {
    return {
      // CLIENT_ID: 'yPZ8zfbupxQS3jRvZDvP',
      // redirectURI: 'http://localhost:8080/naver/login',
      // //  FIXME state 값 random string 으로 변경
      // state: 123,
      // naverLoginURL: 'https://nid.naver.com/oauth2.0/authorize?response_type=code',
      tokenData: '',
      loginForm: {
        email: '',
        password: '',
      },
    };
  },
  methods: {
    checkLogin() {
      console.log('아무거나');
      login(
        this.loginForm,
        (response) => {
          localStorage.setItem('jwt', response.data.object);

          this.tokenData = jwt_decode(response.data.object);
          // deleteToken();
          requestPermission((permission) => {
            if (permission === 'granted') {
              console.log('Notification permission granted.');
              getToken((currentToken) => {
                if (currentToken) {
                  console.log(currentToken, '토큰토큰');
                  registFcmToken(
                    currentToken,
                    (success) => {
                      if (success.data.status) {
                        console.log('fcm 토큰 regist 성공');
                      } else {
                        console.log('fcm 토큰 regist 실패');
                      }
                    },
                    (error) => {
                      console.log(error);
                      alert('서버 에러');
                    }
                  );
                  // Send the token to your server and update the UI if necessary
                  // ...
                } else {
                  // Show permission request UI
                  console.log(
                    'No registration token available. Request permission to generate one.'
                  );
                  // ...
                }
              });
              // TODO(developer): Retrieve a registration token for use with FCM.
              // ...
              receiveMessage();
            } else {
              console.log('Unable to get permission to notify.');
            }
          });
          // console.log(t, 't');
          this.$router.replace({
            name: constants.URL_TYPE.HOME.MAIN,
            params: { uid: this.tokenData.uid },
          });
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
.center {
  margin: 0 auto;
}

.logo {
  width: 200px;
  height: 150px;
  display: block;
  margin-left: auto;
  margin-right: auto;
}

.titlebar {
  font-size: 12px;
  text-align: center;
  font-weight: bold;
  padding-bottom: 30px;
}

.loginbar {
  width: 350px;
  height: 50px;
  margin: 0 auto;
  background-color: #ff70bc;
  color: white;
  box-shadow: 0 4px 16px rgba(216, 37, 136, 0.3);
  font-weight: bold;
  border-radius: 10px;
  transition: 0.3s;
}

.loginbar:hover {
  box-shadow: 0 2px 4px rgba(216, 37, 136, 0.9);
  transform: translateY(1px);
}

.loginbar:focus {
  outline: 0px;
}

.InfoName {
  color: pink;
}

.findPassword {
  color: grey;
  font-size: 15px;
  padding-top: 20px;
  float: right;
}
</style>
