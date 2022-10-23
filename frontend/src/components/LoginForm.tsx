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
    <div className="flex h-[80vh] w-[80vw]">
      {/* 입력창 */}
      <div className="bg-background flex flex-col items-center justify-center rounded-l-lg">
        <input
          onChange={onChangeID}
          type="text"
          placeholder="Mattermost ID"
          className="w-3/5 text-center border-b-2 border-b-title py-1 px-2 focus:outline-none"
        />
        <input
          onChange={onChangePW}
          type="password"
          placeholder="Mattermost PW"
          className="w-3/5 text-center border-b-2 border-b-title py-1 px-2 focus:outline-none"
        />
        <button onClick={onSubmit} className="bg-title rounded drop-shadow w-s h-s text-background">
          매터모스트로 로그인
        </button>
      </div>
      {/* 타이틀 */}
      <div className="bg-title grid place-items-center rounded-r-lg">
        <div className="font-damion text-background text-2xl">MeetUp</div>
      </div>
    </div>
  );
}

export default LoginForm;
