<template>
  <div>
    <v-row justify="end">
      <v-btn
        class="ma-2"
        fab
        x-small
        light
        @click="hashtagDrawer = !hashtagDrawer"
        style="position: fixed; top: 60px; z-index: 2"
      >
        <v-icon dark> mdi-pound </v-icon>
      </v-btn>
    </v-row>

    <v-row justify="end" v-if="isSameUser">
      <v-btn
        class="ma-2"
        fab
        x-small
        light
        @click="followDrawer = !followDrawer"
        style="position: fixed; top: 100px; z-index: 2"
      >
        <v-icon dark> mdi-account-heart-outline</v-icon>
      </v-btn>
    </v-row>

    <v-row justify="end" v-if="isSameUser && !isShowFavorites">
      <v-btn
        class="ma-2"
        fab
        x-small
        light
        @click="clickShowFavoriteSwitch"
        style="position: fixed; top: 140px; z-index: 2"
      >
        <v-icon> mdi-heart-outline</v-icon>
      </v-btn>
    </v-row>

    <v-row justify="end" v-if="isSameUser && isShowFavorites">
      <v-btn
        class="ma-2"
        fab
        x-small
        light
        @click="clickShowFavoriteSwitch"
        style="position: fixed; top: 140px; z-index: 2"
      >
        <v-icon> mdi-heart</v-icon>
      </v-btn>
    </v-row>

    <v-row justify="end">
      <KakaoSharing
        :filteredHashtagSwitches="userHashtagSwitches"
        :isShowFavorites="isShowFavorites"
        :articles="articles"
        fab
        x-small
        dark
        style="position: fixed; top: 20px; z-index: 2"
      />
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
        label="제목을 검색해주세요"
        style="position: fixed; top: 10px; z-index: 2"
      >
        <template slot="no-data">
          <div class="center ml-2" style="font-size: 0.8em">
            검색 후보가 존재하지 않아요 :(
          </div>
        </template>
      </v-autocomplete>
    </v-row>

    <v-navigation-drawer v-model="hashtagDrawer" absolute right temporary>
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
          label="검색어를 입력해주세요"
        >
          <template slot="no-data">
            <div class="center ml-2" style="font-size: 0.8em">
              검색 후보가 존재하지 않아요 :(
            </div>
          </template></v-autocomplete
        >
      </v-list-item>
      <v-list-item>
        <v-switch
          @click="clickShowAllHashtagSwitch(selectAllHashtagSwitch)"
          v-model="selectAllHashtagSwitch"
          label="전체보기"
        ></v-switch>
      </v-list-item>

      <div v-if="selectedHashtagNames.length == 0">
        <v-list-item v-for="(hashtag, i) in userHashtags" :key="i" link>
          <!-- <v-list-item-title v-text="hashtag"></v-list-item-title> -->
          <v-switch
            @click="
              clickHashtagSwitch(
                userHashtagSwitches[userHashtagMap.get(hashtag.hashtagName)]
              )
            "
            v-model="
              userHashtagSwitches[userHashtagMap.get(hashtag.hashtagName)]
            "
            :label="hashtag.hashtagName"
          ></v-switch>
        </v-list-item>
      </div>
      <div v-if="selectedHashtagNames.length > 0">
        <v-list-item
          v-for="(hashtagName, i) in selectedHashtagNames"
          :key="i"
          link
        >
          <!-- <v-list-item-title v-text="hashtag"></v-list-item-title> -->
          <v-switch
            @click="
              clickHashtagSwitch(
                userHashtagSwitches[userHashtagMap.get(hashtagName)]
              )
            "
            v-model="userHashtagSwitches[userHashtagMap.get(hashtagName)]"
            :label="hashtagName"
          ></v-switch>
        </v-list-item>
      </div>
    </v-navigation-drawer>

    <v-navigation-drawer v-model="followDrawer" absolute right temporary>
      <v-list-item>
        <h3>팔로우</h3>
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
          label="검색어를 입력해주세요"
        >
          <template slot="no-data">
            <div class="center ml-2" style="font-size: 0.8em">
              검색 후보가 존재하지 않아요 :(
            </div>
          </template></v-autocomplete
        >
      </v-list-item>

      <div v-if="selectedFollowUserNames.length == 0">
        <v-list-item v-for="(followUser, i) in followUsers" :key="i" link>
          <!-- <v-list-item-title v-text="hashtag"></v-list-item-title> -->
          <v-switch
            @click="
              clickFollowUserSwitch(followUserMap.get(followUser.username))
            "
            v-model="followUserSwitches[followUserMap.get(followUser.username)]"
            :label="followUser.username"
          ></v-switch>
        </v-list-item>
      </div>
      <div v-if="selectedFollowUserNames.length > 0">
        <v-list-item
          v-for="(followUserName, i) in selectedFollowUserNames"
          :key="i"
          link
        >
          <!-- <v-list-item-title v-text="hashtag"></v-list-item-title> -->
          <v-switch
            @click="clickFollowUserSwitch(followUserMap.get(followUserName))"
            v-model="followUserSwitches[followUserMap.get(followUserName)]"
            :label="followUserName"
          ></v-switch>
        </v-list-item>
      </div>
    </v-navigation-drawer>

    <div id="map" style="height: 100vh; width: 810px; z-index: 1"></div>

    <v-btn
      @click="goToFilteredDataList"
      style="position: fixed; bottom: 100px; z-index: 2"
      depressed
      light
      small
      ><v-icon>mdi-view-list</v-icon></v-btn
    >

    <Navigation />
  </div>
</template>

<script>
import {
  getArticles,
  getUserHashtags,
  getUserPublicHashtags,
  getPublicArticles,
  myFavorite,
} from '@/api/user.js';
import { getFollowingUsers } from '@/api/tempFollow.js';
import constants from '@/lib/constants';
import jwt_decode from 'jwt-decode';
import Navigation from '@/components/Navigation.vue';
import KakaoSharing from '@/components/sns/KakaoSharing.vue';

const KAKAOMAP_KEY = process.env.VUE_APP_KAKAOMAP_KEY;

const PUBLIC_IMAGE_SRC =
  'https://i4b107.p.ssafy.io/images/markers/publicMarker.jpg';
const PRIVATE_IMAGE_SRC =
  'https://i4b107.p.ssafy.io/images/markers/privateMarker.jpg';
const FOLLOW_IMAGE_SRC =
  'https://i4b107.p.ssafy.io/images/markers/followMarker.jpg';
const FAVORITE_IMAGE_SRC =
  'https://i4b107.p.ssafy.io/images/markers/favoriteMarker.jpg';
const MARKER_WIDTH = 50;
const MARKER_HEIGHT = 50;

export default {
  components: {
    Navigation,
    KakaoSharing,
  },
  created() {
    this.initPage();
  },
  mounted() {},
  watch: {
    '$route.params.uid': function () {
      this.initPage();
    },
  },
  methods: {
    promiseGetUserHashtags() {
      return new Promise((resolve, reject) => {
        getUserHashtags(
          this.mapUid,
          (response) => {
            if (response.data.status) {
              let fullHashtags = response.data.object;
              for (let i = 0; i < fullHashtags.length; ++i) {
                this.fullHashtagNames.push(fullHashtags[i].hashtagName);
              }
              if (this.isSameUser) {
                this.userHashtags = fullHashtags;
                for (let i = 0; i < this.userHashtags.length; ++i) {
                  this.userHashtagMap.set(this.userHashtags[i].hashtagName, i);
                  this.userHashtagNames.push(this.userHashtags[i].hashtagName);
                  this.userHashtagSwitches.push(false);
                }
              }
              return resolve();
            } else {
              return reject();
            }
          },
          (error) => {
            console.log(error);
            return reject();
          }
        );
      });
    },
    promiseGetUserPublicHashtags() {
      return new Promise((resolve, reject) => {
        getUserPublicHashtags(
          this.mapUid,
          (response) => {
            if (response.data.status) {
              this.userHashtags = response.data.object;

              for (let i = 0; i < this.userHashtags.length; ++i) {
                this.userHashtagMap.set(this.userHashtags[i].hashtagName, i);
                this.userHashtagNames.push(this.userHashtags[i].hashtagName);
                this.userHashtagSwitches.push(false);
              }
              return resolve();
            } else {
              return reject();
            }
          },
          (error) => {
            console.log(error);
            return reject();
          }
        );
      });
    },

    promiseGetArticles() {
      return new Promise((resolve, reject) => {
        getArticles(
          this.mapUid,
          (response) => {
            if (response.data.status) {
              this.articles = response.data.object;
              for (let i = 0; i < this.articles.length; ++i) {
                if (!this.articles[i].private) {
                  this.publicArticles.push(this.articles[i]);
                }
                if (this.isSameUser) {
                  this.articleTitles.push(this.articles[i].title);
                } else if (!this.articles[i].private) {
                  this.articleTitles.push(this.articles[i].title);
                }
              }
              window.kakao && window.kakao.maps
                ? this.initMap()
                : this.addScript();
              resolve();
            } else {
              reject();
            }
          },
          (error) => {
            console.log(error);
            reject();
          }
        );
      });
    },

    promiseGetFollowingUsers() {
      return new Promise((resolve, reject) => {
        getFollowingUsers(
          this.mapUid,
          (response) => {
            if (response.data.status) {
              this.followUsers = response.data.object;
              for (let i = 0; i < this.followUsers.length; ++i) {
                this.followUserMap.set(this.followUsers[i].username, i);
                this.followUserNames.push(this.followUsers[i].username);
                this.followUserSwitches.push(false);
              }
              return resolve();
            } else {
              return reject();
            }
          },
          (error) => {
            console.log(error);
            return reject();
          }
        );
      });
    },

    promiseGetMyFavorites() {
      return new Promise((resolve, reject) => {
        let _this = this;
        myFavorite(
          (response) => {
            if (response.data.status) {
              this.favoriteArticles = response.data.object;
              let favoriteImageSize = new kakao.maps.Size(
                MARKER_WIDTH,
                MARKER_HEIGHT
              );
              let favoriteMarkerImage = new kakao.maps.MarkerImage(
                FAVORITE_IMAGE_SRC,
                favoriteImageSize
              );
              for (let i = 0; i < this.favoriteArticles.length; ++i) {
                let favoriteMarker = new kakao.maps.Marker({
                  position: new window.kakao.maps.LatLng(
                    this.favoriteArticles[i].positionLat,
                    this.favoriteArticles[i].positionLng
                  ),
                  image: favoriteMarkerImage,
                });
                let overlay = new kakao.maps.CustomOverlay({
                  map: this.map,
                  position: favoriteMarker.getPosition(),
                });
                let wrapDiv = this.makeCustomizedOverlay(
                  overlay,
                  this.favoriteArticles[i]
                );

                overlay.setContent(wrapDiv);

                kakao.maps.event.addListener(
                  favoriteMarker,
                  'click',
                  function () {
                    overlay.setMap(_this.map);
                  }
                );
                overlay.setMap(null);
                this.favoriteMarkers.push(favoriteMarker);
              }
              this.clusterer.addMarkers(this.favoriteMarkers);
              return resolve();
            } else {
              return reject();
            }
          },
          (error) => {
            console.log(error);
            return reject();
          }
        );
      });
    },
    goToFilteredDataList() {
      let filteredData = [];
      let filteredArticleNoArr = [];
      let paramArticles = this.articles;
      if (!this.isSameUser) {
        paramArticles = this.publicArticles;
      }
      let currentArticles = this.getCurrentArticles(paramArticles);
      for (let i = 0; i < currentArticles.length; ++i) {
        filteredData.push(currentArticles[i]);
        filteredArticleNoArr.push(currentArticles[i].articleNo);
      }
      if (this.isShowFavorites) {
        for (let i = 0; i < this.favoriteArticles.length; ++i) {
          if (
            filteredArticleNoArr.includes(this.favoriteArticles[i].articleNo)
          ) {
            continue;
          }
          filteredData.push(this.favoriteArticles[i]);
          filteredArticleNoArr.push(this.favoriteArticles[i].articleNo);
        }
      }
      for (let i = 0; i < this.followArticles.length; ++i) {
        if (filteredArticleNoArr.includes(this.followArticles[i].articleNo)) {
          continue;
        }
        filteredData.push(this.followArticles[i]);
      }

      let storedObj = {};

      storedObj.isShowFavorites = this.isShowFavorites;
      let followIdx = -1;
      for (let i = 0; i < this.followUserSwitches.length; ++i) {
        if (this.followUserSwitches[i]) {
          followIdx = i;
          break;
        }
      }
      storedObj.followIdx = followIdx;
      storedObj.isSelectAll = this.selectAllHashtagSwitch;
      storedObj.hashtagSwitches = this.userHashtagSwitches;
      sessionStorage.setItem('storedData', JSON.stringify(storedObj));

      this.$router.push({
        name: constants.URL_TYPE.HOME.FILTEREDLIST,
        params: { filteredData: filteredData },
      });
    },

    initPage() {
      this.initDatas();
      this.mapUid = this.$route.params.uid;
      const token = localStorage.getItem('jwt');
      let isToken = token !== undefined && token !== null;
      if (
        isToken &&
        Number(jwt_decode(token).uid) === Number(this.$route.params.uid)
      ) {
        this.isSameUser = true;
      }

      if (this.isSameUser) {
        this.promiseGetUserHashtags()
          .then(this.promiseGetFollowingUsers())
          .then(this.promiseGetArticles())
          .catch(() => {
            console.log('promise실패');
          });
      } else {
        this.promiseGetUserHashtags()
          .then(this.promiseGetFollowingUsers())
          .then(this.promiseGetUserPublicHashtags())
          .then(this.promiseGetArticles())
          .catch(() => {
            console.log('promise실패');
          });
      }
    },

    initDatas() {
      this.mapUid = '';
      this.fullHashtagNames = [];
      this.followArticles = [];
      this.favoriteArticles = [];
      this.favoriteMarkers = [];
      this.followMarkers = [];
      this.publicArticles = [];
      this.isShowFavorites = true;
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
      this.hashtagDrawer = false;
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
    clickShowFavoriteSwitch() {
      this.isShowFavorites = !this.isShowFavorites;
      if (this.isShowFavorites) {
        this.clusterer.addMarkers(this.favoriteMarkers);
      } else {
        this.clusterer.removeMarkers(this.favoriteMarkers);
      }
    },
    clickFollowUserSwitch(idx) {
      let _this = this;
      if (!this.followUserSwitches[idx]) {
        this.clusterer.removeMarkers(this.followMarkers);
        this.followMarkers = [];
        this.followArticles = [];
        return;
      }

      for (let i = 0; i < this.followUserSwitches.length; ++i) {
        if (i === idx) {
          continue;
        }
        this.followUserSwitches[i] = false;
      }

      getPublicArticles(
        _this.followUsers[idx].uid,
        (response) => {
          if (response.data.status) {
            this.followArticles = response.data.object;
            this.clusterer.removeMarkers(this.followMarkers);
            let followImageSize = new kakao.maps.Size(
              MARKER_WIDTH,
              MARKER_HEIGHT
            );
            let followMarkerImage = new kakao.maps.MarkerImage(
              FOLLOW_IMAGE_SRC,
              followImageSize
            );

            this.followMarkers = [];
            for (let i = 0; i < this.followArticles.length; ++i) {
              let followMarker = new kakao.maps.Marker({
                position: new window.kakao.maps.LatLng(
                  this.followArticles[i].positionLat,
                  this.followArticles[i].positionLng
                ),
                image: followMarkerImage,
              });

              let overlay = new kakao.maps.CustomOverlay({
                map: _this.map,
                position: followMarker.getPosition(),
              });
              let wrapDiv = _this.makeCustomizedOverlay(
                overlay,
                this.followArticles[i]
              );

              overlay.setContent(wrapDiv);

              kakao.maps.event.addListener(followMarker, 'click', function () {
                overlay.setMap(_this.map);
              });
              overlay.setMap(null);
              this.followMarkers.push(followMarker);
            }

            this.clusterer.addMarkers(this.followMarkers);
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
    clickSearchTitleBar() {
      this.searchTitle = '';
      this.clearAll();
      let markers = [];
      for (let i = 0; i < this.articles.length; ++i) {
        if (this.selectedArticleTitles.includes(this.articles[i].title)) {
          markers.push(this.kakaoMarkers[i]);
        }
      }
      this.clusterer.addMarkers(markers);
    },
    goToArticleDetail(article) {
      let storedObj = {};
      let centerPosition = {
        positionLat: article.positionLat,
        positionLng: article.positionLng,
      };
      storedObj.isShowFavorites = this.isShowFavorites;
      storedObj.centerPosition = centerPosition;
      let followIdx = -1;
      for (let i = 0; i < this.followUserSwitches.length; ++i) {
        if (this.followUserSwitches[i]) {
          followIdx = i;
          break;
        }
      }
      storedObj.followIdx = followIdx;
      storedObj.isSelectAll = this.selectAllHashtagSwitch;

      storedObj.hashtagSwitches = this.userHashtagSwitches;
      sessionStorage.setItem('storedData', JSON.stringify(storedObj));

      this.$router.push({
        name: constants.URL_TYPE.ARTICLE.ARTICLEDETAIL,
        params: { articleNo: article.articleNo, article: article },
      });
    },

    clickHashtagSwitch(isOn) {
      if (isOn) {
        this.selectAllHashtagSwitch = false;
      }

      let paramArticles = this.articles;
      if (!this.isSameUser) {
        paramArticles = this.publicArticles;
      }
      let currentMarkers = this.getCurrentMarkers(paramArticles);

      this.clusterer.clear();
      this.clusterer.addMarkers(this.followMarkers);
      if (this.isShowFavorites) {
        this.clusterer.addMarkers(this.favoriteMarkers);
      }
      this.clusterer.addMarkers(currentMarkers);
    },
    clickShowAllHashtagSwitch(isOn) {
      let paramArticles = this.articles;
      if (!this.isSameUser) {
        paramArticles = this.publicArticles;
      }
      let currentMarkers = this.getCurrentMarkers(paramArticles);
      this.clusterer.removeMarkers(currentMarkers);
      if (isOn) {
        for (let i = 0; i < this.userHashtagSwitches.length; ++i) {
          this.userHashtagSwitches[i] = false;
        }
        this.clusterer.addMarkers(this.kakaoMarkers);
      } else {
        this.clusterer.removeMarkers(this.kakaoMarkers);
      }
    },

    getCurrentMarkers(paramArticles) {
      if (this.selectAllHashtagSwitch) {
        return this.kakaoMarkers;
      }
      let retMarkers = [];
      let currentOnHashtagNames = [];
      for (let i = 0; i < this.userHashtagSwitches.length; ++i) {
        if (this.userHashtagSwitches[i]) {
          currentOnHashtagNames.push(this.userHashtagNames[i]);
        }
      }

      if (currentOnHashtagNames.length == 0) {
        return retMarkers;
      }

      for (let i = 0; i < paramArticles.length; ++i) {
        let isCurrentArticle = true;
        let articleHashtagNames = [];
        for (let j = 0; j < paramArticles[i].hashtags.length; ++j) {
          articleHashtagNames.push(paramArticles[i].hashtags[j].hashtagName);
        }
        for (let j = 0; j < currentOnHashtagNames.length; ++j) {
          if (!articleHashtagNames.includes(currentOnHashtagNames[j])) {
            isCurrentArticle = false;
            break;
          }
        }
        if (isCurrentArticle) {
          retMarkers.push(this.kakaoMarkers[i]);
        }
      }

      return retMarkers;
    },

    getCurrentArticles(paramArticles) {
      if (this.selectAllHashtagSwitch) {
        return paramArticles;
      }
      let retArticles = [];
      let currentOnHashtagNames = [];
      for (let i = 0; i < this.userHashtagSwitches.length; ++i) {
        if (this.userHashtagSwitches[i]) {
          currentOnHashtagNames.push(this.userHashtagNames[i]);
        }
      }

      if (currentOnHashtagNames.length == 0) {
        return retArticles;
      }

      for (let i = 0; i < paramArticles.length; ++i) {
        let isCurrentArticle = true;
        let articleHashtagNames = [];
        for (let j = 0; j < paramArticles[i].hashtags.length; ++j) {
          articleHashtagNames.push(paramArticles[i].hashtags[j].hashtagName);
        }
        for (let j = 0; j < currentOnHashtagNames.length; ++j) {
          if (!articleHashtagNames.includes(currentOnHashtagNames[j])) {
            isCurrentArticle = false;
            break;
          }
        }
        if (isCurrentArticle) {
          retArticles.push(paramArticles[i]);
        }
      }

      return retArticles;
    },

    initMap() {
      let _this = this;
      let element = document.getElementById('map');
      while (element.firstChild) {
        element.removeChild(element.firstChild);
      }

      _this.map = new kakao.maps.Map(document.getElementById('map'), {
        center: new kakao.maps.LatLng(36.2683, 127.6358), // 지도의 중심좌표
        level: 13, // 지도의 확대 레벨
      });

      _this.clusterer = new kakao.maps.MarkerClusterer({
        map: _this.map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
        averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
        minLevel: 10, // 클러스터 할 최소 지도 레벨
      });

      let nowArticles = _this.articles;
      if (!_this.isSameUser) {
        nowArticles = _this.publicArticles;
      }

      for (let i = 0; i < nowArticles.length; ++i) {
        let data = nowArticles[i];
        let imageSize = new kakao.maps.Size(MARKER_WIDTH, MARKER_HEIGHT);
        let markerImage = new kakao.maps.MarkerImage(
          PUBLIC_IMAGE_SRC,
          imageSize
        );
        if (data.private) {
          markerImage = new kakao.maps.MarkerImage(
            PRIVATE_IMAGE_SRC,
            imageSize
          );
        }
        let nowMarker = new kakao.maps.Marker({
          position: new window.kakao.maps.LatLng(
            data.positionLat,
            data.positionLng
          ),
          image: markerImage,
        });

        _this.kakaoMarkers.push(nowMarker);

        let overlay = new kakao.maps.CustomOverlay({
          map: _this.map,
          position: nowMarker.getPosition(),
        });

        let wrapDiv = _this.makeCustomizedOverlay(overlay, data);

        overlay.setContent(wrapDiv);

        kakao.maps.event.addListener(nowMarker, 'click', function () {
          overlay.setMap(_this.map);
        });

        overlay.setMap(null);
      }

      _this.clusterer.addMarkers(_this.kakaoMarkers);
      if (
        this.$route.query.jsonQueryData !== undefined &&
        this.$route.query.jsonQueryData !== null
      ) {
        this.setHashtagMarkers();
      }
      if (!this.isSameUser) {
        this.setMainUsingFilteredData();
      }
      if (this.isSameUser) {
        this.promiseGetMyFavorites()
          .then(() => {
            this.setMainUsingFilteredData();
          })
          .catch(() => {
            console.log('not ok');
          });
      }
    },
    setMainUsingFilteredData() {
      if (
        sessionStorage.getItem('storedData') === null ||
        sessionStorage.getItem('storedData') === undefined
      ) {
        return;
      }

      let storedObj = JSON.parse(sessionStorage.getItem('storedData'));
      if (
        storedObj.centerPosition !== null &&
        storedObj.centerPosition !== undefined
      ) {
        this.map.setLevel(4);
        this.map.setCenter(
          new window.kakao.maps.LatLng(
            storedObj.centerPosition.positionLat,
            storedObj.centerPosition.positionLng
          )
        );
      }

      this.clearAll();

      if (storedObj.isShowFavorites) {
        this.isShowFavorites = storedObj.isShowFavorites;
        this.clusterer.addMarkers(this.favoriteMarkers);
      }
      if (storedObj.followIdx > -1) {
        this.followUserSwitches[storedObj.followIdx] = true;
        this.clickFollowUserSwitch(storedObj.followIdx);
      }
      for (let i = 0; i < this.fullHashtagNames.length; ++i) {
        if (
          storedObj.hashtagSwitches[i] &&
          this.userHashtagNames.includes(this.fullHashtagNames[i])
        ) {
          this.userHashtagSwitches[
            this.userHashtagMap.get(this.fullHashtagNames[i])
          ] = true;
        }
      }
      let paramArticles = this.articles;
      if (!this.isSameUser) {
        paramArticles = this.publicArticles;
      }
      this.selectAllHashtagSwitch = storedObj.isSelectAll;

      let currentMarkers = this.getCurrentMarkers(paramArticles);
      this.clusterer.removeMarkers(this.kakaoMarkers);
      this.clusterer.addMarkers(currentMarkers);
      sessionStorage.removeItem('storedData');
    },
    clearAll() {
      this.clusterer.clear();
      this.isShowFavorites = false;
      for (let i = 0; i < this.userHashtagSwitches.length; ++i) {
        this.userHashtagSwitches[i] = false;
      }
      for (let i = 0; i < this.followUserSwitches.length; ++i) {
        this.followUserSwitches[i] = false;
      }
      this.followMarkers = [];
    },
    setHashtagMarkers() {
      let queryData = JSON.parse(this.$route.query.jsonQueryData);
      let cnt = 0;
      for (let i = 0; i < this.fullHashtagNames.length; ++i) {
        if (
          queryData[i] &&
          this.userHashtagNames.includes(this.fullHashtagNames[i])
        ) {
          this.userHashtagSwitches[
            this.userHashtagMap.get(this.fullHashtagNames[i])
          ] = true;
          cnt += 1;
        }
      }
      let paramArticles = this.articles;
      if (!this.isSameUser) {
        paramArticles = this.publicArticles;
      }
      if (cnt > 0) {
        this.selectAllHashtagSwitch = false;
      }
      let currentMarkers = this.getCurrentMarkers(paramArticles);
      this.clusterer.removeMarkers(this.kakaoMarkers);
      this.clusterer.addMarkers(currentMarkers);
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
      closeDiv.onclick = function () {
        overlay.setMap(null);
      };
      titleDiv.appendChild(closeDiv);

      infoDiv.appendChild(titleDiv);
      let bodyDiv = document.createElement('div');
      bodyDiv.className = 'body';
      let imgDiv = document.createElement('div');
      imgDiv.className = 'img';
      let imgSrc = document.createElement('img');
      if (data.imagePaths.length > 0) {
        imgSrc.src = `https://i4b107.p.ssafy.io/images/uploads/${data.imagePaths[0]}`;
      } else {
        imgSrc.src = 'https://cdn.vuetifyjs.com/images/cards/cooking.png';
      }
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
        rating.className = 'mdi mdi-star theme--light primary--text';
        ratingDiv.appendChild(rating);
      }
      if (evaluation % 2 == 1) {
        rating = document.createElement('icon');
        rating.style.fontSize = '20px';
        rating.className = 'mdi mdi-star-half-full theme--light primary--text';
        ratingDiv.appendChild(rating);
        starCnt += 1;
      }
      for (starCnt; starCnt < 5; ++starCnt) {
        rating = document.createElement('icon');
        rating.style.fontSize = '20px';
        rating.className = 'mdi mdi-star-outline theme--light primary--text';
        ratingDiv.appendChild(rating);
      }
      descDiv.appendChild(ratingDiv);
      let aTag = document.createElement('button');
      aTag.textContent = '게시물 보기';
      aTag.onclick = function () {
        _this.goToArticleDetail(data);
      };
      descDiv.appendChild(aTag);
      bodyDiv.appendChild(imgDiv);
      bodyDiv.appendChild(descDiv);
      infoDiv.appendChild(bodyDiv);
      wrapDiv.appendChild(infoDiv);
      return wrapDiv;
    },

    addScript() {
      const script = document.createElement('script');
      /* global kakao */
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${KAKAOMAP_KEY}&libraries=services,clusterer`;
      let _this = this;
      script.onload = () =>
        kakao.maps.load(function () {
          _this.initMap();
        });
      document.head.appendChild(script);
    },
  },
  data: () => {
    return {
      fullHashtagNames: [],
      favoriteArticles: [],
      favoriteMarkers: [],
      followMarkers: [],
      followArticles: [],
      publicArticles: [],
      isShowFavorites: true,
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
      hashtagDrawer: false,
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
      mapUid: '',
    };
  },
};
</script>
<style>
.txt_line {
  width: 70px;
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
