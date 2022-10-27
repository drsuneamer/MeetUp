import LoginForm from '../components/LoginForm';
import Image1 from '../assets/background1.png';
import Image2 from '../assets/background2.png';
import Image3 from '../assets/background3.png';
import Image4 from '../assets/background4.png';
import { useState, useEffect, useMemo } from 'react';

function Login() {
  // Image[] not working
  const imgArray: any = useMemo(() => [Image1, Image2, Image3, Image4], []);
  const [showImg, setShowImg] = useState();

  useEffect(() => {
    const imgNum: number = Math.round(Math.random() * 3);
    setShowImg(imgArray[imgNum]);
  }, [imgArray]);

  return (
    <div className="h-screen bg-cover" style={{ backgroundImage: `url(${showImg})` }}>
      <div className="bg-title bg-opacity-30 w-screen h-screen flex items-center justify-center">
        <LoginForm />
      </div>
    </div>
  );
}

export default Login;
