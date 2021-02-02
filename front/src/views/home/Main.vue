<template>
  <div>
    <!-- <div> -->
    <v-app-bar style="position:fixed; top:0; z-index:2">
      <v-spacer></v-spacer>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
    </v-app-bar>
    <!-- </div> -->
    <v-navigation-drawer v-model="drawer" absolute right temporary>
      <v-list>
        <v-list-item @click="goToMyPage">
          <v-list-item-icon>
            <v-icon>mdi-home</v-icon>
            <!-- <v-icon>mdi-chevron-right</v-icon> -->
          </v-list-item-icon>
          <v-list-item-title>마이페이지</v-list-item-title>
        </v-list-item>
        <!-- <NaverLogin/> -->
        <v-list-item @click="logout">
          <v-list-item-title>로그아웃</v-list-item-title>
        </v-list-item>

        <v-list-group prepend-icon="mdi-account-circle">
          <template v-slot:activator>
            <v-list-item-title>해시태그</v-list-item-title>
          </template>

          <v-list-item>
            <v-switch @click="clickShowAllSwitch(selectAllSwitch)" v-model="selectAllSwitch" label="전체보기"></v-switch>
          </v-list-item>
          <v-list-item v-for="(hashtag, i) in userHashtags" :key="i" link>
            <!-- <v-list-item-title v-text="hashtag"></v-list-item-title> -->
            <v-switch @click="clickHashtagSwitch(userHashtagSwitches[i])" v-model="userHashtagSwitches[i]" :label="hashtag.hashtagName"></v-switch>
          </v-list-item>
        </v-list-group>
      </v-list>
    </v-navigation-drawer>

    <v-row justify="end">
      <v-btn class="ma-2" fab small dark @click="switchDrawer = !switchDrawer" style="position: fixed; top: 160px; right:5px; z-index: 2;">
        <v-icon dark>
          mdi-plus
        </v-icon>
      </v-btn>
      <!-- <v-btn class="ma-2" @click="moveCreateArticle" style="position: fixed; bottom: 160px; right:5px; z-index: 2;" icon> -->
      <!-- <v-icon @click="moveCreateArticle" style="position: fixed; bottom: 160px; right:5px; z-index: 2;" link>mdi-plus-circle</v-icon> -->
      <!-- </v-btn> -->
    </v-row>

    <v-navigation-drawer v-model="switchDrawer" absolute right temporary>
      <v-list-item>
        <h3>해시태그</h3>
      </v-list-item>
      <v-list-time>
        <v-autocomplete v-model="selectedHashtags" :items="userHashtagNames" multiple dense filled :search-input.sync="search" @change="search = ''" label="Filled"></v-autocomplete>
      </v-list-time>
      <v-list-item>
        <v-switch @click="clickShowAllSwitch(selectAllSwitch)" v-model="selectAllSwitch" label="전체보기"></v-switch>
      </v-list-item>
      <v-list-item v-for="(hashtag, i) in userHashtags" :key="i" link>
        <!-- <v-list-item-title v-text="hashtag"></v-list-item-title> -->
        <v-switch
          v-if="selectedHashtags.length == 0 || selectedHashtags.includes(hashtag.hashtagName)"
          @click="clickHashtagSwitch(userHashtagSwitches[i])"
          v-model="userHashtagSwitches[i]"
          :label="hashtag.hashtagName"
        ></v-switch>
      </v-list-item>
    </v-navigation-drawer>

    <div id="map" style="width: 100vw; height: 100vh; z-index: 1;"></div>
    <!-- <div style="position: fixed; bottom: 0; z-index: 2"> -->
    <v-expand-x-transition>
      <v-row justify="center" v-if="expand">
        <v-sheet class="mx-auto" elevation="8" max-width="100vw" style="position: fixed; bottom: 0; z-index: 2; ">
          <v-slide-group v-model="model" class="pa-4" show-arrows>
            <v-slide-item v-for="(article, i) in recentArticles" :key="i">
              <v-card class="ma-4" height="100" width="70" @click="goToArticleDetail(article)">
                <v-img height="70" src="https://cdn.vuetifyjs.com/images/cards/cooking.png"></v-img>
                <v-card-title class="txt_line" style="padding: 0">{{ article.title }}</v-card-title>
              </v-card>
            </v-slide-item>
          </v-slide-group>
        </v-sheet>
      </v-row>
    </v-expand-x-transition>
    <!-- </div> -->
    <v-btn class="ma-2" light fab small @click="expand = !expand" style="position: fixed; bottom: 160px; z-index: 2;">
      <v-icon v-if="!expand">mdi-chevron-right</v-icon>
      <v-icon v-if="expand">mdi-chevron-left</v-icon>
    </v-btn>
    <v-row justify="end">
      <v-btn class="ma-2" fab small dark @click="goToCreateArticle" style="position: fixed; bottom: 160px; right:5px; z-index: 2;">
        <v-icon dark>
          mdi-plus
        </v-icon>
      </v-btn>
      <!-- <v-btn class="ma-2" @click="moveCreateArticle" style="position: fixed; bottom: 160px; right:5px; z-index: 2;" icon> -->
      <!-- <v-icon @click="moveCreateArticle" style="position: fixed; bottom: 160px; right:5px; z-index: 2;" link>mdi-plus-circle</v-icon> -->
      <!-- </v-btn> -->
    </v-row>
  </div>
</template>

<script>
// import constants from '../../lib/constants';
// import { login } from '@/api/user.js';
import { getArticles, getRecentArticles, getUserHashtags } from '@/api/article.js';
import constants from '@/lib/constants';

const KAKAOMAP_KEY = process.env.VUE_APP_KAKAOMAP_KEY;

export default {
  components: {},
  created() {
    getArticles(
      1,
      (response) => {
        if (response.data.status) {
          this.articles = response.data.object;
          window.kakao && window.kakao.maps ? this.showMap(this.articles) : this.addScript();
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
      1,
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

    getUserHashtags(
      1,
      (response) => {
        if (response.data.status) {
          this.userHashtags = response.data.object;
          // alert('hashtag list를 받았습니다.');

          // alert(this.userHashtags.length);
          for (let i = 0; i < this.userHashtags.length; ++i) {
            // alert(this.userHashtags[i].hashtagName);
            this.userHashtagMap.set(this.userHashtags[i].hashtagName, i);
            this.userHashtagNames.push(this.userHashtags[i].hashtagName);
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

    // TODO : 해시태그 데이터들과 장소 데이터들 받아오기, 장소 데이터 가지고 axios해서 장소데이터의 hashtag받아오기
  },
  mounted() {},
  watch: {},
  methods: {
    goToCreateArticle() {
      this.$router.push({ name: constants.URL_TYPE.ARTICLE.CREATEARTICLE });
    },
    goToArticleDetail(article) {
      this.$router.push({ name: constants.URL_TYPE.ARTICLE.ARTICLEDETAIL, params: { articleNo: article.articleNo, article: article } });
    },
    goToMyPage() {
      alert('마이페이지로 이동');
    },
    logout() {
      alert('로그아웃');
    },
    clickHashtagSwitch(isOn) {
      if (isOn) {
        this.selectAllSwitch = false;
      }
      let showDatas = [];
      // let dataSet = new Set();
      // for (let i = 0; i < this.userHashtags.length; ++i) {
      //   if (this.userHashtagSwitches[this.hashtagMap.get(this.userHashtags[i].name)]) {
      //     dataSet.add(this.hashtags[i].name);
      //   }
      // }
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
          showDatas.push(this.articles[i]);
        }
      }
      this.showMap(showDatas);
    },
    clickShowAllSwitch(isOn) {
      if (isOn) {
        for (let i = 0; i < this.userHashtagSwitches.length; ++i) {
          this.userHashtagSwitches[i] = false;
        }
        this.showMap(this.articles);
      } else {
        let empties = [];
        this.showMap(empties);
      }
    },

    showMap(datas) {
      let _this = this;
      let element = document.getElementById('map');
      while (element.firstChild) {
        element.removeChild(element.firstChild);
      }

      var map = new kakao.maps.Map(document.getElementById('map'), {
        // 지도를 표시할 div
        center: new kakao.maps.LatLng(36.2683, 127.6358), // 지도의 중심좌표
        level: 13, // 지도의 확대 레벨
      });

      // 마커 클러스터러를 생성합니다
      var clusterer = new kakao.maps.MarkerClusterer({
        map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
        averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
        minLevel: 10, // 클러스터 할 최소 지도 레벨
      });

      // var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png', // 마커이미지의 주소입니다
      //   imageSize = new kakao.maps.Size(64, 69), // 마커이미지의 크기입니다
      //   imageOption = { offset: new kakao.maps.Point(27, 69) }; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

      // // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
      // var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

      let markers = datas.map((data) => {
        let nowMarker = new window.kakao.maps.Marker({
          position: new window.kakao.maps.LatLng(data.positionLat, data.positionLng),
          // image: markerImage,
        });

        // TODO : 커스텀 오버레이
        let infowindow = new kakao.maps.InfoWindow({
          content: data.title, // 인포윈도우에 표시할 내용
        });
        kakao.maps.event.addListener(nowMarker, 'mouseover', function() {
          infowindow.open(map, nowMarker);
        });
        kakao.maps.event.addListener(nowMarker, 'mouseout', function() {
          infowindow.close();
        });
        kakao.maps.event.addListener(nowMarker, 'click', function() {
          // TODO : 라우터 이동
          _this.goToArticleDetail(data);
        });
        return nowMarker;
      });

      // 클러스터러에 마커들을 추가합니다
      clusterer.addMarkers(markers);
      //   });
    },
    addScript() {
      const script = document.createElement('script');
      /* global kakao */
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${KAKAOMAP_KEY}&libraries=services,clusterer`;
      let _this = this;
      script.onload = () =>
        kakao.maps.load(function() {
          _this.showMap(_this.articles);
        });
      //  kakao.maps.load(this.showMap); 과 비교
      document.head.appendChild(script);
    },
  },
  data: () => {
    return {
      selectedHashtags: [],
      search: '',
      switchDrawer: false,
      expand: false,
      recentArticles: [],
      model: null,
      selectAllSwitch: true,
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
</style>
