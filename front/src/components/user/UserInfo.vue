<template>
  <div>
    User Info

    <div>
      <v-avatar>
        <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" />
      </v-avatar>
      <div>
        <v-icon @click="dofollow">mdi-account-plus</v-icon>
        <v-icon @click="dofollow">mdi-account-minus</v-icon>
      </div>
      <div>
        팔로잉
        <Follow :users="followingList" :followBtn="followBtn" />
      </div>
      <div>
        팔로워
        <Follow :users="followerList" :followBtn="followBtn" />
      </div>
    </div>
  </div>
</template>

<script>
// import axios from 'axios';
import Follow from '@/components/user/Follow';
import axios from 'axios';

export default {
  name: 'UserInfo',
  components: {
    Follow,
  },
  props: {
    profile: Object,
  },
  data() {
    return {
      myImg: '',
      followingList: [
        {
          id: 1,
          nickname: 'Al',
        },
        {
          id: 2,
          nickname: 'AgFe',
        },
        {
          id: 3,
          nickname: 'bummy',
        },
      ],
      followerList: [
        {
          id: 1,
          nickname: 'Al',
        },
        {
          id: 2,
          nickname: 'AgFe',
        },
        {
          id: 3,
          nickname: 'bummy',
        },
      ],
    };
  },
  methods: {
    doFollow() {
      // params로 받은 userdto의 이메일과 jwt디코드로 받은 email정보와 같으면 본인
      // => isMine = true/false로 판단해서 버튼 가리기 트루면 본인이니까 axios 안하고
      // false면 다른사람페이지니까 버튼을 보여주고 axios 요청을 보내고
      // 토큰에 내정보는 있고, 여기 email은 내가 follow할 사랆의 email
      axios.get(`http://127.0.0.1:8080/follow/doFollow/${this.email}`);
    },
  },
  created() {
    // => isMine = true/false로 판단해서 버튼 가리기 트루면 본인이니까 axios 안하고

    // props로 mypage받은 user정보를 이용해서(token말고 uid나 email 이런걸로) axios 요청. 본인의 팔로워 팔로잉 받아오는거.
    axios.get(`http://localhost:8080/follow/findFollow/`);
  },
};
</script>

<style></style>
