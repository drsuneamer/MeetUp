import { useState, useEffect } from 'react';
import axios from 'axios';
import Alert from '@mui/material/Alert';
import { useNavigate } from 'react-router-dom';
import { KeyboardEvent } from 'react';

function LoginForm() {
  const navigate = useNavigate();

  const [id, setID] = useState('');
  const [pw, setPW] = useState('');
  const [login, setLogin] = useState({ id: '', password: '' });
  const [checked, setChecked] = useState(false); // 개인정보동의 체크 여부 확인
  const [alert, setAlert] = useState(false); // 개인정보제공 미동의 시 alert
  const [load, setLoad] = useState(false); // 로그인 버튼 클릭 시 로딩 alert
  const [error, setError] = useState(false);

  const notYet = () => {
    // 개인정보동의 미동의 상태로 로그인 버튼 누른 경우(alert 유발)
    setAlert(true);
  };

  const toggleCheck = () => {
    // 개인정보동의 버튼이 체크되었는지 확인
    setChecked(!checked); // 체크 시 버튼 디자인 바뀜(disable-active)
  };

  const onChangeID = (e: React.ChangeEvent<HTMLInputElement>) => {
    setID(e.target.value);
  };

  const onChangePW = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPW(e.target.value);
  };

  useEffect(() => {
    setLogin({ id: id, password: pw });
  }, [id, pw]); // id와 pw값이 변경될때마다 제출용 object에 반영

  const onKeyPress = (e: KeyboardEvent<HTMLInputElement>): void => {
    if (e.key === 'Enter') {
      onSubmit();
    }
  };
  // 로그인 API 연결
  const onSubmit = async () => {
    await axios.post('http://localhost:8080/user/login', login).then((res) => {
      console.log(res);
      if (res.status === 200) {
        // 로그인 완료 시 localstorage에 accesstoken, nickname 저장 후 메인('/') 이동
        window.localStorage.setItem('accessToken', res.data.tokenDto.accessToken);
        window.localStorage.setItem('nickname', res.data.nickname);
        navigate('/');
      } else {
        setError(true);
        console.log(error);
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
          placeholder="Mattermost ID"
          className="w-xs text-center placeholder-placeholder border-b-2 border-b-title py-1 px-2 mb-8 focus:outline-none focus:border-b-footer"
        />
        <input
          onKeyDown={onKeyPress}
          onChange={onChangePW}
          type="password"
          placeholder="Mattermost PW"
          className="w-xs text-center placeholder-placeholder border-b-2 border-b-title py-1 px-2 mb-10 focus:outline-none focus:border-b-footer"
        />
        <div className="flex items-center mb-3">
          <label className="mr-2 ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">개인정보(닉네임) 제공 동의 </label>
          <input
            onKeyDown={onKeyPress}
            onClick={toggleCheck}
            id="default-checkbox"
            type="checkbox"
            value=""
            className="w-4 h-4 text-blue-600 bg-gray-100 rounded border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
          />
        </div>
        {checked ? ( // 개인정보 동의 체크 시에만 버튼 활성화
          <button onClick={onSubmit} className="bg-title rounded drop-shadow-shadow w-xs h-s text-background text-m hover:bg-hover">
            매터모스트로 로그인
          </button>
        ) : (
          // 비활성화된 버튼 - 클릭시 아래 alert
          <button onClick={notYet} className="bg-footer rounded drop-shadow-shadow w-xs h-s text-label text-m">
            매터모스트로 로그인
          </button>
        )}
        {alert && load === false ? (
          <Alert severity="error" className="mt-10">
            개인정보 수집 미동의시 서비스 이용이 불가합니다
          </Alert>
        ) : (
          ''
        )}
        {load ? (
          <Alert severity="info" className="mt-10 text-[13px]">
            첫 로그인의 경우 데이터 동기화 시간이 소요됩니다 (최대 2분)
          </Alert>
        ) : (
          ''
        )}
        {error ? (
          <Alert severity="error" className="mt-10">
            아이디(비밀번호)를 잘못 입력하였습니다.
          </Alert>
        ) : (
          ''
        )}
      </div>
      {/* 타이틀 */}
      <div className="bg-title w-[420px] relative rounded-r-login cursor-default">
        <div className="absolute top-[162px] left-[55px] font-damion text-background text-2xl">MeetUp</div>
        <div className="absolute top-[228px] left-[55px] font-damion text-background text-2xl scale-y-[-1] opacity-10">MeetUp</div>
      </div>
    </div>
  );
}

export default LoginForm;
