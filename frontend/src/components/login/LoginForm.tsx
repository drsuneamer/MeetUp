import { useState, useEffect } from 'react';
import axios from 'axios';
import Alert from '@mui/material/Alert';
import { useNavigate } from 'react-router-dom';
import { KeyboardEvent } from 'react';
import LoginSpinner from '../common/LoginSpinner';

function LoginForm() {
  const baseURL = process.env.REACT_APP_BASE_URL;
  const navigate = useNavigate();

  const [id, setID] = useState('');
  const [pw, setPW] = useState('');
  const [login, setLogin] = useState({ id: '', password: '' });
  const [checked, setChecked] = useState(false); // 개인정보동의 체크 여부 확인
  const [alert, setAlert] = useState(false); // 개인정보제공 미동의 시 alert
  const [load, setLoad] = useState(false); // 로그인 버튼 클릭 시 로딩 alert
  const [cert, setCert] = useState(false);
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

  const enterLogin = (e: KeyboardEvent<HTMLInputElement>): void => {
    if (e.key === 'Enter' && checked) {
      onSubmit();
    }
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      setAlert(false);
      setCert(false);
    }, 3000);

    return () => {
      clearTimeout(timer);
    };
  }, [cert, alert]);

  // 로그인 API 연결
  const onSubmit = async () => {
    setLoad(true);
    await axios
      .post(`${baseURL}/user/login`, login)
      .then((res) => {
        if (res.status === 200) {
          // 로그인 완료 시 localstorage에 accesstoken, nickname, id 저장 후 메인('/') 이동
          window.localStorage.setItem('id', res.data.id);
          window.localStorage.setItem('accessToken', res.data.tokenDto.accessToken);
          window.localStorage.setItem('tokenExpiresIn', res.data.tokenDto.tokenExpiresIn);
          window.localStorage.setItem('nickname', res.data.nickname);
          navigate(`/calendar/${window.localStorage.getItem('id')}`);
          window.location.reload();
        }
      })
      .catch((error) => {
        if (error.response.data.errorMessage === '인증 정보가 없습니다.') {
          setLoad(false);
          setCert(true);
          setChecked(false);
        } else {
          console.log(error.response);
          setLoad(false);
          setError(true);
          setChecked(false);
        }
      });
  };

  if (load) {
    return <LoginSpinner />;
  } else {
    return (
      // 전체
      <div className="flex h-[500px] w-[900px]">
        {/* 입력창 */}
        <div className="bg-background w-[480px] flex flex-col items-center justify-center rounded-l-login">
          <div className="mt-3 mb-10 flex flex-col items-center justify-center">
            <input
              onKeyDown={enterLogin}
              onChange={onChangeID}
              type="text"
              placeholder="Mattermost ID"
              className="w-xs text-center placeholder-placeholder border-b-2 border-b-title py-1 px-2 mb-8 focus:outline-none focus:border-b-footer"
            />
            <input
              onKeyDown={enterLogin}
              onChange={onChangePW}
              type="password"
              placeholder="Mattermost PW"
              className="w-xs text-center placeholder-placeholder border-b-2 border-b-title py-1 px-2 mb-10 focus:outline-none focus:border-b-footer"
            />
            <div className="flex items-center mb-3">
              <label className="mr-2 ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">개인정보(닉네임) 제공 동의 </label>
              <input
                onKeyDown={enterLogin}
                onClick={toggleCheck}
                id="default-checkbox"
                type="checkbox"
                className="w-4 h-4 text-blue-600 bg-gray-100 rounded border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
              />
            </div>

            {checked ? ( // 개인정보 동의 체크 시에만 버튼 활성화
              <button onClick={onSubmit} className="mt-5 bg-title rounded drop-shadow-shadow w-xs h-s text-background text-m hover:bg-hover">
                매터모스트로 로그인
              </button>
            ) : (
              // 비활성화된 버튼 - 클릭시 아래 alert
              <button onClick={notYet} className="mt-5 bg-footer rounded drop-shadow-shadow w-xs h-s text-label text-m">
                매터모스트로 로그인
              </button>
            )}
          </div>
          {alert ? ( // 개인정보 동의하지 않은 상태로 로그인 버튼 클릭시 - 3초
            <Alert severity="error" className="mb-4 text-[13px]">
              개인정보 수집 미동의시 서비스 이용이 불가합니다
            </Alert>
          ) : (
            ''
          )}

          {cert ? ( // axios response 에러 시 반응 - 3초
            <Alert severity="error" className="mb-4 text-[13px]">
              아이디(비밀번호)를 잘못 입력하였습니다.
            </Alert>
          ) : (
            ''
          )}

          {error ? ( // axios response 에러 (비밀번호 오류 제외) 반응 - 3초
            <Alert severity="error" className="mb-4 text-[13px]">
              오류가 발생했습니다! 잠시 후에 다시 시도해주세요 😥
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
}

export default LoginForm;
