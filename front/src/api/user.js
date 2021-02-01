import { createInstance } from './index.js';

const instance = createInstance();

function login(user, success, fail) {
    instance.post("/login", { username: user.username, password: user.password })
        .then(success).catch(fail);
}
    
export { login };