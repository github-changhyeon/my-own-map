import { createInstance } from './index.js';

const instance = createInstance();

function getFollowingUsers(uid, success, fail) {
  // const body = {
  //   email: user.email,
  //   password: user.password,
  instance
    .get(`follow/findFollowing/${uid}`)
    .then(success)
    .catch(fail);
}

export { getFollowingUsers };
