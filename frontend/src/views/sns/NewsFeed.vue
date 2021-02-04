<template>
  <div>
    <v-card>
      <v-tabs centered style="position: fixed; top: 0px; z-index: 2">
        <v-tab style="width: 50vw">전체</v-tab>
        <v-tab style="width: 50vw">팔로우</v-tab>
      </v-tabs>
    </v-card>
    <AllNewsFeed style="padding-top: 70px" />
  </div>
</template>
<script>
import AllNewsFeed from '@/components/sns/AllNewsFeed';

export default {
  name: 'NewsFeed',
  created() {},
  components: {
    AllNewsFeed,
  },
  data() {
    return {};
  },
  methods: {},
};
</script>

<style></style>
