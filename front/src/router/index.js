import Vue from 'vue';
import VueRouter from 'vue-router';
import constants from '../lib/constants';
import Main from '../views/home/Main.vue';
import Login from '../views/user/Login.vue';
import Agreement from '../views/user/Agreement';
// Article
import ArticleDetail from '@/views/article/ArticleDetail';
import CreateArticle from '@/views/article/CreateArticle';
import SelectPosition from '@/views/article/SelectPosition';
import UpdateArticle from '@/views/article/UpdateArticle';

// User
import MyPage from '@/views/user/MyPage';
Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    // name: 'Main',
    name: constants.URL_TYPE.HOME.MAIN,
    component: Main,
  },
  // {
  //   path: '/about',
  //   name: 'About',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  // }
  {
    path: '/articles/create',
    name: constants.URL_TYPE.ARTICLE.CREATEARTICLE,
    component: CreateArticle,
  },

  {
    path: '/articles/selectPosition',
    name: constants.URL_TYPE.ARTICLE.SELECTPOSITION,
    component: SelectPosition,
  },
  {
    path: '/articles/:articleNo',
    name: constants.URL_TYPE.ARTICLE.ARTICLEDETAIL,
    component: ArticleDetail,
    props: true,
  },
  {
    path: '/users/:uid',
    name: constants.URL_TYPE.USER.MYPAGE,
    component: MyPage,
    props: true,
  },
  {
    path: '/articles/:articleNo/update',
    name: constants.URL_TYPE.ARTICLE.UPDATEARTICLE,
    component: UpdateArticle,
    props: true,
  },
  {
    path: '/users/login',
    name: 'login',
    component: Login,
  },
  {
    path: '/agreement',
    name: Agreement,
    component: Agreement
  },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

export default router;