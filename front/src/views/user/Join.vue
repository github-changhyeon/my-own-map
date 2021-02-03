<template>
  <div id="app">
    <v-app>
      <main>
        <v-container fluid fill-height class="loginOverlay">
          <v-layout flex align-center justify-center>
            <v-flex xs12 sm4 elevation-6>
              <v-toolbar class="pt-5 blue darken-4">
                <v-toolbar-title class="white--text"><h4>회원가입</h4></v-toolbar-title>
              </v-toolbar>
              <v-card>
                <v-card-text class="pt-4">
                  <div>
                    <v-form v-model="valid" ref="form">
                      <v-text-field label="이메일을 입력해 주세요." v-model="joinForm.email"></v-text-field>
                      <v-text-field label="비밀번호를 입력해 주세요." type="password" v-model="joinForm.password" min="8"></v-text-field>
                      <v-text-field label="비밀번호를 다시 입력해주세요." type="password" v-model="joinForm.passwordConfirm" min="8"></v-text-field>
                      <v-text-field label="닉네임을 입력해 주세요." v-model="joinForm.username"></v-text-field>
                      <v-layout justify-space-between>
                        <v-btn @click="joinUser">등록하기</v-btn>
                        <router-link to="/login"><a href="">뒤로가기</a></router-link>
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

<style></style>
