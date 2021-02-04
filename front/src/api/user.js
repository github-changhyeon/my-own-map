import { createInstance } from './index.js';

const instance = createInstance();

function login(user, success, fail) {
  // { email: user.email, password: user.password }
  instance.post('/users/login', user).then(success).catch(fail);
}

function join(user, success, fail) {
  // { email: user.email, password: user.password }

  let body = {
    email: user.email,
    password: user.password,
    username: user.username,
  };
  instance.post('/users/join', JSON.stringify(body)).then(success).catch(fail);
}

function getUserInfo(uid, success, fail) {
  instance.defaults.headers['jwt'] = window.localStorage.getItem('jwt');
  // const body = {
  //   email: user.email,
  //   password: user.password,
  instance.get(`/users/${uid}`).then(success).catch(fail);
}

function getArticles(uid, success, fail) {
  instance.get(`/users/${uid}/articles`).then(success).catch(fail);
}

function getRecentArticles(uid, success, fail) {
  instance.get(`/users/${uid}/recentArticles`).then(success).catch(fail);
}

function getUserHashtags(uid, success, fail) {
  instance.get(`/users/${uid}/userHashtags`).then(success).catch(fail);
}

export { login, join, getUserInfo, getArticles, getRecentArticles, getUserHashtags };
