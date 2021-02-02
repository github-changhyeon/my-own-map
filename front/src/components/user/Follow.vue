<template>
  <div>
    <div>
      <span>
        <v-icon>mdi-account-plus</v-icon>
      </span>
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
      required: true,
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
