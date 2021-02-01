<template>
  <v-sheet class="mx-auto" max-width="700">
    <v-slide-group multiple show-arrows>
      <v-slide-item v-for="(n, idx) in 5" :key="idx" v-slot="{ active, toggle }">
        <!-- <v-slide-item
        v-for="(hash, idx) in hashs"
        :key="idx"
        v-slot="{ active, toggle }"
      > -->
        <v-btn class="mx-2" :input-value="active" active-class="purple white--text" depressed rounded @click="toggle">
          {{ n }}
          <!-- {{ hash }} -->
        </v-btn>
      </v-slide-item>
    </v-slide-group>
  </v-sheet>
</template>

<script>
import axios from 'axios';

export default {
  data: () => ({
    model: null,
    hashs: [],
  }),
  methods: {
    getHash() {
      axios
        .get('url')
        .then((res) => {
          this.hashs = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>
