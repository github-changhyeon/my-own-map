<template>
  <v-row justify="center">
    <v-col cols="12" sm="8">
      <v-card>
        <v-card-title class="secondary darken-1">
          <span class="headline white--text">정보 변경하기</span>

          <v-spacer></v-spacer>
        </v-card-title>
        <v-divider inset></v-divider>
        <v-list>
          <v-list-item>
            <v-list-item-action style="margin-right: 10px">
              <v-avatar>
                <img :src="profileImageUrl" />
              </v-avatar>
            </v-list-item-action>
            <v-list-item-action style="margin-right: 10px">
              <v-file-input v-model="profileImage" accept="image/*" hide-input prepend-icon="mdi-account" @change="changeProfileFunc"></v-file-input>
            </v-list-item-action>
            <v-list-item-content>
              <v-text-field label="닉네임" placeholder="닉네임을 입력해 주세요" v-model="userDto.username"></v-text-field>
            </v-list-item-content>
          </v-list-item>

          <v-list-item>
            <v-list-item-action>
              <v-icon>mdi-email</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title>
                <v-text-field label="이메일" placeholder="이메일은 변경할 수 없습니다" v-model="userDto.email" disabled></v-text-field>
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item v-if="!isChangePassword">
            <v-list-item-action>
              <v-icon>mdi-lock-outline</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-text-field label="비밀번호" placeholder="Placeholder" v-model="userDto.password" disabled type="password"></v-text-field>
            </v-list-item-content>

            <v-list-item-action>
              <v-btn @click="changePasswordFunc">변경하기</v-btn>
            </v-list-item-action>
          </v-list-item>

          <v-list-item v-if="isChangePassword">
            <v-list-item-action>
              <v-icon>mdi-lock-outline</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-text-field label="새 비밀번호" placeholder="새 비밀번호를 입력해 주세요" v-model="userDto.password" type="password"></v-text-field>
            </v-list-item-content>

            <v-list-item-action>
              <v-btn @click="cancelChangePassword">취소하기</v-btn>
            </v-list-item-action>
          </v-list-item>

          <v-list-item v-if="isChangePassword">
            <v-list-item-action>
              <v-icon>mdi-lock-outline</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-text-field label="새 비밀번호 확인" placeholder="새 비밀번호를 입력해 주세요" v-model="passwordConfirm" type="password"></v-text-field>
            </v-list-item-content>
          </v-list-item>

          <v-list-item style="justify-content: center">
            <v-list-item-action>
              <!-- <div> -->
              <v-btn @click="updateUserFunc">
                <span> 정보수정 </span>
              </v-btn>
            </v-list-item-action>
            <v-list-item-action>
              <v-btn @click="deleteUserFunc">
                <span> 회원탈퇴 </span>
              </v-btn>
              <!-- </div> -->
            </v-list-item-action>
          </v-list-item>
        </v-list>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import { getUserInfo, deleteUser, updateUser } from '@/api/user.js';
import constants from '@/lib/constants.js';

export default {
  name: 'Navigation',
  components: {},
  props: ['propsUid'],
  computed: {},
  watch: {},
  created() {
    this.uid = this.propsUid;
    getUserInfo(
      this.uid,
      (response) => {
        if (response.data.status) {
          this.userDto = response.data.object;
          this.storePassword = this.userDto.password;
        } else {
          alert('유저 정보를 받아올 수 없습니다.');
        }
      },
      (error) => {
        console.log(error);
        alert('서버 에러');
      }
    );
  },
  methods: {
    changeProfileFunc(e) {
      console.log(e);
      this.profileImageUrl = URL.createObjectURL(this.profileImage);
    },
    changePasswordFunc() {
      this.userDto.password = '';
      this.isChangePassword = true;
    },
    cancelChangePassword() {
      this.userDto.password = this.storePassword;
      this.isChangePassword = false;
    },
    deleteUserFunc() {
      if (confirm('Do you really want to delete?')) {
        deleteUser(
          (response) => {
            if (response.data.status) {
              alert('회원 탈퇴가 완료되었습니다.');
              this.$router.replace({ name: constants.URL_TYPE.USER.LOGIN });
            } else {
              alert('회원 탈퇴를 할 수 없습니다.');
            }
          },
          (error) => {
            alert('서버 에러');
            console.log(error);
          }
        );
      }
    },
    updateUserFunc() {
      let formData = new FormData();
      formData.append('file', this.profileImage);
      formData.append('user', new Blob([JSON.stringify(this.userDto)], { type: 'application/json' }));
      console.log(new Blob([JSON.stringify(this.userDto)]));
      console.log('file', formData.get('file'));
      console.log('user', formData.get('user'));
      updateUser(
        formData,
        (response) => {
          if (response.data.status) {
            this.userDto = response.data.object;
            this.storePassword = this.userDto.password;
            alert('정보수정에 성공했습니다.');
          } else {
            alert('회원 정보 수정을 할 수 없습니다.');
          }
        },
        (error) => {
          alert('서버 에러');
          console.log(error);
        }
      );
    },
  },
  data: function() {
    return {
      profileImageUrl: '',
      profileImage: {},
      storePassword: '',
      passwordConfirm: '',
      uid: '',
      isChangePassword: false,
      userDto: {},
    };
  },
};
</script>
