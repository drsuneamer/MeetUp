import React from 'react';
import { Outlet, Navigate } from 'react-router-dom';

const PrivateRoutes = () => {
  //   const accessToken = localStorage.getItem('accessToken');
  const tokenExpiresIn = Number(localStorage.getItem('tokenExpiresIn'));

  const today = new Date();
  const parsedToday = today.getTime();
  const isExpired = tokenExpiresIn - parsedToday < 0;
  // console.log('is refresh token expired?', isExpired);

  if (isExpired) {
    return <Navigate to="/" />;
  }
  return <Outlet />;
};

export default PrivateRoutes;
