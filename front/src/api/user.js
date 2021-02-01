import { createInstance } from './index.js';

const instance = createInstance();

function login(user, success, fail) {
  // { email: user.email, password: user.password }
  instance.post('/users/login', user).then(success).catch(fail);
}

export { login };
