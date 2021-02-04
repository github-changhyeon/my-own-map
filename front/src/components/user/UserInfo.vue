<template>
  <div>
    User Info

    <div>
      <v-avatar>
        <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" />
      </v-avatar>
      <div>
        <v-icon @click="doFollow">mdi-account-plus</v-icon>
        <!-- <v-icon @click="dofollow">mdi-account-minus</v-icon> -->
      </div>
      <div @click="goToFollowingList">
        팔로잉 : {{ followingList.length }}
        <Follow :users="followingList" />
      </div>
      <br />

      <div @click="goToFollowerList">
        팔로워 : {{ followerList.length }}
        <Follow :users="followerList" />
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
      uid: 0,
      followingList: [
        {
          id: 1,
          username: 'Al',
          email: '',
          profileImg: '',
          stateMsg: '',
        },
        {
          id: 2,
          username: 'AaaAl',
          email: '',
          profileImg: '',
          stateMsg: '',
        },
        {
          id: 3,
          username: 'bbbbAl',
          email: '',
          profileImg: '',
          stateMsg: '',
        },
      ],
      followerList: [
        {
          id: 1,
          username: 'Al',
          email: '',
          profileImg: '',
          stateMsg: '',
        },
        {
          id: 2,
          username: '333l',
          email: '',
          profileImg: '',
          stateMsg: '',
        },
      ],
    };
  },
  methods: {
    setToken: function() {
      const token = localStorage.getItem('jwt');
      const config = {
        headers: {
          jwt: `${token}`,
        },
      };
      return config;
    },
    goToFollowingList() {},
    goToFollowerList() {},
    doFollow() {
      const config = this.setToken();
      // jwt랑 팔로우할 상대방의 email
      // params로 받은 userdto의 이메일과 jwt디코드로 받은 email정보와 같으면 본인
      // => isMine = true/false로 판단해서 버튼 가리기 트루면 본인이니까 axios 안하고
      // false면 다른사람페이지니까 버튼을 보여주고 axios 요청을 보내고
      // 토큰에 내정보는 있고, 여기 uid 내가 follow할 사랆의 uid
      console.log('dofollow');
      console.log(this.uid);
      axios
        .get(`http://localhost:8080/follow/doFollow/${this.uid}`, config)
        .then((response) => {
          console.log(response);
          // console.log(response.data.object.body.object);
          // 이 uid의 팔로워 list를 넘겨받고-> response.data
          this.followerList = response.data.object.body.object;
          console.log(this.followerList);
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
  created() {
    // this.uid = this.$route.parmas.uid;
    const config = this.setToken();
    const uid = this.$route.params.uid;
    this.uid = uid;
    // console.log(uid);
    // => isMine = true/false로 판단해서 버튼 가리기 트루면 본인이니까 axios 안하고
    console.log(config);
    // props로 mypage받은 user정보를 이용해서(token말고 uid나 email 이런걸로) axios 요청. 본인의 팔로워 팔로잉 받아오는거.
    axios
      .get(`http://localhost:8080/follow/findFollowing/${uid}`)
      .then((response) => {
        // console.log(response);
        this.followingList = response.data.object;
        // console.log(this.followingList);
      })
      .catch((error) => {
        console.log(error);
      });
    axios
      .get(`http://localhost:8080/follow/findFollower/${uid}`)
      .then((response) => {
        // console.log(response);
        this.followerList = response.data.object;
        // console.log(this.followerList);
      })
      .catch((error) => {
        console.log(error);
      });
  },
};
</script>

<style></style>
