<template>
  <div>
    <img class="profileImage" :src="profileImageUrl" />
    <v-file-input v-if="isSameUser" class="addbutton" v-model="profileImage" accept="image/*" hide-input prepend-icon="mdi-plus" @change="changeProfileFunc"></v-file-input>
  </div>
</template>

<script>
// import { getUserInfo } from '@/api/user.js';
// import jwt_decode from 'jwt-decode';

export default {
  name: 'UserPicture',
  components: {},
  props: ['isSameUser', 'propsUid'],
  data: function() {
    return {
      profileImageUrl: 'https://user-images.githubusercontent.com/68372599/107969212-d0284180-6ff2-11eb-80c0-b13c581eccf1.png',
      profileImage: {},
      uid: '',
      tokenData: '',
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
  created() {
    this.uid = this.propsUid;
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
