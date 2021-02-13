import { createInstance, createMultipartInstance } from './index.js';

const instance = createInstance();
const multipartInstance = createMultipartInstance();

function login(user, success, fail) {
  // { email: user.email, password: user.password }
  instance
    .post('/users/login', user)
    .then(success)
    .catch(fail);
}

function join(user, success, fail) {
  // { email: user.email, password: user.password }
  instance
    .post('/users/join', user)
    .then(success)
    .catch(fail);
}

function getUserInfo(uid, success, fail) {
  instance.defaults.headers['jwt'] = window.localStorage.getItem('jwt');
  // const body = {
  //   email: user.email,
  //   password: user.password,
  instance
    .get(`/users/findByUid/${uid}`)
    .then(success)
    .catch(fail);
}

function deleteUser(success, fail) {
  instance.defaults.headers['jwt'] = window.localStorage.getItem('jwt');
  // const body = {
  //   email: user.email,
  //   password: user.password,
  instance
    .delete(`/users`)
    .then(success)
    .catch(fail);
}

function updateUser(formData, success, fail) {
  multipartInstance.defaults.headers['jwt'] = window.localStorage.getItem(
    'jwt'
  );
  // const body = {
  //   email: user.email,
  //   password: user.password,
  multipartInstance
    .put(`/users`, formData)
    .then(success)
    .catch(fail);
}

function getArticles(uid, success, fail) {
  instance
    .get(`/users/${uid}/articles`)
    .then(success)
    .catch(fail);
}

function getPrivateArticles(uid, success, fail) {
  instance.defaults.headers['jwt'] = window.localStorage.getItem('jwt');
  instance
    .get(`/users/${uid}/privateArticles`)
    .then(success)
    .catch(fail);
}

function getPublicArticles(uid, success, fail) {
  instance
    .get(`/users/${uid}/publicArticles`)
    .then(success)
    .catch(fail);
}

function getRecentArticles(uid, success, fail) {
  instance.defaults.headers['jwt'] = window.localStorage.getItem('jwt');
  instance
    .get(`/users/${uid}/recentArticles`)
    .then(success)
    .catch(fail);
}

function getRecentPublicArticles(uid, success, fail) {
  instance
    .get(`/users/${uid}/recentPublicArticles`)
    .then(success)
    .catch(fail);
}

function getUserHashtags(uid, success, fail) {
  instance
    .get(`/users/${uid}/userHashtags`)
    .then(success)
    .catch(fail);
}

function getUserPublicHashtags(uid, success, fail) {
  instance
    .get(`/users/${uid}/userPublicHashtags`)
    .then(success)
    .catch(fail);
}

function doFollow(uid, config, success, fail) {
  instance
    .get(`/follow/doFollow/${uid}`, config)
    .then(success)
    .catch(fail);
}

function findFollowing(uid, success, fail) {
  instance
    .get(`/follow/findFollowing/${uid}`)
    .then(success)
    .catch(fail);
}

function findFollower(uid, success, fail) {
  instance
    .get(`/follow/findFollower/${uid}`)
    .then(success)
    .catch(fail);
}

function isFollow(uid, config, success, fail) {
  instance
    .get(`/follow/isFollow/${uid}`, config)
    .then(success)
    .catch(fail);
}

function doFavorite(articleNo, config, success, fail) {
  instance
    .get(`/favorite/doFavorite/${articleNo}`, config)
    .then(success)
    .catch(fail);
}

function isFavorite(articleNo, config, success, fail) {
  instance
    .get(`/favorite/isFavorite/${articleNo}`, config)
    .then(success)
    .catch(fail);
}

function myFavorite(success, fail) {
  instance.defaults.headers['jwt'] = window.localStorage.getItem('jwt');
  instance
    .get(`/favorite/myFavorite`)
    .then(success)
    .catch(fail);
}

export {
  login,
  join,
  getUserInfo,
  getArticles,
  getRecentArticles,
  getUserHashtags,
  getUserPublicHashtags,
  doFollow,
  findFollower,
  findFollowing,
  isFollow,
  getPrivateArticles,
  getPublicArticles,
  getRecentPublicArticles,
  doFavorite,
  isFavorite,
  myFavorite,
  deleteUser,
  updateUser,
};
