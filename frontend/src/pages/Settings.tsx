import Layout from '../components/layout/Layout';
import { useState, useEffect } from 'react';
import { axiosInstance } from '../components/auth/axiosConfig';
import Alert from '@mui/material/Alert';

function Settings() {
  const [url, setUrl] = useState('Webex Link');
  const [input, setInput] = useState({ webexUrl: '' });
  const [done, setDone] = useState(false);

  useEffect(() => {
    axiosInstance.get('/user/webex').then((res) => {
      if (res.status === 200) {
        if (res.data.webexUrl === null) {
          setUrl('Webex Link');
        } else {
          setUrl(res.data.webexUrl);
        }
      }
    });
  }, []);

  const onInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUrl(e.target.value);
  };

  useEffect(() => {
    setInput({ webexUrl: url });
  }, [url]);

  const submit = () => {
    axiosInstance.put('/user/webex', input).then((res) => {
      if (res.status === 201) {
        setDone(true);
      }
    });
  };

  return (
    <Layout>
      <div className="text-m mx-[20vw] pt-[20vh] pb-[180px]">
        <div>
          <div className="flex items-center">
            <div className="font-bold text-title">나의 Webex 주소 관리</div>
            <button
              onClick={submit}
              className="ml-3 w-[80px] h-[23px] bg-background border-title border-solid border-[2px] text-[13px] drop-shadow-shadow rounded font-bold text-title align-middle"
            >
              저장
            </button>
          </div>
          <input
            onKeyDown={(e) => {
              if (e.key === 'Enter') {
                submit();
              }
            }}
            onChange={onInput}
            type="text"
            placeholder={url}
            className="w-full text-center placeholder-label border-b-2 border-b-title py-1 px-2 mb-8 focus:outline-none focus:border-b-footer"
          />
        </div>
        {done ? <Alert severity="success">저장이 완료되었습니다.</Alert> : ''}
      </div>
    </Layout>
  );
}

export default Settings;
