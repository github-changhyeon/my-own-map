import { createInstance } from './index.js';

const instance = createInstance();

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
    .get(`/users/${uid}`)
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

export {
  login,
  join,
  getUserInfo,
  getArticles,
  getRecentArticles,
  getUserHashtags,
  doFollow,
  findFollower,
  findFollowing,
  isFollow,
  getPrivateArticles,
  getPublicArticles,
  getRecentPublicArticles,
};
