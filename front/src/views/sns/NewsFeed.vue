<template>
  <v-app id="inspire">
      <v-container>
        <v-row no-gutters>
          <v-col
            v-for="(article, idx) in articles"
            :key="idx"
            md="6"
            offset-md="3"
          >
            <v-card
            height="200"
            class="mt-10"
            outlined
            tile            
            >
            <div>
            작성자: {{ article.user }}
            </div>
            <div>
            rating: {{ article.rating}}
            </div>
            <div>
            {{ article.img }}
            </div>
            <div>
            해쉬태그
            </div>
            <div>
            작성일자
            </div>
            <div>
            주소
            </div>
            <div>
            title
            </div>
            <div>
            내용
            </div>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
  </v-app>
</template>
<script>

export default {
  name: 'NewsFeed',
  data() {
    return {
      articles: [
        {
          user: 1,
          rating: 5,
          img: 'https://cdn.vuetifyjs.com/images/cards/cooking.png
        },
        {
          user: 2,
        },
        {
          user: 3,
        }
      ],
    }
  },
  methods: {

  }
}
</script>

<style>

</style>