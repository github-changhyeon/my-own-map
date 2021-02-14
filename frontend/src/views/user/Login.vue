<template>
  <div id="app">
    <v-app>
      <main>
        <v-container fluid fill-height class="loginOverlay">
          <v-layout flex align-center justify-center>
            <v-flex xs12 sm4 elevation-6>
              <v-toolbar class="pt-5 darken-4" style="background-color:#FF70BC">
                <v-toolbar-title class="white--text"
                  ><h4>MOM에 오신 것을 환영합니다.</h4></v-toolbar-title
                >
              </v-toolbar>
              <v-card>
                <v-card-text class="pt-4">
                  <div>
                    <!-- <v-form v-model="valid" ref="form"> -->
                    <v-form ref="form">
                      <v-text-field
                        name="email"
                        type="email"
                        label="이메일을 입력해 주세요."
                        v-model="loginForm.email"
                      ></v-text-field>
                      <v-text-field
                        name="password"
                        type="password"
                        label="비밀번호를 입력해 주세요."
                        v-model="loginForm.password"
                        min="8"
                      ></v-text-field>
                      <v-layout justify-space-between>
                        <v-btn @click="checkLogin">로그인 하기</v-btn>
                        <!-- <a :href="naverLoginURL">네이버 아이디로 로그인하기 -->
                        <NaverLogin />
                        <a href="">비밀번호 찾기</a>
                        <router-link to="/join"
                          ><a href="">회원가입</a></router-link
                        >
                      </v-layout>
                    </v-form>
                  </div>
                </v-card-text>
              </v-card>
            </v-flex>
          </v-layout>
        </v-container>
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
  // receiveMessage,
  requestPermission,
  getToken,
} from '@/api/notification.js';

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
      tokenData: '',
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
