<template>
  <div></div>
</template>

<script>
export default {
  name: 'Agreement',
  data() {
    return {
      token: '',
      tokenData: '',
    };
  },
  methods: {},
  created() {
    this.token = this.$route.query.token;
    localStorage.setItem('jwt', this.token);

    //this.$router.push('/');
  },
};
</script>
