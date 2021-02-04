<template>
  <v-app>
    <v-main>
      <!-- <Main /> -->
      <router-view> </router-view>
      <Navigation />
    </v-main>
  </v-app>
</template>

<script>
// import Main from './views/home/Main.vue';
import Navigation from '@/components/Navigation.vue';

export default {
  name: 'App',

  components: {
    // Main,
    Navigation,
  },
  mounted() {},
  methods: {},
  data: () => ({
    articleNo: '',
    positionObj: null,
  }),
};
</script>
