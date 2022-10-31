import axios from 'axios';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function AdminLoginForm() {
  const navigate = useNavigate();
  const [id, setID] = useState('');
  const [pw, setPW] = useState('');
  const [login, setLogin] = useState({ id: '', password: '' });

  const onChangeID = (e: React.ChangeEvent<HTMLInputElement>) => {
    setID(e.target.value);
  };

  const onChangePW = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPW(e.target.value);
  };

  useEffect(() => {
    setLogin({ id: id, password: pw });
  }, [id, pw]);

  const onSubmit = async () => {
    await axios.post('http://localhost:8080/admin/login', login).then((res) => {
      console.log(res);
      if (res.status === 200) {
        // 로그인 완료 시 localstorage에 accesstoken, nickname 저장 후 메인('/') 이동
        window.localStorage.setItem('accessToken', res.data.accessToken);
        navigate('/admin');
      }
    });
  };
  return (
    // 전체
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
        <button onClick={onSubmit} className="bg-title rounded drop-shadow-shadow w-xs h-s text-background text-m mt-10 hover:bg-hover">
          관리자 로그인
        </button>
      </div>
      {/* 타이틀 */}
      <div className="bg-title w-[420px] relative rounded-r-login cursor-default">
        <div className="absolute top-[162px] left-[265px] font-damion text-background text-[43px]">Meetup</div>
        <div className="absolute top-[175px] left-[30px] font-damion text-background text-[75px]">Maintenance</div>
        <div className="absolute top-[218px] left-[30px] font-damion text-background text-[75px] scale-y-[-1] opacity-10">Maintenance</div>
      </div>
    </div>
  );
}

export default AdminLoginForm;
