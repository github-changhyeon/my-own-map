<template>
  <div>
    User Info

    <div>
      <v-avatar>
        <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" />
      </v-avatar>
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
  methods: {},
  created() {
    // axios.post(`http://localhost:8080/users/follow/${userId}/`);
  },
};
</script>

<style></style>
