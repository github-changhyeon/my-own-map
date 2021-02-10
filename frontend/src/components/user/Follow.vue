<template>
  <div>
    <div>
      <div v-for="(user, idx) in users" :key="idx">
        <v-avatar>
          <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="" />
        </v-avatar>
        hi<span>{{ user.username }} </span>
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
    return {};
  },
  created() {
    console.log(this.users, 'follow.vue의 콘솔');
  },
};
</script>

<style></style>
