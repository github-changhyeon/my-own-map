import Vue from 'vue';
import App from './App.vue';
import router from './router';
import vuetify from './plugins/vuetify';
import VeeValidate from 'vee-validate';

Vue.use(vuetify);

Vue.config.productionTip = false;

new Vue({
  router,
  vuetify,
  VeeValidate,
  render: (h) => h(App),
}).$mount('#app');
