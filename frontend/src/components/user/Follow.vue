<template>
  <div>
    <div>
      <div v-for="(user, idx) in follow" :key="idx">
        <v-avatar>
          <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="" />
        </v-avatar>
        <span>{{ user.username }} </span>
        <br />
        <!-- <span>{{ user.email }} </span> -->
      </div>
    </div>
  </div>
</template>

<script>
// import axios from 'axios'

export default {
  name: 'Follow',
  props: {
    users: {
      type: Array,
      // required: true,
    },
    remove: {
      type: Function,
      // required: true,
    },
  },
  data() {
    return {
      follow: null,
    };
  },
  created() {
    console.log(this.users, 'follow.vue의 콘솔');
    console.log(this.$route.params.follow);
    this.follow = this.$route.params.follow;
  },
};
</script>

<style></style>
