<template>
  <div>
    <v-row justify="end">
      <v-btn class="ma-2" fab small light @click="switchDrawer = !switchDrawer" style="position: fixed; top: 50px; right: 5px; z-index: 2">
        <v-icon dark> mdi-pound </v-icon>
      </v-btn>
    </v-row>

    <v-row justify="end" v-if="isSameUser">
      <v-btn class="ma-2" fab small light @click="followDrawer = !followDrawer" style="position: fixed; top: 50px; right: 50px; z-index: 2">
        <v-icon dark> mdi-account-heart-outline</v-icon>
      </v-btn>
    </v-row>
    <v-row justify="center">
      <v-autocomplete
        v-model="selectedArticleTitles"
        :items="articleTitles"
        multiple
        dense
        solo
        background-color="white"
        :search-input.sync="searchTitle"
        @change="clickSearchTitleBar"
        label="Title을 검색해주세요"
        style="position: fixed; top: 10px; z-index: 2"
      ></v-autocomplete>
    </v-row>

    <v-navigation-drawer v-model="switchDrawer" absolute right temporary>
      <v-list-item>
        <h3>해시태그</h3>
      </v-list-item>
      <v-list-item>
        <v-autocomplete
          v-model="selectedHashtagNames"
          :items="userHashtagNames"
          chips
          small-chips
          multiple
          dense
          filled
          :search-input.sync="searchHashtag"
          @change="searchHashtag = ''"
          label="Filled"
        ></v-autocomplete>
      </v-list-item>
      <v-list-item>
        <v-switch @click="clickShowAllHashtagSwitch(selectAllHashtagSwitch)" v-model="selectAllHashtagSwitch" label="전체보기"></v-switch>
      </v-list-item>
      <v-list-item v-if="isSameUser">
        <v-switch @click="clickShowPrivateArticles(isShowPrivate)" v-model="isShowPrivate" label="Private"></v-switch>
      </v-list-item>
      <div v-if="selectedHashtagNames.length == 0">
        <v-list-item v-for="(hashtag, i) in userHashtags" :key="i" link>
          <!-- <v-list-item-title v-text="hashtag"></v-list-item-title> -->
          <v-switch
            @click="clickHashtagSwitch(userHashtagSwitches[userHashtagMap.get(hashtag.hashtagName)])"
            v-model="userHashtagSwitches[userHashtagMap.get(hashtag.hashtagName)]"
            :label="hashtag.hashtagName"
          ></v-switch>
        </v-list-item>
      </div>
      <div v-if="selectedHashtagNames.length > 0">
        <v-list-item v-for="(hashtagName, i) in selectedHashtagNames" :key="i" link>
          <!-- <v-list-item-title v-text="hashtag"></v-list-item-title> -->
          <v-switch @click="clickHashtagSwitch(userHashtagSwitches[userHashtagMap.get(hashtagName)])" v-model="userHashtagSwitches[userHashtagMap.get(hashtagName)]" :label="hashtagName"></v-switch>
        </v-list-item>
      </div>
    </v-navigation-drawer>

    <v-navigation-drawer v-model="followDrawer" absolute right temporary>
      <v-list-item>
        <h3>팔로잉</h3>
      </v-list-item>
      <v-list-item>
        <v-autocomplete
          v-model="selectedFollowUserNames"
          :items="followUserNames"
          chips
          small-chips
          multiple
          dense
          filled
          :search-input.sync="searchFollowUser"
          @change="searchFollowUser = ''"
          label="Filled"
        ></v-autocomplete>
      </v-list-item>

      <div v-if="selectedFollowUserNames.length == 0">
        <v-list-item v-for="(followUser, i) in followUsers" :key="i" link>
          <!-- <v-list-item-title v-text="hashtag"></v-list-item-title> -->
          <v-switch @click="clickFollowUserSwitch(followUserMap.get(followUser.username))" v-model="followUserSwitches[followUserMap.get(followUser.username)]" :label="followUser.username"></v-switch>
        </v-list-item>
      </div>
      <div v-if="selectedFollowUserNames.length > 0">
        <v-list-item v-for="(followUserName, i) in selectedFollowUserNames" :key="i" link>
          <!-- <v-list-item-title v-text="hashtag"></v-list-item-title> -->
          <v-switch @click="clickFollowUserSwitch(followUserMap.get(followUserName))" v-model="followUserSwitches[followUserMap.get(followUserName)]" :label="followUserName"></v-switch>
        </v-list-item>
      </div>
    </v-navigation-drawer>

    <div id="map" style="width: 100vw; height: 100vh; z-index: 1"></div>
    <!-- <div style="position: fixed; bottom: 0; z-index: 2"> -->
    <v-expand-x-transition>
      <v-row justify="center" v-if="expand">
        <v-sheet class="mx-auto" elevation="8" max-width="100vw" style="position: fixed; bottom: 40px; z-index: 2">
          <v-slide-group v-model="model" class="pa-4" show-arrows>
            <v-slide-item v-for="(article, i) in recentArticles" :key="i">
              <v-card class="ma-4" height="100px" width="70px" @click="goToArticleDetail(article)">
                <v-img
                  height="70px"
                  :src="article.imagePaths.length === 0 ? 'https://cdn.vuetifyjs.com/images/cards/sunshine.jpg' : 'https://i4b107.p.ssafy.io/images/uploads/' + article.imagePaths[0]"
                ></v-img>
                <v-card-title class="txt_line" style="padding: 0">{{ article.title }}</v-card-title>
              </v-card>
            </v-slide-item>
          </v-slide-group>
        </v-sheet>
      </v-row>
    </v-expand-x-transition>
    <!-- </div> -->
    <v-btn class="ma-2" light fab small @click="expand = !expand" style="position: fixed; bottom: 200px; z-index: 2">
      <v-icon v-if="!expand">mdi-chevron-right</v-icon>
      <v-icon v-if="expand">mdi-chevron-left</v-icon>
    </v-btn>
    <v-row justify="end">
      <!-- <v-btn
        class="ma-2"
        fab
        small
        dark
        @click="goToCreateArticle"
        v-if="isSameUser"
        style="position: fixed; bottom: 200px; right: 5px; z-index: 2"
      >
        <v-icon dark> mdi-plus </v-icon>
      </v-btn> -->
      <!-- <v-btn class="ma-2" @click="moveCreateArticle" style="position: fixed; bottom: 160px; right:5px; z-index: 2;" icon> -->
      <!-- <v-icon @click="moveCreateArticle" style="position: fixed; bottom: 160px; right:5px; z-index: 2;" link>mdi-plus-circle</v-icon> -->
      <!-- </v-btn> -->
      <KakaoSharing :articles="articles" class="ma-2" fab small dark style="position: fixed; bottom: 200px; right: 50px; z-index: 2" />
    </v-row>

    <Navigation />
  </div>
</template>

<script>
// import constants from '../../lib/constants';
// import { login } from '@/api/user.js';
import { getArticles, getRecentArticles, getUserHashtags, getPublicArticles, getRecentPublicArticles } from '@/api/user.js';
import { getFollowingUsers } from '@/api/tempFollow.js';
import constants from '@/lib/constants';
import jwt_decode from 'jwt-decode';
import Navigation from '@/components/Navigation.vue';
import KakaoSharing from '@/components/sns/KakaoSharing.vue';
// import Vue from 'vue';

const KAKAOMAP_KEY = process.env.VUE_APP_KAKAOMAP_KEY;
// const fullStarHtml = '<button type="button" tabindex="-1" aria-label="Rating 1 of 5" class="v-icon notranslate v-icon--link mdi mdi-star theme--light orange--text" style="font-size: 20px"></button>';
// const halfStarHtml =
//   '<button type="button" tabindex="-1" aria-label="Rating 4 of 5" class="v-icon notranslate v-icon--link mdi mdi-star-half-full theme--light orange--text" style="font-size: 20px"></button>';
// const emptyStarHtml =
//   '<button type="button" tabindex="-1" aria-label="Rating 5 of 5" class="v-icon notranslate v-icon--link mdi mdi-star-outline theme--light orange--text " style="font-size: 20px"></button>';
const PUBLIC_IMAGE_SRC = 'https://user-images.githubusercontent.com/20719987/107175839-2e608d80-6a11-11eb-9bb4-e60529268553.png';
const PRIVATE_IMAGE_SRC = 'https://user-images.githubusercontent.com/20719987/107175853-37e9f580-6a11-11eb-984f-f392d643b4db.png';
const FOLLOW_IMAGE_SRC = 'https://user-images.githubusercontent.com/20719987/107175869-40dac700-6a11-11eb-840b-e7bd0be3f0b8.png';
// const FAVORITE_IMAGE_SRC =
//   'https://user-images.githubusercontent.com/20719987/107175880-46d0a800-6a11-11eb-9c74-e61c393ba2f6.png';
//   const PUBLIC_IMAGE_SRC =
//   'https://i4b107.p.ssafy.io/images/markers/publicMarker.jpg';
// const PRIVATE_IMAGE_SRC =
//   'https://i4b107.p.ssafy.io/images/markers/privateMarker.jpg';
// const FOLLOW_IMAGE_SRC =
//   'https://i4b107.p.ssafy.io/images/markers/followMarker.jpg';
// const FAVORITE_IMAGE_SRC =
//   'https://i4b107.p.ssafy.io/images/markers/favoriteMarker.jpg';
const MARKER_WIDTH = 35;
const MARKER_HEIGHT = 35;

export default {
  components: {
    // NaverLogin
    Navigation,
    KakaoSharing,
  },
  created() {
    this.initPage();
  },
  mounted() {},
  watch: {
    '$route.params.uid': function(uid) {
      console.log(uid);
      this.initPage();
    },
  },
  methods: {
    clickShowPrivateArticles(isShow) {
      if (!isShow) {
        for (let i = 0; i < this.privateMarkers.length; ++i) {
          this.clusterer.removeMarker(this.privateMarkers[i]);
        }
      } else {
        for (let i = 0; i < this.privateMarkers.length; ++i) {
          this.clusterer.addMarker(this.privateMarkers[i]);
        }
      }
    },
    initPage() {
      this.initDatas();
      let uid = this.$route.params.uid;
      const token = localStorage.getItem('jwt');
      let isToken = token !== undefined && token !== null;
      if (isToken && Number(jwt_decode(token).uid) === Number(this.$route.params.uid)) {
        this.isSameUser = true;
      }
      if (this.isSameUser) {
        getArticles(
          uid,
          (response) => {
            if (response.data.status) {
              this.articles = response.data.object;
              for (let i = 0; i < this.articles.length; ++i) {
                this.articleTitles.push(this.articles[i].title);
              }
              window.kakao && window.kakao.maps ? this.initMap() : this.addScript();
              // alert('article list를 받았습니다.');
            } else {
              alert('article list 실패');
            }
          },
          (error) => {
            console.log(error);
            alert('article list 받기에 실패했습니다.');
          }
        );

        getRecentArticles(
          uid,
          (response) => {
            if (response.data.status) {
              this.recentArticles = response.data.object;
              // alert('recent list를 받았습니다.');
            } else {
              alert('recent list 실패');
            }
          },
          (error) => {
            console.log(error);
            alert('recent list 받기에 실패했습니다.');
          }
        );
      } else {
        getPublicArticles(
          uid,
          (response) => {
            if (response.data.status) {
              this.articles = response.data.object;
              for (let i = 0; i < this.articles.length; ++i) {
                this.articleTitles.push(this.articles[i].title);
              }
              window.kakao && window.kakao.maps ? this.initMap() : this.addScript();
              // alert('article list를 받았습니다.');
            } else {
              alert('article list 실패');
            }
          },
          (error) => {
            console.log(error);
            alert('article list 받기에 실패했습니다.');
          }
        );

        getRecentPublicArticles(
          uid,
          (response) => {
            if (response.data.status) {
              this.recentArticles = response.data.object;
              // alert('recent list를 받았습니다.');
            } else {
              alert('recent list 실패');
            }
          },
          (error) => {
            console.log(error);
            alert('recent list 받기에 실패했습니다.');
          }
        );
      }

      getUserHashtags(
        uid,
        (response) => {
          if (response.data.status) {
            this.userHashtags = response.data.object;
            // alert('hashtag list를 받았습니다.');

            // alert(this.userHashtags.length);
            for (let i = 0; i < this.userHashtags.length; ++i) {
              // alert(this.userHashtags[i].hashtagName);
              this.userHashtagMap.set(this.userHashtags[i].hashtagName, i);
              this.userHashtagNames.push(this.userHashtags[i].hashtagName);
              this.userHashtagSwitches.push(false);
            }
          } else {
            alert('hashtag list 실패');
          }
        },
        (error) => {
          console.log(error);
          alert('hashtag list 받기에 실패했습니다.');
        }
      );

      // TODO : follow하는 유저들의 정보 받아오기
      getFollowingUsers(
        uid,
        (response) => {
          // console.log(response);
          if (response.data.status) {
            this.followUsers = response.data.object;
            for (let i = 0; i < this.followUsers.length; ++i) {
              this.followUserMap.set(this.followUsers[i].username, i);
              this.followUserNames.push(this.followUsers[i].username);
              this.followUserSwitches.push(false);
            }
          } else {
            console.log('팔로우하는 유저 리스트를 받아올 수 없습니다.');
          }
        },
        (error) => {
          console.log(error);
          alert('팔로우하는 유저 리스트를 받아올 수 없습니다.');
        }
      );
    },
    initDatas() {
      this.privateMarkers = [];
      this.isShowFavorites = true;
      this.isShowPrivate = true;
      this.isSameUser = false;
      this.map = {};
      this.followUserMap = new Map();
      this.followUserSwitches = [];
      this.selectedFollowUserNames = [];
      this.followUserNames = [];
      this.searchFollowUser = '';
      this.followUsers = [];
      this.clusterer = {};
      this.kakaoMarkers = [];
      this.articleTitles = [];
      this.selectedHashtagNames = [];
      this.selectedArticleTitles = [];
      this.searchHashtag = '';
      this.searchTitle = '';
      this.followDrawer = false;
      this.switchDrawer = false;
      this.expand = false;
      this.recentArticles = [];
      this.model = null;
      this.selectAllHashtagSwitch = true;
      this.drawer = false;
      this.userHashtags = [];
      this.userHashtagNames = [];
      this.userHashtagMap = new Map();
      this.userHashtagSwitches = [];
      this.articles = [];
    },
    clickFollowUserSwitch(idx) {
      let _this = this;
      if (!this.followUserSwitches[idx]) {
        this.clusterer.clear();
        this.clusterer.addMarkers(this.kakaoMarkers);
        return;
      }

      for (let i = 0; i < this.followUserSwitches.length; ++i) {
        if (i === idx) {
          continue;
        }
        this.followUserSwitches[i] = false;
      }
      for (let i = 0; i < this.userHashtagSwitches.length; ++i) {
        this.userHashtagSwitches[i] = false;
      }
      _this.selectAllHashtagSwitch = false;
      // TODO : idx에 해당하는 articles 얻어오기
      let followUserArticles = [];
      getArticles(
        _this.followUsers[idx].uid,
        (response) => {
          if (response.data.status) {
            followUserArticles = response.data.object;
            // 팔로우한 사람 게시글 받아와서 지도에 표시

            let followImageSize = new kakao.maps.Size(MARKER_WIDTH, MARKER_HEIGHT);
            let followMarkerImage = new kakao.maps.MarkerImage(FOLLOW_IMAGE_SRC, followImageSize);

            let followMarkers = [];
            for (let i = 0; i < followUserArticles.length; ++i) {
              let followMarker = new kakao.maps.Marker({
                position: new window.kakao.maps.LatLng(followUserArticles[i].positionLat, followUserArticles[i].positionLng),
                image: followMarkerImage,
              });

              let overlay = new kakao.maps.CustomOverlay({
                map: _this.map,
                position: followMarker.getPosition(),
              });
              let wrapDiv = _this.makeCustomizedOverlay(overlay, followUserArticles[i]);

              overlay.setContent(wrapDiv);

              kakao.maps.event.addListener(followMarker, 'click', function() {
                overlay.setMap(_this.map);
              });
              overlay.setMap(null);
              followMarkers.push(followMarker);
            }
            this.clusterer.clear();
            this.clusterer.addMarkers(this.kakaoMarkers);
            this.clusterer.addMarkers(followMarkers);
          } else {
            console.log('해당 유저의 게시물들을 받아올 수 없습니다.');
          }
        },
        (error) => {
          console.log(error);
          alert('해당 유저의 게시물들을 받아올 수 없습니다.');
        }
      );
    },
    clickSearchTitleBar() {
      this.searchTitle = '';
      this.clusterer.clear();
      let markers = [];
      for (let i = 0; i < this.articles.length; ++i) {
        if (this.selectedArticleTitles.includes(this.articles[i].title)) {
          markers.push(this.kakaoMarkers[i]);
        }
      }
      this.clusterer.addMarkers(markers);
    },
    // goToCreateArticle() {
    //   this.$router.push({ name: constants.URL_TYPE.ARTICLE.CREATEARTICLE });
    // },
    goToArticleDetail(article) {
      this.$router.push({
        name: constants.URL_TYPE.ARTICLE.ARTICLEDETAIL,
        params: { articleNo: article.articleNo, article: article },
      });
    },
    goToMyPage() {
      alert('마이페이지로 이동');
    },
    logout() {
      alert('로그아웃');
    },
    clickHashtagSwitch(isOn) {
      if (isOn) {
        this.selectAllHashtagSwitch = false;
        for (let i = 0; i < this.followUserSwitches.length; ++i) {
          this.followUserSwitches[i] = false;
        }
      }
      let markers = [];

      let switchOnCnt = 0;
      for (let i = 0; i < this.userHashtagSwitches.length; ++i) {
        if (this.userHashtagSwitches[i]) {
          switchOnCnt += 1;
        }
      }
      for (let i = 0; i < this.articles.length; ++i) {
        let cnt = 0;
        for (let j = 0; j < this.articles[i].hashtags.length; ++j) {
          // alert(this.articles[i].hashtags[j].hashtagName);
          // alert(this.userHashtagMap.get(this.articles[i].hashtags[j].hashtagName));
          if (this.userHashtagSwitches[this.userHashtagMap.get(this.articles[i].hashtags[j].hashtagName)]) {
            cnt += 1;
          }
        }
        // alert(cnt);
        if (cnt != 0 && cnt == switchOnCnt) {
          markers.push(this.kakaoMarkers[i]);
        }
      }
      // this.showMap(showDatas);
      // this.initMap();
      this.clusterer.clear();
      this.clusterer.addMarkers(markers);
    },
    clickShowAllHashtagSwitch(isOn) {
      this.clusterer.clear();
      if (isOn) {
        for (let i = 0; i < this.userHashtagSwitches.length; ++i) {
          this.userHashtagSwitches[i] = false;
        }
        for (let i = 0; i < this.followUserSwitches.length; ++i) {
          this.followUserSwitches[i] = false;
        }
        this.clusterer.addMarkers(this.kakaoMarkers);
      }
    },

    initMap() {
      let _this = this;
      let element = document.getElementById('map');
      while (element.firstChild) {
        element.removeChild(element.firstChild);
      }

      _this.map = new kakao.maps.Map(document.getElementById('map'), {
        // 지도를 표시할 div
        center: new kakao.maps.LatLng(36.2683, 127.6358), // 지도의 중심좌표
        level: 13, // 지도의 확대 레벨
      });

      // 마커 클러스터러를 생성합니다
      _this.clusterer = new kakao.maps.MarkerClusterer({
        map: _this.map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
        averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
        minLevel: 10, // 클러스터 할 최소 지도 레벨
      });

      for (let i = 0; i < _this.articles.length; ++i) {
        let data = _this.articles[i];
        let imageSize = new kakao.maps.Size(MARKER_WIDTH, MARKER_HEIGHT);
        let markerImage = new kakao.maps.MarkerImage(PUBLIC_IMAGE_SRC, imageSize);
        if (data.isPrivate) {
          markerImage = new kakao.maps.MarkerImage(PRIVATE_IMAGE_SRC, imageSize);
        }
        let nowMarker = new kakao.maps.Marker({
          position: new window.kakao.maps.LatLng(data.positionLat, data.positionLng),
          image: markerImage,
        });

        _this.kakaoMarkers.push(nowMarker);
        if (data.isPrivate) {
          _this.privateMarkers.push(nowMarker);
        }

        let overlay = new kakao.maps.CustomOverlay({
          map: _this.map,
          position: nowMarker.getPosition(),
        });

        let wrapDiv = _this.makeCustomizedOverlay(overlay, data);

        overlay.setContent(wrapDiv);

        kakao.maps.event.addListener(nowMarker, 'click', function() {
          overlay.setMap(_this.map);
        });

        overlay.setMap(null);
      }

      // console.log(markers);
      // 클러스터러에 마커들을 추가합니다
      _this.clusterer.addMarkers(_this.kakaoMarkers);
      // _this.clusterer.clear();
      //   });
    },
    makeCustomizedOverlay(overlay, data) {
      let _this = this;
      let wrapDiv = document.createElement('div');
      wrapDiv.className = 'wrap';
      let infoDiv = document.createElement('div');
      infoDiv.className = 'infos';
      let titleDiv = document.createElement('div');
      titleDiv.className = 'title';
      titleDiv.textContent = data.title;
      let closeDiv = document.createElement('div');
      closeDiv.className = 'close';
      closeDiv.title = '닫기';
      closeDiv.onclick = function() {
        // alert('a');
        overlay.setMap(null);
      };
      titleDiv.appendChild(closeDiv);

      infoDiv.appendChild(titleDiv);
      let bodyDiv = document.createElement('div');
      bodyDiv.className = 'body';
      let imgDiv = document.createElement('div');
      imgDiv.className = 'img';
      let imgSrc = document.createElement('img');
      imgSrc.src = 'https://cdn.vuetifyjs.com/images/cards/cooking.png';
      imgSrc.style.width = '73px';
      imgSrc.style.height = '70px';
      imgDiv.appendChild(imgSrc);
      let descDiv = document.createElement('div');
      descDiv.className = 'desc';
      let ellipsisDiv = document.createElement('div');
      ellipsisDiv.className = 'ellipsis';
      ellipsisDiv.textContent = data.address;
      descDiv.appendChild(ellipsisDiv);
      let ratingDiv = document.createElement('div');
      let rating = '';
      let starCnt = 0;
      let evaluation = data.evaluation * 2;

      for (starCnt; starCnt < Math.floor(evaluation / 2); ++starCnt) {
        rating = document.createElement('icon');
        rating.style.fontSize = '20px';
        rating.className = 'mdi mdi-star theme--light orange--text';
        ratingDiv.appendChild(rating);
      }
      if (evaluation % 2 == 1) {
        rating = document.createElement('icon');
        rating.style.fontSize = '20px';
        rating.className = 'mdi mdi-star-half-full theme--light orange--text';
        ratingDiv.appendChild(rating);
        starCnt += 1;
      }
      for (starCnt; starCnt < 5; ++starCnt) {
        rating = document.createElement('icon');
        rating.style.fontSize = '20px';
        rating.className = 'mdi mdi-star-outline theme--light orange--text';
        ratingDiv.appendChild(rating);
      }
      descDiv.appendChild(ratingDiv);
      let aTag = document.createElement('button');
      aTag.textContent = '게시물 보기';
      aTag.onclick = function() {
        _this.$router.push({
          name: constants.URL_TYPE.ARTICLE.ARTICLEDETAIL,
          params: { articleNo: data.articleNo, article: data },
        });
      };
      descDiv.appendChild(aTag);
      bodyDiv.appendChild(imgDiv);
      bodyDiv.appendChild(descDiv);
      infoDiv.appendChild(bodyDiv);
      wrapDiv.appendChild(infoDiv);
      return wrapDiv;
      // console.log(evaluation);
    },

    addScript() {
      const script = document.createElement('script');
      /* global kakao */
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${KAKAOMAP_KEY}&libraries=services,clusterer`;
      let _this = this;
      script.onload = () =>
        kakao.maps.load(function() {
          _this.initMap();
        });
      //  kakao.maps.load(this.showMap); 과 비교
      document.head.appendChild(script);
    },
  },
  data: () => {
    return {
      privateMarkers: [],
      isShowFavorites: true,
      isShowPrivate: true,
      isSameUser: false,
      map: {},
      followUserMap: new Map(),
      followUserSwitches: [],
      selectedFollowUserNames: [],
      followUserNames: [],
      searchFollowUser: '',
      followUsers: [],
      clusterer: {},
      kakaoMarkers: [],
      articleTitles: [],
      selectedHashtagNames: [],
      selectedArticleTitles: [],
      searchHashtag: '',
      searchTitle: '',
      followDrawer: false,
      switchDrawer: false,
      expand: false,
      recentArticles: [],
      model: null,
      selectAllHashtagSwitch: true,
      drawer: false,
      userHashtags: [],
      userHashtagNames: [],
      userHashtagMap: new Map(),
      userHashtagSwitches: [],
      articles: [],
    };
  },
};
</script>
<style>
.txt_line {
  width: 70px;
  /* padding: 0 5px; */
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.wrap {
  position: absolute;
  left: 0;
  bottom: 40px;
  width: 288px;
  height: 132px;
  margin-left: -144px;
  text-align: left;
  overflow: hidden;
  font-size: 12px;
  font-family: 'Malgun Gothic', dotum, '돋움', sans-serif;
  line-height: 1.5;
}
.wrap * {
  padding: 0;
  margin: 0;
}
.wrap .infos {
  width: 286px;
  height: 120px;
  border-radius: 5px;
  border-bottom: 2px solid #ccc;
  border-right: 1px solid #ccc;
  overflow: hidden;
  background: #fff;
}
.wrap .infos:nth-child(1) {
  border: 0;
  box-shadow: 0px 1px 2px #888;
}
.infos .title {
  padding: 5px 0 0 10px;
  height: 30px;
  background: #eee;
  border-bottom: 1px solid #ddd;
  font-size: 18px;
  font-weight: bold;
}
.infos .close {
  position: absolute;
  top: 10px;
  right: 10px;
  color: #888;
  width: 17px;
  height: 17px;
  background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/overlay_close.png');
}
.infos .close:hover {
  cursor: pointer;
}
.infos .body {
  position: relative;
  overflow: hidden;
}
.infos .desc {
  position: relative;
  margin: 13px 0 0 90px;
  height: 75px;
}
.desc .ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.desc .jibun {
  font-size: 11px;
  color: #888;
  margin-top: -2px;
}
.infos .img {
  position: absolute;
  top: 6px;
  left: 5px;
  width: 73px;
  height: 71px;
  border: 1px solid #ddd;
  color: #888;
  overflow: hidden;
}
.infos:after {
  content: '';
  position: absolute;
  margin-left: -12px;
  left: 50%;
  bottom: 0;
  width: 22px;
  height: 12px;
  background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png');
}
.infos .link {
  color: #5085bb;
}
</style>
