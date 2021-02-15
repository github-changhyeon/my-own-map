<template>
  <div class="userinfo">
    <!-- <v-avatar>
      <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" />
    </v-avatar> -->
    <div class="map-follow-button">
      <v-btn style="margin-right: 20px;" class="mapbutton" color="primary" v-if="!isSameUser" @click="goToMap">지도 보기</v-btn>
      <div>
        <v-icon style="margin-right: 150px; top:-30px;" v-if="!isSameUser && !isFollow" @click="checkFollow">mdi-account-plus</v-icon>
        <v-icon style="margin-right: 150px; top:-30px;" v-if="!isSameUser && isFollow" @click="checkFollow">mdi-account-minus</v-icon>\
      </div>
    </div>
    <div class="word-spacing" style="margin-left:50px">
      <div @click="goToFollowerList">
        <span style="font-weight:bold;">팔로워</span>
        <br />
        <div style="margin-left:20px;">
          {{ followerList.length }}
        </div>
        <!-- <Follow :users="followerList" /> -->
      </div>

      <br />
      <div @click="goToFollowingList" style="margin-right:50px;">
        <span style="font-weight:bold;">팔로우</span>
        <br />
        <div style="margin-left:20px;">
          {{ followingList.length }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import constants from '@/lib/constants.js';
import { notifyAction } from '@/api/fcm.js';
import jwt_decode from 'jwt-decode';

// import Follow from '@/components/user/Follow';
import { doFollow, findFollower, findFollowing, isFollow } from '@/api/user.js';

export default {
  name: 'UserInfo',
  components: {
    // Follow,
  },
  props: {
    // followerList: Array,
    // followingList: Array,
    profile: Object,
    isSameUser: Boolean,
  },
  watch: {
    '$route.params.uid': function(uid) {
      const config = this.setToken();
      this.uid = uid;

      isFollow(
        this.uid,
        config,
        (response) => {
          console.log('isfollow?');
          // console.log(response);
          this.isFollow = response.data.status;
          console.log(this.isFollow);
        },
        (error) => {
          console.log(error);
        }
      );
    },
  },
  data() {
    return {
      myImg: '',
      uid: 0,
      isFollow: true,
      followerList: [],
      followingList: [],
      //   followingList: [
      //     {
      //       id: 1,
      //       username: 'Al',
      //       email: '',
      //       profileImg: '',
      //       stateMsg: '',
      //     },
      //     {
      //       id: 2,
      //       username: 'AaaAl',
      //       email: '',
      //       profileImg: '',
      //       stateMsg: '',
      //     },
      //     {
      //       id: 3,
      //       username: 'bbbbAl',
      //       email: '',
      //       profileImg: '',
      //       stateMsg: '',
      //     },
      //   ],
      //   followerList: [
      //     {
      //       id: 1,
      //       username: 'Al',
      //       email: '',
      //       profileImg: '',
      //       stateMsg: '',
      //     },
      //     {
      //       id: 2,
      //       username: '333l',
      //       email: '',
      //       profileImg: '',
      //       stateMsg: '',
      //     },
      //   ],
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
    goToFollowingList() {
      this.$router.push({ name: 'Follow', params: { follow: this.followingList } });
    },
    goToFollowerList() {
      this.$router.push({ name: 'Follow', params: { follow: this.followerList } });
    },
    goToMap() {
      this.$router.push({
        name: constants.URL_TYPE.HOME.MAIN,
        params: { uid: this.uid },
      });
    },
    checkFollow() {
      const config = this.setToken();
      doFollow(
        this.uid,
        config,
        (response) => {
          this.followerList = response.data.object.body.object;
          this.isFollow = !this.isFollow;
          let body = {
            // uid: this.uid,
            uid: jwt_decode(localStorage.getItem('jwt')).uid,
            message: 'FOLLOW',
          };

          notifyAction(
            body,
            (success) => {
              if (success.data.status) {
                console.log('알림 ok');
              } else {
                console.log('알림을 할 수 없습니다.');
              }
            },
            (error) => {
              console.log(error);
              alert('서버에러');
            }
          );
        },
        (error) => {
          console.log(error);
        }
      );
    },
  },
  created() {
    const config = this.setToken();
    const uid = this.$route.params.uid;
    this.uid = uid;
    // console.log(uid);
    // => isMine = true/false로 판단해서 버튼 가리기 트루면 본인이니까 axios 안하고
    // console.log(config);
    // props로 mypage받은 user정보를 이용해서(token말고 uid나 email 이런걸로) axios 요청. 본인의 팔로워 팔로잉 받아오는거.
    findFollowing(
      this.uid,
      (response) => {
        this.followingList = response.data.object;
      },
      (error) => {
        console.log(error);
        console.log('findfollowing');
      }
    );

    findFollower(
      this.uid,
      (response) => {
        this.followerList = response.data.object;
        console.log(this.followerList);
      },
      (error) => {
        console.log(error);
      }
    );

    isFollow(
      this.uid,
      config,
      (response) => {
        console.log('isfollow?');
        // console.log(response);
        this.isFollow = response.data.status;
        console.log(this.isFollow);
      },
      (error) => {
        console.log(error);
      }
    );
  },
};
</script>

<style scoped>
/* .userinfo {
  display: flex;
  justify-content: space-around;
  padding-top: 25px;
  padding-bottom: 25px;
} */

.map-follow-button {
  text-align: right;
  clear: both;
}

.mapbutton {
  border-radius: 10px;
  font-weight: bold;
}

.word-spacing {
  display: flex;
  justify-content: space-evenly;
  padding-top: 10px;
  padding-bottom: 25px;
}
</style>
