<template>
  <div>
    <img
      class="profileImage"
      :src="
        require(`https://i4b107.p.ssafy.io/images/profileImages/${profileImageUrl}`)
      "
    />
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
// import jwt_decode from 'jwt-decode';

export default {
  name: 'UserPicture',
  components: {},
  props: ['isSameUser', 'propsUserDto'],
  data: function () {
    return {
      profileImageUrl: 'basic_user.png',
      // profileImageUrl: 'https://ssl.pstatic.net/static/newsstand/up/2013/0813/nsd114516931.gif',
      profileImage: {},
      uid: '',
      tokenData: '',
      userDto: {},
    };
  },
  watch: {
    propsUserDto: function (val) {
      console.log('watch');
      this.userDto = val;
      this.profileImageUrl = this.userDto.profileImagePath;
    },
    profileImageUrl: function (val) {
      this.profileImageUrl = val;
      console.log(this.profileImageUrl, 'watch 중');
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
            this.profileImageUrl =
              'https://i4b107.p.ssafy.io/images/profileImages/' +
              response.data.object.profileImagePath;
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
  created() {
    console.log(this.userDto, 'userpicture created userdto');
    console.log(this.propsUserDto, 'userpicture created userdto');
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
