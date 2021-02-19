<template>
  <div>
    <v-btn
      icon
      color="black"
      style="position: fixed; display: flex; top: 10px; z-index: 2"
      @click="goBack"
    >
      <v-icon>mdi-arrow-left</v-icon>
    </v-btn>
    <v-row justify="center" style="margin-top: 50px">
      <v-col cols="12" sm="8">
        <v-card>
          <v-card-title class="secondary darken-1">
            <span class="headline white--text">정보 변경하기</span>

            <v-spacer></v-spacer>
          </v-card-title>
          <v-divider inset></v-divider>
          <v-list>
            <v-list-item>
              <!-- <v-list-item-action style="margin-right: 10px">
              <v-avatar>
                <img :src="profileImageUrl" />
              </v-avatar>
            </v-list-item-action>
            <v-list-item-action style="margin-right: 10px">
              <v-file-input v-model="profileImage" accept="image/*" hide-input prepend-icon="mdi-account" @change="changeProfileFunc"></v-file-input>
            </v-list-item-action> -->
              <v-list-item-action style="margin-top: 0px">
                <v-icon>mdi-account</v-icon>
              </v-list-item-action>
              <v-list-item-content>
                <v-text-field
                  label="닉네임"
                  placeholder="닉네임을 입력해 주세요"
                  v-model="userDto.username"
                ></v-text-field>
              </v-list-item-content>
            </v-list-item>

            <v-list-item>
              <v-list-item-action>
                <v-icon>mdi-message-draw</v-icon>
              </v-list-item-action>

              <v-list-item-content>
                <v-list-item-title>
                  <v-text-field
                    label="상태 메세지"
                    placeholder="메세지를 입력해 주세요"
                    v-model="userDto.stateMsg"
                  ></v-text-field>
                </v-list-item-title>
              </v-list-item-content>
            </v-list-item>

            <v-list-item>
              <v-list-item-action>
                <v-icon>mdi-email</v-icon>
              </v-list-item-action>

              <v-list-item-content>
                <v-list-item-title>
                  <v-text-field
                    label="이메일"
                    placeholder="이메일은 변경할 수 없습니다"
                    v-model="userDto.email"
                    disabled
                  ></v-text-field>
                </v-list-item-title>
              </v-list-item-content>
            </v-list-item>

            <v-list-item v-if="!isChangePassword">
              <v-list-item-action style="margin-top: 0px">
                <v-icon>mdi-lock-outline</v-icon>
              </v-list-item-action>

              <v-list-item-content>
                <v-text-field
                  label="비밀번호"
                  placeholder="Placeholder"
                  v-model="userDto.password"
                  disabled
                  type="password"
                ></v-text-field>
              </v-list-item-content>

              <v-list-item-action>
                <v-btn color="secondary" @click="changePasswordFunc"
                  >변경하기</v-btn
                >
              </v-list-item-action>
            </v-list-item>

            <v-list-item v-if="isChangePassword">
              <v-list-item-action>
                <v-icon>mdi-lock-outline</v-icon>
              </v-list-item-action>

              <v-list-item-content>
                <v-text-field
                  label="새 비밀번호"
                  placeholder="새 비밀번호를 입력해 주세요"
                  v-model="userDto.password"
                  type="password"
                ></v-text-field>
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
                <v-text-field
                  label="새 비밀번호 확인"
                  placeholder="새 비밀번호를 입력해 주세요"
                  v-model="passwordConfirm"
                  type="password"
                ></v-text-field>
              </v-list-item-content>
            </v-list-item>

            <v-list-item-action>
              <!-- <div> -->
              <v-btn
                style="margin-left: 220px; font-weight: bold"
                color="primary"
                @click="updateUserFunc"
              >
                정보수정
              </v-btn>
            </v-list-item-action>
            <!-- <button class="deletebutton" @click="deleteUserFunc">
              회원탈퇴
            </button> -->
            <!-- </div> -->
          </v-list>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import {
  getUserInfo,
  // deleteUser,
  updateUser,
} from '@/api/user.js';
import constants from '@/lib/constants.js';
import jwt_decode from 'jwt-decode';

export default {
  name: 'ChangeInfo',
  components: {},
  props: ['propsUid'],
  computed: {},
  watch: {},
  created() {
    const token = localStorage.getItem('jwt');
    // this.tokenData = jwt_decode(token);
    this.uid = jwt_decode(token).uid;
    getUserInfo(
      this.uid,
      (response) => {
        if (response.data.status) {
          this.userDto = response.data.object;
          this.storePassword = this.userDto.password;
        }
      },
      (error) => {
        console.log(error);
      }
    );
  },
  methods: {
    goBack() {
      // 뒤로가기
      this.$router.go(-1);
    },

    changePasswordFunc() {
      this.userDto.password = '';
      this.isChangePassword = true;
    },
    cancelChangePassword() {
      this.userDto.password = this.storePassword;
      this.isChangePassword = false;
    },
    // deleteUserFunc() {
    //   if (confirm('탈퇴하시겠습니까??')) {
    //     deleteUser(
    //       (response) => {
    //         if (response.data.status) {
    //           alert('회원 탈퇴가 완료되었습니다.');
    //           this.$router.replace({ name: constants.URL_TYPE.USER.LOGIN });
    //         } else {
    //           alert('회원 탈퇴를 할 수 없습니다.');
    //         }
    //       },
    //       (error) => {
    //         alert('서버 에러');
    //         console.log(error);
    //       }
    //     );
    //   }
    // },
    updateUserFunc() {
      if (confirm('수정하시겠습니까?')) {
        let formData = new FormData();
        formData.append('file', this.profileImage);
        formData.append(
          'user',
          new Blob([JSON.stringify(this.userDto)], { type: 'application/json' })
        );

        updateUser(
          formData,
          (response) => {
            if (response.data.status) {
              this.userDto = response.data.object;
              this.storePassword = this.userDto.password;
              this.stateMsg = this.userDto.stateMsg;
              alert('정보수정에 성공했습니다.');
              this.$router.push({
                name: constants.URL_TYPE.USER.MYPAGE,
                params: { uid: this.userDto.uid },
              });
            } else {
              alert('회원 정보 수정을 할 수 없습니다.');
            }
          },
          (error) => {
            console.log(error);
          }
        );
      }
    },
  },
  data: function () {
    return {
      profileImageUrl: '',
      profileImage: {},
      stateMsg: '',
      storePassword: '',
      passwordConfirm: '',
      uid: '',
      isChangePassword: false,
      userDto: {},
    };
  },
};
</script>

<style scoped>
.deletebutton {
  margin-left: 460px;
  color: red;
  text-decoration-line: underline;
  font-size: 14px;
}
</style>
