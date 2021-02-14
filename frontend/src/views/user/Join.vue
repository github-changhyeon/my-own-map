<template>
  <div id="app">
    <v-app class="center">
      <main class="center" style="margin-top:50px; width:350px;">
        <img class="logo" src="@/assets/MOM_Icon.png" alt="" />
        <div class="titlebar">
          이미 회원이신가요?
          <router-link to="/"><a href="">로그인하기</a></router-link>
        </div>
        <!-- <v-form v-model="valid" ref="form"> -->
        <v-form ref="form">
          <div class="InfoName">
            이메일
          </div>
          <v-text-field style="padding-top:0px;" placeholder="이메일을 입력해 주세요." v-model="joinForm.email"></v-text-field>
          <div class="InfoName">
            비밀번호
          </div>
          <v-text-field style="padding-top:0px;" placeholder="비밀번호를 입력해 주세요." type="password" v-model="joinForm.password" min="8"></v-text-field>
          <div class="InfoName">
            비밀번호 확인
          </div>
          <v-text-field style="padding-top:0px;" placeholder="비밀번호를 다시 입력해주세요." type="password" v-model="joinForm.passwordConfirm" min="8"></v-text-field>
          <div class="InfoName">
            닉네임
          </div>
          <v-text-field style="padding-top:0px;" placeholder="닉네임을 입력해 주세요." v-model="joinForm.username"></v-text-field>
          <button class="joinbar" @click="joinUser">가입하기</button>
        </v-form>
      </main>
    </v-app>
  </div>
</template>

<script>
// import axios from 'axios';
import { join } from '@/api/user.js';
import constants from '@/lib/constants';

export default {
  name: 'Join',
  data() {
    return {
      joinForm: {
        email: '',
        password: '',
        passwordConfirm: '',
        username: '',
      },
    };
  },
  methods: {
    joinUser() {
      join(
        this.joinForm,
        (response) => {
          console.log(response);
          this.$router.push({ name: constants.URL_TYPE.USER.LOGIN });
        },
        (err) => {
          console.log(err);
        }
      );
    },
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

.joinbar {
  width: 350px;
  height: 50px;
  margin: 0 auto;
  background-color: #ff70bc;
  color: white;
  font-weight: bold;
  border-radius: 10px;
}

.joinbar:hover {
  box-shadow: 0 2px 4px rgba(216, 37, 136, 0.9);
  transform: translateY(1px);
}

.joinbar:focus {
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
