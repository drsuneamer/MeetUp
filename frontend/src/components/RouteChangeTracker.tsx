import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import ReactGA from 'react-ga';

const RouteChangeTracker = () => {
  const location = useLocation();
  const [initialized, setInitialized] = useState(false);
  const profile = process.env.REACT_APP_PROFILE;

  useEffect(() => {
    if (profile !== 'dev') {
      ReactGA.initialize('G-WTBJTG44BB');
    }
    // if (!window.location.href.includes('localhost')) {
    //   ReactGA.initialize('G-WTBJTG44BB');
    // }
    setInitialized(true);
  }, []);

  useEffect(() => {
    if (initialized) {
      ReactGA.pageview(location.pathname + location.search);
    }
  }, [initialized, location]);
};

export default RouteChangeTracker;
