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

export { login, join, getUserInfo };
