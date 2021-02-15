<template>
  <div>
    <div>
      <img class="profileImage" :src="profileImageUrl" />
    </div>
    <v-file-input class="addbutton" v-model="profileImage" accept="image/*" hide-input prepend-icon="mdi-plus" @change="changeProfileFunc"></v-file-input>
  </div>
</template>

<script>
import { getUserInfo } from '@/api/user.js';

export default {
  name: 'UserPicture',
  components: {},
  props: ['propsUid'],
  data: function() {
    return {
      profileImageUrl: '@/assets/basic_user.png',
      profileImage: {},
      uid: '',
      userDto: {},
    };
  },
  methods: {
    changeProfileFunc(e) {
      console.log(e);
      this.profileImageUrl = URL.createObjectURL(this.profileImage);
    },
  },
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
};
</script>

<style scoped>
.profileImage {
  display: block;
  margin: 0 auto;
  height: 500px;
  width: 300px;
}

.addbutton {
  display: flex;
  justify-content: center;
}
</style>
