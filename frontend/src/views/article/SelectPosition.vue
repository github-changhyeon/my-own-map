<template>
  <div>
    <div id="map" style="width:100%;height:350px;"></div>
    <div id="clickLatLng"></div>
    <v-btn @click="selectFunc">위치 선택하기</v-btn>
  </div>
</template>

<script>
const KAKAOMAP_KEY = process.env.VUE_APP_KAKAOMAP_KEY;
import constants from '@/lib/constants';

export default {
  name: 'SelectPosition',
  components: {},
  props: [],
  computed: {},
  watch: {},
  created() {},
  mounted() {
    window.kakao && window.kakao.maps ? this.showMap() : this.addScript();
  },
  methods: {
    selectFunc() {
      if (this.$route.params.where == 'create') {
        this.$router.push({ name: constants.URL_TYPE.ARTICLE.CREATEARTICLE, params: { positionObj: this.positionObj } });
      } else if (this.$route.params.where == 'update') {
        this.$router.push({ name: constants.URL_TYPE.ARTICLE.UPDATEARTICLE, params: { articleNo: this.$route.params.articleNo, positionObj: this.positionObj } });
      }
    },
    showMap() {
      let _this = this;
      var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
          center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
          level: 12, // 지도의 확대 레벨
        };

      var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

      // 지도를 클릭한 위치에 표출할 마커입니다
      var marker = new kakao.maps.Marker({
        // 지도 중심좌표에 마커를 생성합니다
        position: map.getCenter(),
      });
      // 지도에 마커를 표시합니다
      marker.setMap(map);
      var geocoder = new kakao.maps.services.Geocoder();
      // 지도에 클릭 이벤트를 등록합니다
      // 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
      kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
        // 클릭한 위도, 경도 정보를 가져옵니다
        var latlng = mouseEvent.latLng;

        // 마커 위치를 클릭한 위치로 옮깁니다
        marker.setPosition(latlng);
        _this.positionObj.positionLat = latlng.getLat();
        _this.positionObj.positionLng = latlng.getLng();
        //  = { positionLat: latlng.getLat(), positionLng: latlng.getLng() };
        let message = '클릭한 위치의 위도는 ' + latlng.getLat() + ' 이고, ';
        message += '경도는 ' + latlng.getLng() + ' 입니다';

        geocoder.coord2Address(latlng.getLng(), latlng.getLat(), function(result, status) {
          if (status === kakao.maps.services.Status.OK) {
            var detailAddr = '';

            detailAddr += result[0].address.address_name;
            _this.positionObj.address = detailAddr;
            message += detailAddr;
            let resultDiv = document.getElementById('clickLatLng');
            resultDiv.innerHTML = message;
          } else {
            let resultDiv = document.getElementById('clickLatLng');
            resultDiv.innerHTML = message;
          }
        });
      });
    },
    addScript() {
      const script = document.createElement('script');
      /* global kakao */
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${KAKAOMAP_KEY}&libraries=services,clusterer`;
      let _this = this;
      script.onload = () =>
        kakao.maps.load(function() {
          _this.showMap();
        });
      //  kakao.maps.load(this.showMap); 과 비교
      document.head.appendChild(script);
    },
  },
  data: function() {
    return {
      positionObj: { positionLat: 33.450701, positionLng: 126.570667, address: '' },
    };
  },
};
</script>
