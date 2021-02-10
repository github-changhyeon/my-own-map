<template>
  <div>
    <v-card>
      <v-tabs centered style="position: fixed; top: 0px; z-index: 2">
        <v-tab @click="isAll = true" style="width: 50vw">전체 게시글 보기</v-tab>
        <!-- <v-tab @click="isAll = false" style="width: 50vw">팔로우</v-tab> -->
      </v-tabs>
    </v-card>
    <AllNewsFeed v-if="isAll" style="padding-top: 70px; margin-bottom:50px;" />
    <!-- <FollowNewsFeed v-if="!isAll" style="padding-top: 70px; margin-bottom:50px;" /> -->
    <Navigation />
  </div>
</template>
<script>
import AllNewsFeed from '@/components/sns/AllNewsFeed';
// import FollowNewsFeed from '@/components/sns/FollowNewsFeed';
import Navigation from '@/components/Navigation.vue';

export default {
  name: 'NewsFeed',
  created() {},
  components: {
    AllNewsFeed,
    // FollowNewsFeed,
    Navigation,
  },
  data() {
    return {
      isAll: true,
    };
  },
  methods: {},
};
</script>

<style></style>
