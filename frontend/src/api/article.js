import { createInstance, createMultipartInstance } from './index.js';

const instance = createInstance();
const multipartInstance = createMultipartInstance();

// function login(user, success, fail) {
//   instance.defaults.headers['access-token'] = window.localStorage.getItem('access-token');
//   const body = {
//     email: user.email,
//     password: user.password,
//   };

//   instance
//     .post('/account/login', JSON.stringify(body))
//     .then(success)
//     .catch(fail);
// }

// async function findByEmail(email, success, fail) {
//   instance.defaults.headers['access-token'] = window.localStorage.getItem('access-token');
//   await instance
//     .get(`/account/info/${email}`)
//     .then(success)
//     .catch(fail);
// }

function getArticle(articleNo, success, fail) {
  instance
    .get(`articles/${articleNo}`)
    .then(success)
    .catch(fail);
}

function getAllArticles(success, fail) {
  instance
    .get(`/articles`)
    .then(success)
    .catch(fail);
}

function getPublicArticles(uid, success, fail) {
  instance
    .get(`/users/${uid}/publicArticles`)
    .then(success)
    .catch(fail);
}

function getPrivateArticles(uid, success, fail) {
  instance
    .get(`/users/${uid}/privateArticles`)
    .then(success)
    .catch(fail);
}

function getFavoriteArticles(uid, success, fail) {
  instance
    .get(`/users/${uid}/favoriteArticles`)
    .then(success)
    .catch(fail);
}


function createArticle(article, success, fail) {
  // let userId = 1;
  multipartInstance
    .post(`/articles`, article)
    .then(success)
    .catch(fail);
}

function deleteArticle(articleNo, success, fail) {
  instance
    .delete(`articles/${articleNo}`)
    .then(success)
    .catch(fail);
}

function updateArticle(article, success, fail) {
  instance
    .post(`articles/${article.articleNo}`, article)
    .then(success)
    .catch(fail);
}

export { getArticle, deleteArticle, getAllArticles, getPublicArticles, getPrivateArticles, getFavoriteArticles, updateArticle, createArticle };
