import axios from 'axios';
import { Link } from 'react-router-dom';

// const baseURL = process.env.REACT_APP_LOCAL_URL; => 왜안되지
const baseURL = 'http://localhost:8080';
const accessToken = window.localStorage.getItem('accessToken');

// axios를 모듈화하여 이후 baseURL, header를 입력하지 않고 사용 가능
export const axiosInstance = axios.create({
  baseURL,
  headers: {
    Authorization: `Bearer ${accessToken}`,
  },
});

// 인터셉터: axios 요청시마다 로그인 만료 여부 확인
axiosInstance.interceptors.request.use((config): any => {
  const tokenExpiresIn = Number(localStorage.getItem('tokenExpiresIn'));

  const today = new Date();
  const parsedToday = today.getTime();
  const isExpired = tokenExpiresIn - parsedToday < 0;

  if (!config.headers) {
    config.headers = {};
    return config.headers;
  }
  if (accessToken && !isExpired) {
    config.headers.Authorization = `Bearer ${accessToken}`;
    return config;
  }
  if (isExpired) {
    <Link to="/" />;
  }
});
