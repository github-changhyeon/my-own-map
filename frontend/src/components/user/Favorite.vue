<template>
  <div>
    <v-icon v-if="!isLiked" @click="checkLiked">mdi-favorite</v-icon>
    <v-icon v-if="isLiked" @click="checkLiked">mdi-favorite-border</v-icon>
  </div>
</template>

<script>
export default {
  name: 'Favorite',
  data() {
    return {
      isLiked: false,
    };
  },
  methods: {
    checkLiked() {
      // doLike(
      //   this.uid,
      //   config,
      //   (response) => {
      //     console.log(response);
      //   },
      //   (error) => {
      //     console.log(error);
      //   }
      // );
    },
  },
};
</script>

<style></style>
