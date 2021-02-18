<template>
  <div>
    <v-card v-if="histories.length > 0" width="800" class="mx-auto">
      <v-list>
        <v-list-group
          sub-group
          :value="true"
          v-for="(historiesOfOneDay, i) in histories"
          :key="i"
        >
          <template v-slot:activator>
            <v-list-item-title>{{
              historiesOfOneDay[0].regiTime.substring(0, 10)
            }}</v-list-item-title>
          </template>

          <!-- aaa -->
          <template>
            <v-list-item v-for="(history, j) in historiesOfOneDay" :key="i + j">
              <v-list-item-avatar>
                <v-img
                  :src="`https://i4b107.p.ssafy.io/images/profileImages/${historiesOfOneDay[j].userFrom.profileImagePath}`"
                ></v-img>
                <!-- {{ item.userDto.username }} -->
              </v-list-item-avatar>
              <v-list-item-content v-if="history.state === 'FOLLOW'">
                <div>
                  <button
                    style="color: #ff1f96"
                    @click="goToUserPage(history.userFrom.uid)"
                  >
                    {{ history.userFrom.username }}
                  </button>
                  님이 당신을 팔로우합니다.
                </div>
              </v-list-item-content>
              <v-list-item-content v-if="history.state === 'LIKE'">
                <div>
                  <button
                    style="color: #ff1f96"
                    @click="goToUserPage(history.userFrom.uid)"
                  >
                    {{ history.userFrom.username }}
                  </button>
                  님이
                  <button
                    style="color: #ff1f96"
                    @click="goToArticleDetail(history.articleDto.articleNo)"
                  >
                    {{ history.articleDto.articleNo }}번 게시글</button
                  >을 좋아합니다.
                </div>
              </v-list-item-content>
              <v-list-item-content v-if="history.state === 'COMMENT'">
                <div>
                  <button
                    style="color: #ff1f96"
                    @click="goToUserPage(history.userFrom.uid)"
                  >
                    {{ history.userFrom.username }}
                  </button>
                  님이
                  <button
                    style="color: #ff1f96"
                    @click="goToArticleDetail(history.articleDto.articleNo)"
                  >
                    {{ history.articleDto.articleNo }}번 게시글</button
                  >에 댓글을 달았습니다.
                </div>
              </v-list-item-content>
            </v-list-item>
          </template>
        </v-list-group>
      </v-list>
    </v-card>
    <Navigation />
  </div>
</template>

<script>
import jwt_decode from 'jwt-decode';
import { getHistory } from '@/api/fcm.js';
import constants from '@/lib/constants.js';
import Navigation from '@/components/Navigation.vue';

export default {
  name: 'History',
  components: {
    Navigation,
  },
  props: [],
  computed: {},
  watch: {},
  created() {
    const token = localStorage.getItem('jwt');
    if (token !== null && token !== undefined) {
      this.uid = jwt_decode(token).uid;
    }
    if (this.uid > 0) {
      getHistory(
        (success) => {
          if (success.data.status) {
            let tempHistories = success.data.object;
            this.histories = [];
            console.log(tempHistories, '히스토리즈');
            if (tempHistories.length > 0) {
              let tempArr = new Array();
              tempArr.push(tempHistories[0]);
              for (let i = 1; i < tempHistories.length; ++i) {
                if (
                  tempHistories[i].regiTime.substring(0, 10) !==
                  tempArr[0].regiTime.substring(0, 10)
                ) {
                  this.histories.push(tempArr);
                  tempArr = new Array();
                  tempArr.push(tempHistories[i]);
                } else {
                  tempArr.push(tempHistories[i]);
                }
              }
              if (tempArr.length > 0) {
                this.histories.push(tempArr);
              }
            }
          } else {
            console.log('history 리스트를 받아올 수 없습니다.');
          }
        },
        (error) => {
          console.log(error);
          alert('서버 에러');
        }
      );
    }
  },
  methods: {
    goToUserPage(uid) {
      // alert(uid);
      this.$router.push({
        name: constants.URL_TYPE.USER.MYPAGE,
        params: { uid: uid },
      });
    },
    goToArticleDetail(articleNo) {
      this.$router.push({
        name: constants.URL_TYPE.ARTICLE.ARTICLEDETAIL,
        params: { articleNo: articleNo },
      });
    },
  },
  data: function () {
    return {
      uid: 0,
      histories: [
        // [
        //   {
        //     userFrom: { username: 'aa' },
        //     articleDto: {},
        //     state: 'FOLLOW',
        //     regiTime: '2021-04-05-11',
        //   },
        //   {
        //     userFrom: {},
        //     articleDto: {},
        //     state: 'FOLLOW',
        //     regiTime: '2021-04-05-11',
        //   },
        // ],
        // // [{ userFrom: {}, state: 'FOLLOW', regiTime: '2021-04-05-11' }],
        // [
        //   {
        //     userFrom: {},
        //     articleDto: {},
        //     state: 'LIKE',
        //     regiTime: '2021-04-05-12',
        //   },
        // ],
        // [
        //   {
        //     userFrom: {},
        //     articleDto: {},
        //     state: 'COMMENT',
        //     regiTime: '2021-04-05-13',
        //   },
        // ],
      ],
    };
  },
};
</script>
