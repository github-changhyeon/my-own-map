<template>
  <div>
    <img class="profileImage" :src="fullProfileUrl" />
    <v-file-input
      v-if="isSameUser"
      class="addbutton"
      v-model="profileImage"
      accept="image/*"
      hide-input
      prepend-icon="mdi-plus"
      @change="changeProfileFunc"
    ></v-file-input>
  </div>
</template>

<script>
import { updateUser } from '@/api/user.js';

export default {
  name: 'UserPicture',
  components: {},
  props: ['isSameUser', 'propsUserDto'],
  data: function () {
    return {
      profileImageUrl: 'DefaultProfileImage.png',
      profileImage: {},
      uid: '',
      tokenData: '',
      userDto: {},
      fullProfileUrl:
        'https://i4b107.p.ssafy.io/images/profileImages/DefaultProfileImage.png',
    };
  },
  watch: {
    propsUserDto: function (val) {
      console.log(this.userDto, 'watch');
      this.userDto = val;
      this.profileImageUrl = this.userDto.profileImagePath;
      this.fullProfileUrl = `https://i4b107.p.ssafy.io/images/profileImages/${this.profileImageUrl}`;
    },
    profileImageUrl: function (val) {
      this.profileImageUrl = val;
      console.log(this.profileImageUrl, 'watch 중');
      this.fullProfileUrl = `https://i4b107.p.ssafy.io/images/profileImages/${this.profileImageUrl}`;
    },
  },
  methods: {
    changeProfileFunc(e) {
      console.log(e, typeof e, '이미지 e');
      this.profileImageUrl = URL.createObjectURL(this.profileImage);
      console.log(this.profileImageUrl, '이미지url');
      const formData = new FormData();
      formData.append('file', e);
      this.userDto = this.propsUserDto;
      formData.append(
        'user',
        new Blob([JSON.stringify(this.userDto)], { type: 'application/json' })
      );
      console.log('여기들어왔나');
      console.log(this.userDto, '들어왓나');
      updateUser(
        formData,
        (response) => {
          if (response.data.status) {
            console.log(response.data.object, '성공');
            this.profileImageUrl = response.data.object.profileImagePath;
            this.fullProfileUrl = `https://i4b107.p.ssafy.io/images/profileImages/${this.profileImageUrl}`;
          } else {
            alert('실패');
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
  },
  computed: {},
  created() {},
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
