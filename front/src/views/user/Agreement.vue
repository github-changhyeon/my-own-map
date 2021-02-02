<template>
  <div></div>
</template>

<script>
export default {
  name: 'Agreement',
  data() {
    return {
      token: '',
    };
  },
  methods: {},
  created() {
    this.token = this.$route.query.token;
    // console.log(this.access_token, this.$route.query.nickName);
    console.log(this.$route.query);
    localStorage.setItem('jwt', this.token);
    this.$router.push('/');
  },
};
</script>
