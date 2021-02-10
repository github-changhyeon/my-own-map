<template>
  <v-app>
    <v-card 
      min-height="700"
      class="mx-auto" 
      style="width:800px;"  
    >
      <!-- <Main /> -->
      <router-view> </router-view>
      <!-- <Navigation /> -->
    </v-card>
  </v-app>
</template>

<script>
// import Main from './views/home/Main.vue';
// import Navigation from '@/components/Navigation.vue';

export default {
  name: 'App',

  components: {
    // Main,
    // Navigation,
  },
  mounted() {},
  methods: {},
  data: () => ({
    articleNo: '',
    positionObj: null,
  }),
};
</script>
