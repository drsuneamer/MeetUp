import axios from 'axios';

const baseURL = 'http://localhost:8080';
// const baseURL = process.env.REACT_APP_LOCAL_URL; => 왜안되지

const accessToken = window.localStorage.getItem('accessToken');

// axios를 모듈화하여 이후 baseURL, header를 입력하지 않고 사용 가능
const axiosInstance = axios.create({
  baseURL,
  headers: {
    Authorization: `Bearer ${accessToken}`,
  },
});

export default axiosInstance;
