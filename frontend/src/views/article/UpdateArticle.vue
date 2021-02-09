<template>
  <div class="container" data-app>
    <CreateArticleNav />
    <!-- vue-star-rating 추후에 삭제하기 -->
    <SelectPosition :positionObj="propsPositionObj" @emitSelectPosition="getPos" />
    <!-- <div v-if="!isCurrentMap"><SelectPosition :propsPositionObj="positionObj" /></div> -->
    <!-- <div v-if="isCurrentMap" id="currentMap" style="width:100%; height:350px"></div> -->
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
      <!-- <v-col md="4" offset-md="4">
        <v-combobox v-model="hashtagNames" :items="items" label="해쉬태그를 선택하세요." multiple chips>
          <template v-slot:selection="data">
            <v-chip :key="JSON.stringify(data.item)" v-bind="data.attrs" :input-value="data.selected" :disabled="data.disabled" @click:close="data.parent.selectItem(data.item)">
              <v-avatar class="accent white--text" left v-text="data.item.slice(0, 1).toUpperCase()"></v-avatar>
              {{ data.item }}
            </v-chip>
          </template>
        </v-combobox>
      </v-col> -->

      <v-autocomplete v-model="selectedHashtagNames" :items="hashtagNames" outlined dense chips small-chips label="해쉬태그입니다" multiple></v-autocomplete>

      <br />
      <!-- <div v-for="(hash, idx) in hashs" :key="idx" :value="hash">
        <h1>{{ hash }}</h1>
      </div> -->
    </div>
    <br />
    <div class="inline total-contents">
      이 장소의 사진
      <br />
      <br />
      <v-carousel>
        <v-carousel-item v-for="(item, i) in images" :key="i" :src="item.src" append reverse-transition="fade-transition" transition="fade-transition" multiple="true"></v-carousel-item>
      </v-carousel>
      <!-- <input type="file" @change="onFileSelected">
      <button @click="onUpload">+</button> -->
      <!-- <form encType="multipart/form-data">
        <input ref="imageInput" type="file" accept="image/*" hidden @change="onChangeImages" multiple />
      </form>
      <button class="lefty picture-upload" type="button" @click="onClickImageUpload">+</button> -->
      <!-- <div v-for="(img, idx) in imgs" :key="idx"> -->
      <!-- <img v-for="(img, idx) in imgs" :key="idx" :imgaeUrl="imageUrl" /> -->
      <!-- <input ref="imageInput" type="file" hidden @change="onChangeImages" multiple />
      <button class="lefty picture-upload" type="button" @click="onClickImageUpload">+</button>
      -->
      <v-carousel class="picture-size" v-if="imgs.length != 0">
        <v-carousel-item
          class="picture-size"
          v-for="(img, idx) in imgs"
          :key="idx"
          :src="img"
          append
          reverse-transition="fade-transition"
          transition="fade-transition"
          multiple="true"
        ></v-carousel-item>
      </v-carousel>
    </div>
    <br />
    <div>
      방문 정보 입력
      <br />
      <DatePicker :setDate="article.visitDate" label="날짜를 입력해 주세요."></DatePicker>
      <!-- <v-icon>mdi-calendar-range</v-icon> -->
      <!-- <v-text-field
        hint="방문 날짜를 선택해주세요."
        style="font-size:23px; width:300px;"
      >
      </v-text-field> -->
      <!-- <input type="date"/> -->
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
      <v-rating v-model="article.evaluation" background-color="grey lighten-1" color="blue" half-increments length="5" size="45"></v-rating>
      <!-- <StarRating :increment="0.5" :show-rating="false" :clearable="true" :star-size="45" v-model="article.evaluation" /> -->
    </div>
    <div>
      <button class="upload" @click="updatePost()">등록</button>
    </div>
    <div style="height:100px"></div>
    <Navigation />
  </div>
</template>

<script>
// import { mdiCalendarRange } from '@mdi/js';
import SelectPosition from '@/components/map/SelectPosition.vue';
import constants from '@/lib/constants';
// import axios from 'axios';
// import StarRating from 'vue-star-rating';
import CreateArticleNav from './CreateArticleNav';
import DatePicker from './DatePicker';
// import HashModal from './HashModal.vue';
// import HashList from './HashList.vue';
import { updateArticle, getArticle } from '@/api/article.js';
import { getUserHashtags } from '@/api/user.js';

import jwt_decode from 'jwt-decode';
import Navigation from '@/components/Navigation.vue';

export default {
  name: 'UpdateArticle',
  components: {
    SelectPosition,
    // StarRating,
    CreateArticleNav,
    DatePicker,
    Navigation,
    // HashModal,
    // HashList,
  },
  data() {
    return {
      isCurrentMap: false,
      positionObj: {
        positionLat: this.$route.params.article.positionLat,
        positionLng: this.$route.params.article.positionLng,
        address: this.$route.params.article.address,
      },
      imageUrl: null,
      imageUrls: Array,
      selectedFile: null,
      hashtagNames: [],
      // hash: '',
      // hashs: Array,
      imgs: [],
      // images: [],
      items: [],
      articleHashtagNames: [],
      selectedHashtagNames: [],
      images: [
        {
          src: 'https://cdn.vuetifyjs.com/images/cards/cooking.png',
        },
        {
          src: 'https://cdn.vuetifyjs.com/images/carousel/squirrel.jpg',
        },
        {
          src: 'https://cdn.vuetifyjs.com/images/carousel/sky.jpg',
        },
        {
          src: 'https://cdn.vuetifyjs.com/images/carousel/bird.jpg',
        },
      ],
      rate: 0,
      date: '',
      article: {
        positionLat: '',
        positionLng: '',
        title: '',
        address: '',
        evaluation: 0,
        hashtags: [],
        contents: '',
        visitDate: '',
        userDto: {},
      },
    };
  },
  methods: {
    setDate(date) {
      this.visitDate = date;
    },
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
        this.images.push(file);
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
    updatePost() {
      // console.log(this.article.images[0])
      // var params = new URLSearchParams();
      // params.append('file', this.images);
      // params.append('article', this.article);

      for (let i = 0; i < this.selectedHashtagNames.length; ++i) {
        if (this.articleHashtagNames.includes(this.selectedHashtagNames[i])) {
          continue;
        }
        console.log(this.selectedHashtagNames[i], '와 안돼누 ㅠㅠㅠ');
        let obj = { hashtagNo: 0, hashtagName: this.selectedHashtagNames[i] };
        console.log(obj, '와 안돼누 ㅠㅠㅠ obj.ver');
        this.article.hashtags.push(obj);
      }
      console.log(this.article.hashtags, '넣기전 해쉬태그!@!~!@~@~!#!~#~!#');
      // const imgs = new FormData();
      // sonsole.log(typeof(this.article.images))
      // const formData = new FormData();
      // console.log(this.images);
      // console.log(typeof(this.images));
      // console.log(this.images[0]);
      // console.log(this.images[1]);

      // this.images.forEach((image) => formData.append('file[]', image));
      // formData.append("file", this.images);
      // TODO: 게시글은 수정할 수 없다.
      // formData.append('article', new Blob([JSON.stringify(this.article)], { type: 'application/json' }));
      // console.log("file",formData.get("file"));
      // console.log("file",formData.get("article").hashtags);
      this.article.visitDate = this.date;
      const token = localStorage.getItem('jwt');
      let uid = jwt_decode(token).uid;
      this.article.userDto.uid = uid;
      // this.article.hashtags = this.selectedHashtagNames;
      updateArticle(
        this.article,
        (response) => {
          // console.log(response.data);
          if (response.data.status) {
            alert('작성 성공');
            this.$router.push({ name: constants.URL_TYPE.HOME.MAIN, params: { uid: uid } });
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
    console.log(this.$route.params.articleNo, '이게 게시글 번호야');
    getArticle(
      this.$route.params.articleNo,
      (response) => {
        if (response.data.status) {
          this.article = response.data.object;
          this.positionObj.positionLat = this.article.positionLat;
          this.positionObj.positionLng = this.article.positionLng;
          // this.positionObj.address = this.article.address;
          // this.items = this.article.hashtags;
          console.log(this.positionObj, 'is what i want');
          console.log(this.article);
          for (let i = 0; i < this.article.hashtags.length; i++) {
            this.selectedHashtagNames.push(this.article.hashtags[i].hashtagName);
            this.articleHashtagNames.push(this.article.hashtags[i].hashtagName);
          }
          console.log(this.selectedHashtagNames, '된거같은데?>');
          // for (let i = 0; i < this.article.hashtags.length; ++i) {
          //   this.hashtagNames.push(this.article.hashtags[i].hashtagName);
          // }
          // console.log(this.hashtagNames);
        } else {
          alert('게시글 정보를 받아올 수 없습니다.');
        }
      },
      (error) => {
        console.log(error);
        alert('서버 에러.');
      }
    );

    const token = localStorage.getItem('jwt');
    let uid = jwt_decode(token).uid;
    this.article.userDto.uid = uid;

    getUserHashtags(
      uid,
      (response) => {
        if (response.data.status) {
          let tempHashtagObjs = response.data.object;
          for (let i = 0; i < tempHashtagObjs.length; ++i) {
            this.hashtagNames.push(tempHashtagObjs[i].hashtagName);
          }
          console.log(this.hashtagNames, '해쉬태그네임');
          console.log(this.items, '아이템즈');
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
  width: 480px;
  height: 480px;
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
.total-contents {
  width: 500px;
  margin: 0 auto;
  text-align: justify;
}
</style>
