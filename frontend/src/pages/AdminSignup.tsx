import { useState, useEffect } from 'react';
import axios from 'axios';

import { useNavigate } from 'react-router-dom';

function AdminSignup() {
  const navigate = useNavigate();

  const [id, setID] = useState('');
  const [pw, setPW] = useState('');
  const [key, setKey] = useState('');
  const [signUp, setSignUp] = useState({ id: '', password: '', key: '' });

  const onChangeID = (e: React.ChangeEvent<HTMLInputElement>) => {
    setID(e.target.value);
  };

  const onChangePW = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPW(e.target.value);
  };

  useEffect(() => {
    setSignUp({ id: id, password: pw, key: key });
  }, [id, pw, key]); // id와 pw값이 변경될때마다 제출용 object에 반영

  const onChangeKey = (e: React.ChangeEvent<HTMLInputElement>) => {
    setKey(e.target.value);
  };

  const onSubmit = async (e: React.MouseEvent<HTMLButtonElement>) => {
    await axios.post('https://meet-up.co.kr/api/admin/signup', signUp).then((res) => {
      console.log(signUp);
      console.log(res);
      if (res.status === 201) {
        // 로그인 완료 시 localstorage에 accesstoken, nickname 저장 후 메인('/') 이동
        navigate('/admin-login');
      }
    });
  };

  return (
    <div className="h-screen bg-footer flex items-center justify-center">
      <div className="flex h-[500px] w-[900px]">
        {/* 입력창 */}
        <div className="bg-background w-[480px] flex flex-col items-center justify-center rounded-l-login">
          <input
            onChange={onChangeID}
            type="text"
            placeholder="ID"
            className="w-xs text-center placeholder-placeholder border-b-2 border-b-title py-1 px-2 mb-8 focus:outline-none focus:border-b-footer"
          />
          <input
            onChange={onChangePW}
            type="password"
            placeholder="PW"
            className="w-xs text-center placeholder-placeholder border-b-2 border-b-title py-1 px-2 mb-10 focus:outline-none focus:border-b-footer"
          />
          <input
            onChange={onChangeKey}
            type="password"
            placeholder="Key"
            className="w-xs text-center placeholder-placeholder border-b-2 border-b-title py-1 px-2 mb-10 focus:outline-none focus:border-b-footer"
          />
          <button onClick={onSubmit} className="bg-title rounded drop-shadow-shadow w-xs h-s text-background text-m mt-10 hover:bg-hover">
            관리자 회원가입
          </button>
        </div>
        {/* 타이틀 */}
        <div className="bg-title w-[420px] relative rounded-r-login cursor-default">
          <div className="absolute top-[162px] left-[265px] font-damion text-background text-[43px]">Sign Up</div>
          <div className="absolute top-[175px] left-[30px] font-damion text-background text-[75px]">Maintenance</div>
          <div className="absolute top-[218px] left-[30px] font-damion text-background text-[75px] scale-y-[-1] opacity-10">Maintenance</div>
        </div>
      </div>
    </div>
  );
}

export default AdminSignup;
