<template>
  <div class="container" data-app>
    <CreateArticleNav />
    <SelectPosition @emitSelectPosition="getPos" />
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
      <v-col md="4" offset-md="4">
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
    </div>
    <br />
    <div class="inline">
      이 장소의 사진
      <br />
      <br />
      <form encType="multipart/form-data">
        <input ref="imageInput" type="file" accept="image/*" hidden @change="onChangeImages" multiple />
      </form>
      <button class="lefty picture-upload" type="button" @click="onClickImageUpload">+</button>
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
      <DatePicker label="날짜를 입력해 주세요." @setDate="selectDate"> </DatePicker>
    </div>
    <div class="center">
      <v-rating v-model="article.evaluation" background-color="grey lighten-1" color="blue" half-increments length="5" size="45"></v-rating>
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
import CreateArticleNav from './CreateArticleNav';
import DatePicker from './DatePicker';
import { createArticle } from '@/api/article.js';
import { getUserHashtags } from '@/api/user.js';

import jwt_decode from 'jwt-decode';

export default {
  name: 'CreateArticle',
  components: {
    SelectPosition,
    CreateArticleNav,
    DatePicker,
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
      imgs: [],
      images: [],
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
        uid: 0,
      },
    };
  },
  methods: {
    selectDate(e) {
      this.article.visitDate = e;
      console.log(e)
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
    createPost() {
      // console.log(this.article.images[0])
      var params = new URLSearchParams();
      params.append('file', this.images);
      params.append('article', this.article);
      for (let i = 0; i < this.hashtagNames.length; ++i) {
        let obj = { hashtagNo: 0, hashtagName: this.hashtagNames[i] };
        this.article.hashtags.push(obj);
      }

      // const imgs = new FormData();
      // sonsole.log(typeof(this.article.images))
      const formData = new FormData();
      // console.log(this.images);
      // console.log(typeof(this.images));
      // console.log(this.images[0]);
      // console.log(this.images[1]);

      this.images.forEach((image) => formData.append('file[]', image));
      // formData.append("file", this.images);
      formData.append('article', new Blob([JSON.stringify(this.article)], { type: 'application/json' }));
      // console.log("file",formData.get("file"));
      // console.log("file",formData.get("article").hashtags);
      this.article.visitDate = this.date;
      const token = localStorage.getItem('jwt');
      let uid = jwt_decode(token).uid;
      this.article.uid = uid;
      createArticle(
        formData,
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
    const token = localStorage.getItem('jwt');
    let uid = jwt_decode(token).uid;
    this.article.uid = uid;
    getUserHashtags(
      uid,
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
</style>
