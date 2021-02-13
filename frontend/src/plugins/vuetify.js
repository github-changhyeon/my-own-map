import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';

Vue.use(Vuetify);
const vuetify = new Vuetify({
  theme: {
    themes: {
      light: {
        // hot pink, 핫 핑크
        primary: '#FF70BC',
        // cotton candy, 연한 핑크
        secondary: '#FFC2E2',
        // snow, 배경색
        tertiary: 'FFFAFD',
        // salmon, 살구색
        warning: '#FA8072',
        // anchor: '#FF70BC',
      },
    },
  },
});
export default vuetify;
