<template>
  <div class="container" data-app>
    <CreateArticleNav />
    <!-- vue-star-rating 추후에 삭제하기 -->
    <SelectPosition @emitSelectPosition="getPos" />
    <!-- <div v-if="!isCurrentMap"><SelectPosition :propsPositionObj="positionObj" /></div> -->
    <!-- <div v-if="isCurrentMap" id="currentMap" style="width:100%; height:350px"></div> -->
    <br />
    <div class="center">
      <v-btn @click="moveSelectPosition">위치 선택하기</v-btn>
    </div>
    <br />
    <div class="center">
      <label for="address"></label>
      <input class="inputbox" type="text" id="address" disabled v-model="article.address" placeholder="주소는 자동입력됩니다." />
    </div>
    <br />
    <div class="center">
      <label for="title"></label>
      <input class="inputbox" type="text" id="title" v-model="article.title" placeholder="제목" />
    </div>
    <br />
    <div class="center">
      <label for="contents"></label>
      <input class="inputbox" type="text" id="contents" v-model="article.contents" placeholder="설명" />
    </div>
    <br />
    <div>
      해쉬태그
      <br />
      <br />
      <!-- <HashModal />
      <HashList /> -->
      <v-col cols="12">
        <v-combobox v-model="hashtagNames" :items="items" label="해쉬태그를 선택하세요." multiple chips>
          <template v-slot:selection="data">
            <v-chip :key="JSON.stringify(data.item)" v-bind="data.attrs" :input-value="data.selected" :disabled="data.disabled" @click:close="data.parent.selectItem(data.item)">
              <v-avatar class="accent white--text" left v-text="data.item.slice(0, 1).toUpperCase()"></v-avatar>
              {{ data.item }}
            </v-chip>
          </template>
        </v-combobox>
      </v-col>
      <br />
      <!-- <div v-for="(hash, idx) in hashs" :key="idx" :value="hash">
        <h1>{{ hash }}</h1>
      </div> -->
    </div>
    <br />
    <div class="inline">
      이 장소의 사진
      <br />
      <br />
      <input ref="imageInput" type="file" hidden @change="onChangeImages" multiple />
      <button class="lefty picture-upload" type="button" @click="onClickImageUpload">+</button>
      <v-carousel>
        <v-carousel-item v-for="(img, idx) in imgs" :key="idx" :src="img" append reverse-transition="fade-transition" transition="fade-transition" multiple="true"></v-carousel-item>
      </v-carousel>
      <!-- <div v-for="(img, idx) in imgs" :key="idx">
        <img :src="img" alt="" class="picture-size" />
      </div> -->
    </div>
    <br />
    <div>
      방문 정보 입력
      <br />
      <!-- <v-text-field
        hint="방문 날짜를 선택해주세요."
        style="font-size:23px; width:300px;"
      >
      </v-text-field> -->
      <input type="date" />
      <!-- <v-date-picker
      width="350"
      class="mt-4"
      :min= min
      :max= max
      locale="ko-KR"
      :first-day-of-week="1"
      color="#FDDAB4"
      no-title
      style="font-size:18px;"
      elevation="15"
      >
      </v-date-picker> -->
    </div>
    <div class="center">
      <StarRating :increment="0.5" :show-rating="false" :clearable="true" :star-size="45" v-model="article.evaluation" />
    </div>
    <div>
      <button class="upload" @click="createPost()">등록</button>
    </div>
  </div>
</template>

<script>
import SelectPosition from '@/components/map/SelectPosition.vue';
import constants from '@/lib/constants';
// import axios from 'axios';
import StarRating from 'vue-star-rating';
import CreateArticleNav from './CreateArticleNav';
// import HashModal from './HashModal.vue';
// import HashList from './HashList.vue';
import { createArticle, getUserHashtags } from '@/api/article.js';

export default {
  name: 'CreateArticle',
  components: {
    SelectPosition,
    StarRating,
    CreateArticleNav,
    // HashModal,
    // HashList,
  },
  data() {
    return {
      isCurrentMap: false,
      items: [],
      positionObj: { positionLat: 33.450701, positionLng: 126.570667, address: '' },
      imageUrl: null,
      imageUrls: Array,
      selectedFile: null,
      hashtagNames: [],
      // hash: '',
      // hashs: Array,
      imgs: [],
      rate: 0,
      article: {
        positionLat: '',
        positionLng: '',
        title: '',
        address: '',
        evaluation: 0,
        hashtags: [],
        contents: '',
        images: [],
      },
    };
  },
  methods: {
    getPos(positions) {
      this.article.positionLat = positions.positionLat;
      this.article.positionLng = positions.positionLng;
      this.article.address = positions.address;
    },

    onClickImageUpload() {
      this.$refs.imageInput.click();
      // this.imageUrls.push(newImg);
    },
    onChangeImages(e) {
      for (let i = 0; i < e.target.files.length; i++) {
        const file = e.target.files[i];
        this.imageUrl = URL.createObjectURL(file);
        this.imgs.push(this.imageUrl);
      }
    },
    // addHash() {
    //   const newHash = {
    //     content: this.hash,
    //   };
    //   axios.post('url', newHash).then((res) => {
    //     this.hashs.splice(0, 0, res.data);
    //     this.hash = '';
    //   });
    // },
    createPost() {
      for (let i = 0; i < this.hashtagNames.length; ++i) {
        let obj = { hashtagNo: 0, hashtagName: this.hashtagNames[i] };
        this.article.hashtags.push(obj);
      }

      createArticle(
        this.article,
        (response) => {
          // console.log(response.data);
          if (response.data.status) {
            alert('작성 성공');
            this.$router.replace({ name: constants.URL_TYPE.HOME.MAIN });
          } else {
            alert('작성 실패');
          }
        },
        (error) => {
          console.log(error);
          alert('서버 에러.');
        }
      );
    },
  },
  watch: {
    // imageUrl(newImg) {
    //   // this.imageUrls.push(newImg);
    // },
  },
  mounted() {},
  created() {
    getUserHashtags(
      1,
      (response) => {
        if (response.data.status) {
          let tempHashtagObjs = response.data.object;
          for (let i = 0; i < tempHashtagObjs.length; ++i) {
            this.items.push(tempHashtagObjs[i].hashtagName);
          }
          // alert('해쉬태그 받기 성공');
        } else {
          alert('해쉬태그 받기 실패');
        }
      },
      (error) => {
        console.log(error);
        alert('서버 에러.');
      }
    );
  },
};
</script>

<style scoped>
.container {
  /* width: 500px; */
  width: 100vw;
  min-height: 100vh;
  margin: 0 auto;
}

.center {
  display: flex;
  justify-content: center;
}

.inputbox {
  border: solid black 2px;
  width: 400px;
}

.picture-size {
  width: 100px;
  height: 150px;
  border: 1px solid black;
  float: left;
  margin-right: 10px;
}

.lefty {
  float: left;
}

.inline {
  display: inline-block;
}

.folder-upload {
  width: 25px;
  height: 25px;
  border: solid black 2px;
  border-radius: 5px;
  margin-top: 13px;
}

.picture-upload {
  width: 40px;
  height: 40px;
  border: solid black 2px;
  border-radius: 5px;
  margin-bottom: 13px;
}

.upload {
  float: right;
}
</style>
