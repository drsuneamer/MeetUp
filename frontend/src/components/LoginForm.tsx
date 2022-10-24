import { useState } from 'react';

function LoginForm() {
  const [id, setID] = useState('');
  const [pw, setPW] = useState('');

  const onChangeID = (e: React.ChangeEvent<HTMLInputElement>) => {
    setID(e.target.value);
  };

  const onChangePW = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPW(e.target.value);
  };

  const onSubmit = (e: React.MouseEvent<HTMLButtonElement>) => {
    const login: object = { idValue: id, pwValue: pw };
    console.log(login);
  };
  return (
    // 전체
    <div className="flex h-[500px] w-[900px]">
      {/* 입력창 */}
      <div className="bg-background w-[480px] flex flex-col items-center justify-center rounded-l-login">
        <input
          onChange={onChangeID}
          type="text"
          placeholder="Mattermost ID"
          className="w-xs text-center border-b-2 border-b-title py-1 px-2 mb-8 focus:outline-none placeholder-title"
        />
        <input
          onChange={onChangePW}
          type="password"
          placeholder="Mattermost PW"
          className="w-xs text-center border-b-2 border-b-title py-1 px-2 mb-10 focus:outline-none placeholder-title"
        />
        <button onClick={onSubmit} className="bg-title rounded drop-shadow-shadow w-xs h-s text-background text-m mt-10">
          매터모스트로 로그인
        </button>
      </div>
      {/* 타이틀 */}
      <div className="bg-title w-[420px] relative rounded-r-login">
        <div className="absolute top-[162px] left-[55px] font-damion text-background text-2xl">MeetUp</div>
        <div className="absolute top-[228px] left-[55px] font-damion text-background text-2xl scale-y-[-1] opacity-10">MeetUp</div>
      </div>
    </div>
  );
}

export default LoginForm;
