import { createInstance } from './index.js';

const instance = createInstance();

function getFollowingUsers(uid, success, fail) {
  // const body = {
  //   email: user.email,
  //   password: user.password,
  instance
    .get(`follow/findFollower/${uid}`)
    .then(success)
    .catch(fail);
}

export { getFollowingUsers };
