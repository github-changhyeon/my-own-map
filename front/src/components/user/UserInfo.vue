<template>
  <div>
    User Info
    <div>
      <v-avatar>
        <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" />
      </v-avatar>
    </div>
  </div>
</template>

<script>
// import axios from 'axios';

export default {
  name: 'UserInfo',
  props: {
    profile: Object,
  },
  data() {
    return {
      myImg: '',
    };
  },
  methods: {},
  created() {},
};
</script>

<style></style>
