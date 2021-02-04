<template>
  <div>
    <div>
      <div v-for="(user, idx) in users" :key="idx">
        <span>{{ user.username }} </span>
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
      required: true,
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
    // axios.get()
  },
};
</script>

<style></style>
