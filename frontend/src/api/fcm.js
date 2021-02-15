import { createInstance } from './index.js';

const instance = createInstance();

function notifyAction(body, success, fail) {
  instance.defaults.headers['jwt'] = window.localStorage.getItem('jwt');

  instance
    .post(`/fcm/push`, body)
    .then(success)
    .catch(fail);
}

function registFcmToken(token, success, fail) {
  instance.defaults.headers['jwt'] = window.localStorage.getItem('jwt');

  instance
    .post(`/fcm/register`, token)
    .then(success)
    .catch(fail);
}

function deleteFcmToken(success, fail) {
  instance.defaults.headers['jwt'] = window.localStorage.getItem('jwt');

  instance
    .delete(`/fcm/logout`)
    .then(success)
    .catch(fail);
}

function getHistory(success, fail) {
  instance.defaults.headers['jwt'] = window.localStorage.getItem('jwt');

  instance
    .get(`/fcm/history`)
    .then(success)
    .catch(fail);
}

function setZero(success, fail) {
  instance.defaults.headers['jwt'] = window.localStorage.getItem('jwt');

  instance
    .post(`/fcm/makeZero`)
    .then(success)
    .catch(fail);
}

export { notifyAction, registFcmToken, deleteFcmToken, getHistory, setZero };
