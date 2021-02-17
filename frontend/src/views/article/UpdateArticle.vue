<template>
  <div class="container" data-app>
    <CreateArticleNav />
    <!-- vue-star-rating 추후에 삭제하기 -->
    <SelectPosition v-if="positionObj !== null" :propsPositionObj="positionObj" @emitSelectPosition="getPos" />
    <!-- <div v-if="!isCurrentMap"><SelectPosition :propsPositionObj="positionObj" /></div> -->
    <!-- <div v-if="isCurrentMap" id="currentMap" style="width:100%; height:350px"></div> -->
    <br />
    <div class="center">
      <v-text-field outlined type="text" id="address" disabled v-model="article.address" placeholder="주소는 자동입력됩니다."> </v-text-field>
    </div>
    <div class="center">
      <v-text-field outlined label="제목을 입력해 주세요." type="text" id="title" v-model="article.title"> </v-text-field>
    </div>
    <div class="center">
      <v-textarea outlined label="내용을 입력해 주세요." type="text" id="contents" v-model="article.contents"> </v-textarea>
    </div>
    <div class="center">
      <v-combobox v-model="hashtagNames" :items="items" label="해시태그를 입력해 보세요." multiple chips>
        <template v-slot:selection="data">
          <v-chip color="secondary" :key="JSON.stringify(data.item)" v-bind="data.attrs" :input-value="data.selected" :disabled="data.disabled" @click:close="data.parent.selectItem(data.item)">
            <v-avatar class="primary white--text" left v-text="data.item.slice(0, 1).toUpperCase()"></v-avatar>
            {{ data.item }}
          </v-chip>
        </template>
      </v-combobox>
    </div>

    <br />
    <div>
      <br />
      <div class="lefty">
        <span>사진 </span>
        <form encType="multipart/form-data">
          <input ref="imageInput" type="file" accept="image/*" hidden @change="onChangeImages" multiple />
        </form>
        <v-btn class="ma-2" fab small light type="button" style="top: -15px; z-index: 2" @click="onClickImageUpload">
          <v-icon dark>mdi-plus</v-icon>
        </v-btn>
      </div>
      <div>
        <v-carousel class="picture-size" v-if="imgs.length != 0">
          <v-carousel-item class="picture-size" v-for="(img, idx) in imgs" :key="idx" :src="img" append reverse-transition="fade-transition" transition="fade-transition" multiple="true">
            <button @click="removeImage(idx)" class="deleteButton">X</button>
          </v-carousel-item>
        </v-carousel>
      </div>
    </div>
    <br />
    <!-- <div>
      방문 정보 입력
      <br />
      <DatePicker v-if="article.visitDate !== ''" :setDate="article.visitDate" @setDate="selectDate" label="날짜를 입력해 주세요."></DatePicker>
    </div> -->
    <div>
      <DatePicker label="날짜를 입력해 주세요." @setDate="selectDate"> </DatePicker>
    </div>
    <div class="center">
      <v-rating v-model="article.evaluation" background-color="grey lighten-1" color="primary" half-increments length="5" size="45"></v-rating>
    </div>
    <div class="center">
      <v-checkbox id="privateToggle" v-model="article.private" label="비공개 글로 합니다"></v-checkbox>
    </div>
    <div class="center">
      <button class="uploadbutton" @click="updatePost()">수정</button>
    </div>
    <div style="height:100px"></div>
    <Navigation />
  </div>
</template>

<script>
// import { mdiCalendarRange } from '@mdi/js';
import SelectPosition from '@/components/map/SelectPosition.vue';
import constants from '@/lib/constants';

import CreateArticleNav from './CreateArticleNav';
import DatePicker from './DatePicker';

import { updateArticle, getArticle } from '@/api/article.js';
import { getUserHashtags } from '@/api/user.js';

import jwt_decode from 'jwt-decode';
import Navigation from '@/components/Navigation.vue';

export default {
  name: 'UpdateArticle',
  components: {
    SelectPosition,
    CreateArticleNav,
    DatePicker,
    Navigation,
  },
  data() {
    return {
      isCurrentMap: false,
      // TODO: 랜더링타이밍
      items: [],
      positionObj: {
        positionLat: this.$route.params.article.positionLat,
        positionLng: this.$route.params.article.positionLng,
        address: this.$route.params.article.address,
      },
      // positionObj: null,
      imageUrl: null,
      imageUrls: Array,
      selectedFile: null,
      hashtagNames: [],
      // hash: '',
      // hashs: Array,
      imgs: [],
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
        private: false,
      },
      articleHashtagNames: [],
      selectedHashtagNames: [],
      images: [
        // {
        //   src: 'https://cdn.vuetifyjs.com/images/cards/cooking.png',
        // },
        // {
        //   src: 'https://cdn.vuetifyjs.com/images/carousel/squirrel.jpg',
        // },
        // {
        //   src: 'https://cdn.vuetifyjs.com/images/carousel/sky.jpg',
        // },
        // {
        //   src: 'https://cdn.vuetifyjs.com/images/carousel/bird.jpg',
        // },
      ],
      hashtagSearchKeyword: '',
    };
  },
  methods: {
    // TODO: 준혁아 고마워 ㅠㅠㅠ
    // testKeydown() {
    //   let tmpArray = [];
    //   alert(this.hashtagSearchKeyword);
    //   this.hashtagNames.push(this.hashtagSearchKeyword);
    //   this.selectedHashtagNames.push(this.hashtagSearchKeyword);

    //   for (let i = 0; i < this.selectedHashtagNames.length; ++i) {
    //     tmpArray.push(this.selectedHashtagNames[i]);
    //   }

    //   this.selectedHashtagNames = tmpArray;

    //   this.hashtagSearchKeyword = '';
    //   console.log(this.selectedHashtagNames, '들어갔죠?');
    // },
    selectDate(e) {
      this.article.visitDate = e;
      console.log(e, '에밋받았어요');
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

    updatePost() {
      // console.log(this.article.images[0])
      // var params = new URLSearchParams();
      // params.append('file', this.images);
      // params.append('article', this.article);
      this.article.hashtags = [];

      for (let i = 0; i < this.selectedHashtagNames.length; ++i) {
        // if (this.articleHashtagNames.includes(this.selectedHashtagNames[i])) {
        //   continue;
        // }
        // console.log(this.selectedHashtagNames[i], '와 안돼누 ㅠㅠㅠ');
        let obj = { hashtagNo: 0, hashtagName: this.selectedHashtagNames[i] };
        // console.log(obj, '와 안돼누 ㅠㅠㅠ obj.ver');
        this.article.hashtags.push(obj);
      }

      console.log(this.article.hashtags, '넣기전 해시태그!@!~!@~@~!#!~#~!#');
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
      // this.article.visitDate = this.date;
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
    getArticle(
      this.$route.params.articleNo,
      (response) => {
        if (response.data.status) {
          this.article = response.data.object;
          // this.article.positionObj = response.data.object.positionObj;
          // this.positionObj.positionLat = this.article.positionLat;
          // this.positionObj.positionLng = this.article.positionLng;
          // this.positionObj.address = this.article.address;
          // this.items = this.article.hashtags;
          // console.log(this.positionObj, '랜더링 ㅠㅠㅠㅠㅠㅠㅠㅠ');
          this.positionObj = this.article.positionObj;

          console.log(this.article.visitDate, '프롭스해줄 비짓데이트');
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
          console.log(this.hashtagNames, '해시태그네임');
          console.log(this.items, '아이템즈');
          // alert('해시태그 받기 성공');
        } else {
          alert('해시태그 받기 실패');
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
.center {
  display: flex;
  justify-content: center;
}

.inputbox {
  border: solid black 2px;
  width: 400px;
}

.picture-size {
  width: 100%;
  max-width: 400px;
  border: 1px solid white;
  display: block;
  margin-left: auto;
  margin-right: auto;
}

.lefty {
  display: flex;
  justify-content: left;
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
  width: 50px;
  height: 50px;
  border: solid black 0.1px;
  border-radius: 5px;
  margin-bottom: 13px;
}

.uploadbutton {
  width: 200px;
  height: 50px;
  margin: 0 auto;
  background-color: #ff70bc;
  color: white;
  font-weight: bold;
  border-radius: 10px;
  margin-top: 10px;
}

.uploadbutton:hover {
  box-shadow: 0 2px 4px rgba(216, 37, 136, 0.9);
  transform: translateY(1px);
}

.uploadbutton:focus {
  outline: 0px;
}

.deleteButton {
  background-color: red;
  width: 25px;
  height: 25px;
  float: right;
  border: solid black 1px;
}
</style>
