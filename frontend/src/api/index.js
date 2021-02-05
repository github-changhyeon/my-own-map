import axios from 'axios';
// import { API_BASE_URL } from '../config';

function createInstance() {
  const instance = axios.create({
    baseURL: 'https://i4b107.p.ssafy.io:8080',
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return instance;
}
function createMultipartInstance() {
  const instance = axios.create({
    baseURL: 'https://i4b107.p.ssafy.io:8080',
    headers: {
      // 'Content-Type': false,
      // 'Content-Type': 'multipart/form-data',
    },
  });
  return instance;
}

export { createInstance, createMultipartInstance };
