<template>
  <div>
    <div class="map_wrap">
      <div
        id="map"
        style="width:100%;height:100%;position:relative;overflow:hidden;"
      ></div>

      <div id="menu_wrap_search" class="bg_white">
        <div class="option">
          <div>
            <v-form @submit.prevent="searchPlaces">
              키워드 :
              <input
                type="text"
                placeholder="검색어를 입력해주세요"
                id="keyword"
                size="15"
              />
              <button type="submit">검색하기</button>
            </v-form>
            <button></button>
          </div>
        </div>
      </div>
      <hr />
      <div v-show="isShowList">
        <div id="menu_wrap_body" class="bg_white">
          <ul id="placesList"></ul>
          <div id="pagination"></div>
        </div>
        <div id="menu_wrap_bottom" class="bg_white">
          <button @click="isShowList = false">검색 결과 숨기기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
const KAKAOMAP_KEY = process.env.VUE_APP_KAKAOMAP_KEY;
const STAR_IMAGE_SRC =
  'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png';
const SPRITE_IMAGE_SRC =
  'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png';
export default {
  name: 'SelectPosition',
  components: {},
  props: ['propsPositionObj'],
  computed: {},
  watch: {},
  created() {},
  mounted() {
    window.kakao && window.kakao.maps ? this.initMap() : this.addScript();
  },
  methods: {
    searchPlaces() {
      let keyword = document.getElementById('keyword').value;
      let _this = this;
      if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return;
      }

      let ps = new kakao.maps.services.Places();
      // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
      ps.keywordSearch(keyword, function(data, status, pagination) {
        _this.searchPlacesCB(data, status, pagination);
      });
    },
    searchPlacesCB(data, status, pagination) {
      if (status === kakao.maps.services.Status.OK) {
        // 정상적으로 검색이 완료됐으면
        // 검색 목록과 마커를 표출합니다
        this.displayPlaces(data);

        // 페이지 번호를 표출합니다
        this.displayPagination(pagination);
      } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        alert('검색 결과가 존재하지 않습니다.');
        return;
      } else if (status === kakao.maps.services.Status.ERROR) {
        alert('검색 결과 중 오류가 발생했습니다.');
        return;
      }
    },
    displayPlaces(places) {
      let _this = this;
      _this.isShowList = true;
      let listEl = document.getElementById('placesList'),
        menuEl = document.getElementById('menu_wrap_body'),
        fragment = document.createDocumentFragment(),
        bounds = new kakao.maps.LatLngBounds();
      // listStr = '';

      // 검색 결과 목록에 추가된 항목들을 제거합니다
      this.removeAllChildNods(listEl);

      // 지도에 표시되고 있는 마커를 제거합니다
      this.removeMarker();

      for (let i = 0; i < places.length; i++) {
        // 마커를 생성하고 지도에 표시합니다
        let placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
          marker = _this.addMarker(placePosition, i),
          itemEl = _this.getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
        // LatLngBounds 객체에 좌표를 추가합니다
        bounds.extend(placePosition);

        // 마커와 검색결과 항목에 mouseover 했을때
        // 해당 장소에 인포윈도우에 장소명을 표시합니다
        // mouseout 했을 때는 인포윈도우를 닫습니다
        (function(marker, title) {
          kakao.maps.event.addListener(marker, 'mouseover', function() {
            _this.displayInfowindow(marker, title);
          });

          kakao.maps.event.addListener(marker, 'mouseout', function() {
            _this.infowindow.close();
          });

          kakao.maps.event.addListener(marker, 'click', function() {
            _this.starMarker.setPosition(placePosition);
            _this.map.setLevel(4);
            _this.map.setCenter(placePosition);
            _this.positions.positionLat = places[i].y;
            _this.positions.positionLng = places[i].x;
            _this.positions.address = places[i].address_name;
            _this.$emit('emitSelectPosition', _this.positions);
          });

          itemEl.onmouseover = function() {
            _this.displayInfowindow(marker, title);
          };

          itemEl.onmouseout = function() {
            _this.infowindow.close();
          };
          itemEl.onclick = function() {
            _this.starMarker.setPosition(placePosition);
            _this.map.setLevel(4);
            _this.map.setCenter(placePosition);
            _this.positions.positionLat = places[i].y;
            _this.positions.positionLng = places[i].x;
            _this.positions.address = places[i].address_name;
            _this.$emit('emitSelectPosition', _this.positions);
          };
        })(marker, places[i].place_name);

        fragment.appendChild(itemEl);
      }

      // 검색결과 항목들을 검색결과 목록 Elemnet에 추가합니다
      listEl.appendChild(fragment);
      menuEl.scrollTop = 0;

      // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
      this.map.setBounds(bounds);
    },
    displayInfowindow(marker, title) {
      let content = '<div style="padding:5px;z-index:1;">' + title + '</div>';

      this.infowindow.setContent(content);
      this.infowindow.open(this.map, marker);
    },
    addMarker(position, idx) {
      let _this = this;
      let imageSize = new kakao.maps.Size(36, 37); // 마커 이미지의 크기
      let imgOptions = {
        spriteSize: new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
        spriteOrigin: new kakao.maps.Point(0, idx * 46 + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
        offset: new kakao.maps.Point(13, 37), // 마커 좌표에 일치시킬 이미지 내에서의 좌표
      };
      let markerImage = new kakao.maps.MarkerImage(
        SPRITE_IMAGE_SRC,
        imageSize,
        imgOptions
      );
      let marker = new kakao.maps.Marker({
        position: position, // 마커의 위치
        image: markerImage,
      });

      marker.setMap(_this.map); // 지도 위에 마커를 표출합니다
      _this.markers.push(marker); // 배열에 생성된 마커를 추가합니다

      return marker;
    },
    getListItem(index, places) {
      let el = document.createElement('li'),
        itemStr =
          '<span class="markerbg marker_' +
          (index + 1) +
          '"></span>' +
          '<div class="infos">' +
          '   <h5>' +
          places.place_name +
          '</h5>';

      itemStr += '    <span>' + places.address_name + '</span>';

      el.innerHTML = itemStr;
      el.className = 'item';

      return el;
    },
    removeMarker() {
      for (let i = 0; i < this.markers.length; i++) {
        this.markers[i].setMap(null);
      }
      this.markers = [];
    },
    removeAllChildNods(el) {
      while (el.hasChildNodes()) {
        el.removeChild(el.lastChild);
      }
    },
    displayPagination(pagination) {
      let paginationEl = document.getElementById('pagination'),
        fragment = document.createDocumentFragment(),
        i;

      // 기존에 추가된 페이지번호를 삭제합니다
      while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild(paginationEl.lastChild);
      }

      for (i = 1; i <= pagination.last; i++) {
        var el = document.createElement('a');
        el.href = '#';
        el.innerHTML = i;

        if (i === pagination.current) {
          el.className = 'on';
        } else {
          el.onclick = (function(i) {
            return function() {
              pagination.gotoPage(i);
            };
          })(i);
        }

        fragment.appendChild(el);
      }
      paginationEl.appendChild(fragment);
    },
    setMarkerListener() {
      let _this = this;
      let geocoder = new kakao.maps.services.Geocoder();
      // 지도에 클릭 이벤트를 등록합니다
      // 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
      kakao.maps.event.addListener(_this.map, 'click', function(mouseEvent) {
        // 클릭한 위도, 경도 정보를 가져옵니다
        let latlng = mouseEvent.latLng;

        // 마커 위치를 클릭한 위치로 옮깁니다
        _this.starMarker.setPosition(latlng);
        _this.positions.positionLat = latlng.getLat();
        _this.positions.positionLng = latlng.getLng();
        //  = { positionLat: latlng.getLat(), positionLng: latlng.getLng() };
        _this.infowindow.close();

        geocoder.coord2Address(latlng.getLng(), latlng.getLat(), function(
          result,
          status
        ) {
          if (status === kakao.maps.services.Status.OK) {
            let detailAddr = result[0].address.address_name;
            _this.positions.address = detailAddr;
            _this.$emit('emitSelectPosition', _this.positions);
          } else {
            _this.$emit('emitSelectPosition', _this.positions);
          }
        });
      });
    },
    initMap() {
      let _this = this;
      let mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
          center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
          level: 10, // 지도의 확대 레벨
        };

      _this.map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
      if (
        this.propsPositionObj === undefined ||
        this.propsPositionObj === null
      ) {
        let geocoder = new kakao.maps.services.Geocoder();
        // HTML5의 geolocation으로 사용할 수 있는지 확인합니다

        if (navigator.geolocation) {
          let locPosition = new kakao.maps.LatLng(33.450701, 126.570667);
          let message = '위치 연동을 허용해주세요..';
          let imageSize = new kakao.maps.Size(24, 35);
          let markerImage = new kakao.maps.MarkerImage(
            STAR_IMAGE_SRC,
            imageSize
          );
          _this.starMarker = new kakao.maps.Marker({
            map: _this.map,
            position: locPosition,
            image: markerImage,
          });
          let iwContent = message;
          let iwRemoveable = true;
          _this.infowindow = new kakao.maps.InfoWindow({
            disableAutoPan: true,
            content: iwContent,
            removable: iwRemoveable,
          });
          _this.infowindow.open(_this.map, _this.starMarker);
          this.setMarkerListener();

          // GeoLocation을 이용해서 접속 위치를 얻어옵니다
          navigator.geolocation.getCurrentPosition(function(position) {
            _this.positions.positionLat = position.coords.latitude;
            _this.positions.positionLng = position.coords.longitude;

            locPosition = new kakao.maps.LatLng(
              _this.positions.positionLat,
              _this.positions.positionLng
            ); // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다

            message = '<div style="padding:5px;">여기에 계시군요!</div>';
            // imageSize = new kakao.maps.Size(24, 35);
            // markerImage = new kakao.maps.MarkerImage(STAR_IMAGE_SRC, imageSize);
            _this.starMarker.setPosition(locPosition);
            iwContent = message;
            _this.infowindow.setContent(iwContent);
            _this.infowindow.open(_this.map, _this.starMarker);
            _this.map.setCenter(locPosition);
            geocoder.coord2Address(
              _this.positions.positionLng,
              _this.positions.positionLat,
              function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                  let detailAddr = result[0].address.address_name;
                  _this.positions.address = detailAddr;
                  _this.$emit('emitSelectPosition', _this.positions);
                } else {
                  _this.$emit('emitSelectPosition', _this.positions);
                }
              }
            );
          });
        } else {
          let locPosition = new kakao.maps.LatLng(33.450701, 126.570667);
          let message = 'geolocation을 사용할수 없어요..';
          let imageSize = new kakao.maps.Size(24, 35);
          let markerImage = new kakao.maps.MarkerImage(
            STAR_IMAGE_SRC,
            imageSize
          );
          _this.starMarker = new kakao.maps.Marker({
            map: _this.map,
            position: locPosition,
            image: markerImage,
          });
          let iwContent = message;
          let iwRemoveable = true;
          _this.infowindow = new kakao.maps.InfoWindow({
            disableAutoPan: true,
            content: iwContent,
            removable: iwRemoveable,
          });
          _this.infowindow.open(_this.map, _this.starMarker);
          this.setMarkerListener();
        }
      } else {
        let locPosition = new kakao.maps.LatLng(
          this.propsPositionObj.positionLat,
          this.propsPositionObj.positionLng
        );
        let message = '이 장소군요!!';
        let imageSize = new kakao.maps.Size(24, 35);
        let markerImage = new kakao.maps.MarkerImage(STAR_IMAGE_SRC, imageSize);
        _this.starMarker = new kakao.maps.Marker({
          map: _this.map,
          position: locPosition,
          image: markerImage,
        });
        let iwContent = message;
        let iwRemoveable = true;
        _this.infowindow = new kakao.maps.InfoWindow({
          disableAutoPan: true,
          content: iwContent,
          removable: iwRemoveable,
        });
        _this.infowindow.open(_this.map, _this.starMarker);
        this.setMarkerListener();
      }
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
  data: function() {
    return {
      infowindow: {},
      map: {},
      isShowList: true,
      starMarker: {},
      markers: [],
      positions: { positionLat: '', positionLng: '', address: '' },
    };
  },
};
</script>
<style>
.map_wrap,
.map_wrap * {
  margin: 0;
  padding: 0;
  font-family: 'Malgun Gothic', dotum, '돋움', sans-serif;
  font-size: 12px;
}
.map_wrap a,
.map_wrap a:hover,
.map_wrap a:active {
  color: #000;
  text-decoration: none;
}
.map_wrap {
  position: relative;
  width: 100%;
  height: 500px;
}
#menu_wrap {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  width: 250px;
  margin: 10px 0 30px 10px;
  padding: 5px;
  overflow-y: auto;
  background: rgba(255, 255, 255, 0.7);
  z-index: 1;
  font-size: 12px;
  border-radius: 10px;
}
#menu_wrap_search {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  width: 250px;
  height: 36px;
  margin: 10px 0 30px 10px;
  padding: 5px;
  overflow-y: auto;
  background: rgba(255, 255, 255, 0.7);
  z-index: 1;
  font-size: 12px;
  border-radius: 10px;
}
#menu_wrap_body {
  position: absolute;
  top: 46px;
  left: 0;
  bottom: 0;
  width: 250px;
  height: 350px;
  margin: 10px 0 30px 10px;
  padding: 5px;
  overflow-y: auto;
  background: rgba(255, 255, 255, 0.7);
  z-index: 1;
  font-size: 12px;
  border-radius: 10px;
}

#menu_wrap_bottom {
  position: absolute;
  left: 62.5px;
  text-align: center;
  bottom: 30px;
  width: 120px;
  height: 30px;
  margin: 10px 0 30px 10px;
  padding: 5px;
  overflow-y: auto;
  background: rgba(255, 255, 255, 0.7);
  z-index: 1;
  font-size: 12px;
  border-radius: 10px;
}
.bg_white {
  background: #fff;
}
#menu_wrap hr {
  display: block;
  height: 1px;
  border: 0;
  border-top: 2px solid #5f5f5f;
  margin: 3px 0;
}
#menu_wrap_search hr {
  display: block;
  height: 1px;
  border: 0;
  border-top: 2px solid #5f5f5f;
  margin: 3px 0;
}
#menu_wrap_body hr {
  display: block;
  height: 1px;
  border: 0;
  border-top: 2px solid #5f5f5f;
  margin: 3px 0;
}
#menu_wrap .option {
  text-align: center;
}
#menu_wrap_search .option {
  text-align: center;
}
#menu_wrap_body .option {
  text-align: center;
}
#menu_wrap .option p {
  margin: 10px 0;
}
#menu_wrap_search .option p {
  margin: 10px 0;
}
#menu_wrap_body .option p {
  margin: 10px 0;
}
#menu_wrap .option button {
  margin-left: 5px;
}
#menu_wrap_search .option button {
  margin-left: 5px;
}
#menu_wrap_body .option button {
  margin-left: 5px;
}
#placesList {
  padding: 0;
}
#placesList li {
  list-style: none;
}
#placesList .item {
  position: relative;
  border-bottom: 1px solid #888;
  overflow: hidden;
  cursor: pointer;
  min-height: 65px;
}
#placesList .item span {
  display: block;
  margin-top: 4px;
}
#placesList .item h5,
#placesList .item .infos {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
#placesList .item .infos {
  padding: 10px 0 10px 55px;
}
#placesList .infos .gray {
  color: #8a8a8a;
}
#placesList .infos .jibun {
  padding-left: 26px;
  background: url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_jibun.png)
    no-repeat;
}
#placesList .infos .tel {
  color: #009900;
}
#placesList .item .markerbg {
  float: left;
  position: absolute;
  width: 36px;
  height: 37px;
  margin: 10px 0 0 10px;
  background: url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png)
    no-repeat;
}
#placesList .item .marker_1 {
  background-position: 0 -10px;
}
#placesList .item .marker_2 {
  background-position: 0 -56px;
}
#placesList .item .marker_3 {
  background-position: 0 -102px;
}
#placesList .item .marker_4 {
  background-position: 0 -148px;
}
#placesList .item .marker_5 {
  background-position: 0 -194px;
}
#placesList .item .marker_6 {
  background-position: 0 -240px;
}
#placesList .item .marker_7 {
  background-position: 0 -286px;
}
#placesList .item .marker_8 {
  background-position: 0 -332px;
}
#placesList .item .marker_9 {
  background-position: 0 -378px;
}
#placesList .item .marker_10 {
  background-position: 0 -423px;
}
#placesList .item .marker_11 {
  background-position: 0 -470px;
}
#placesList .item .marker_12 {
  background-position: 0 -516px;
}
#placesList .item .marker_13 {
  background-position: 0 -562px;
}
#placesList .item .marker_14 {
  background-position: 0 -608px;
}
#placesList .item .marker_15 {
  background-position: 0 -654px;
}
#pagination {
  margin: 10px auto;
  text-align: center;
}
#pagination a {
  display: inline-block;
  margin-right: 10px;
}
#pagination .on {
  font-weight: bold;
  cursor: default;
  color: #777;
}
</style>
