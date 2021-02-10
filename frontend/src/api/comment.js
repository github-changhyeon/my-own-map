import { createInstance } from './index.js';

const instance = createInstance();

function getComment(articleNo, success, fail) {
  instance
    .get(`/comments/${articleNo}`)
    .then(success)
    .catch(fail);
}
  
function createComment(comment, success, fail) {
  instance
    .post(`/comments`, comment)
    .then(success)
    .catch(fail);
}
  
function updateComment(comment, config, success, fail) {
  instance
    .put(`/comments`, comment, config)
    .then(success)
    .catch(fail);
}

function deleteComment(commentNo, success, fail) {
  instance
    .delete(`/comments/${commentNo}`)
    .then(success)
    .catch(fail);
}

export { getComment, deleteComment, createComment, updateComment, };