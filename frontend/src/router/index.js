import Vue from 'vue';
import VueRouter from 'vue-router';
import constants from '../lib/constants';
import Main from '../views/home/Main.vue';
import ListView from '../views/home/ListView.vue';
import Login from '../views/user/Login.vue';
import Agreement from '../views/user/Agreement';
// Article
import ArticleDetail from '@/views/article/ArticleDetail';
import CreateArticle from '@/views/article/CreateArticle';
// import SelectPosition from '@/views/article/SelectPosition';
import UpdateArticle from '@/views/article/UpdateArticle';

// User
import MyPage from '@/views/user/MyPage';
import Join from '@/views/user/Join';
import Follow from '@/components/user/Follow';
import ChangeInfo from '@/components/user/ChangeInfo';
import History from '@/views/user/History';

// SNS
import NewsFeed from '@/views/sns/NewsFeed';
import KakaoSharing from '@/components/sns/KakaoSharing';

Vue.use(VueRouter);

const routes = [
  {
    path: '/maps/:uid',
    // name: 'Main',
    name: constants.URL_TYPE.HOME.MAIN,
    component: Main,
    props: true,
  },
  {
    path: '/listView',
    // name: 'Main',
    name: constants.URL_TYPE.HOME.FILTEREDLIST,
    component: ListView,
    props: true,
  },
  {
    path: '/history',
    // name: 'Main',
    name: constants.URL_TYPE.USER.HISTORY,
    component: History,
    props: true,
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

  // {
  //   path: '/articles/selectPosition',
  //   name: constants.URL_TYPE.ARTICLE.SELECTPOSITION,
  //   component: SelectPosition,
  // },
  {
    path: '/articles/:articleNo',
    name: constants.URL_TYPE.ARTICLE.ARTICLEDETAIL,
    component: ArticleDetail,
    props: true,
  },
  {
    path: '/users/:uid',
    component: MyPage,
    name: constants.URL_TYPE.USER.MYPAGE,
    props: true,
  },
  {
    path: '/articles/:articleNo/update',
    name: constants.URL_TYPE.ARTICLE.UPDATEARTICLE,
    component: UpdateArticle,
    props: true,
  },
  {
    path: '/',
    name: constants.URL_TYPE.USER.LOGIN,
    component: Login,
    props: true,
  },
  {
    path: '/agreement',
    name: Agreement,
    component: Agreement,
  },
  {
    path: '/join',
    name: constants.URL_TYPE.USER.JOIN,
    component: Join,
    props: true,
  },
  {
    path: '/newsfeed',
    name: constants.URL_TYPE.SNS.NEWSFEED,
    component: NewsFeed,
    props: true,
  },
  {
    path: '/share',
    name: 'KakaoSharing',
    component: KakaoSharing,
    props: true,
  },
  {
    path: '/followlist',
    name: 'Follow',
    component: Follow,
    props: true,
  },
  {
    path: '/changeinfo',
    name: 'ChangeInfo',
    component: ChangeInfo,
    props: true,
  },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

export default router;
