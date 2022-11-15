import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import ColorPicker from 'react-pick-color';
import Layout from '../components/layout/Layout';
import Spinner from '../components/common/Spinner';
import MultipleLevelSelection from '../components/MultipleLevelSelection';
import Alert from '@mui/material/Alert';

import { axiosInstance } from '../components/auth/axiosConfig';

interface Category {
  id: string;
  teamId?: string;
  displayName: string;
}

function CreateMeetup() {
  const navigate = useNavigate();

  const [lv1Categories, setLv1] = useState<any>([]);
  const [lv2Categories, setLv2] = useState<any>([]);

  // 전체 팀, 채널 목록 가져오기
  useEffect(() => {
    axiosInstance.get('/meetup/team').then((res) => {
      setLv1(res.data);
    });

    axiosInstance.get(`/meetup/channel`).then((res) => {
      setLv2(res.data);
    });
  }, []);

  if (lv1Categories.length > 0) {
    if (lv1Categories[0].teamId === undefined) {
      for (let i = 0; i < lv1Categories.length; i++) {
        lv1Categories[i].teamId = '0';
      }
    }
  }

  // 알림을 받을 채널 선택하기
  const [category, setCategory] = useState<Category>();
  const [channelId, setChannelId] = useState('');

  useEffect(() => {
    if (category !== undefined) {
      setChannelId(category.id);
      setTitle(category.displayName);
    }
  }, [category]);

  const categories: Category[] = [...lv1Categories, ...lv2Categories];
  const categoriesByteamId = (teamId: string | number) => categories.filter((category) => category.teamId === `${teamId}`);

  // MM 채널 이름 설정
  const [title, setTitle] = useState('');
  const titleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTitle(e.target.value);
  };

  // 달력에 표시할 색상 선택
  const [color, setColor] = useState('#ED8383');
  const [open, setOpen] = useState(false);

  const openColor = () => {
    setOpen(!open);
  };

  // 등록 (API 연결)
  const [record, setRecord] = useState({ channelId: '', title: '', color: '' });
  useEffect(() => {
    setRecord({ channelId: channelId, title: title, color: color });
  }, [channelId, title, color]); // 각 값이 변경될 때마다 제출될 record에 반영

  const [again, setAgain] = useState(false);

  const onSubmit = () => {
    axiosInstance
      .post('/meetup/', record)
      .then((res) => {
        if (res.status === 201) {
          navigate(`/calendar/${localStorage.getItem('id')}`);
        }
      })
      .catch((err) => {
        if (err.response.data.errorCode === '40906') {
          setAgain(true);
        }
      });
  };

  useEffect(() => {
    // 중복 오류 안내 -> 3초 뒤에 사라짐
    const timer = setTimeout(() => {
      setAgain(false);
    }, 3000);

    return () => {
      clearTimeout(timer);
    };
  }, [again]);

  if (lv1Categories.length > 0) {
    return (
      <Layout>
        <div className="text-m mx-[20vw] pt-[15vh] pb-[180px]">
          <div className="mb-10">
            <div className="flex justify-between">
              <div className="font-bold text-title cursor-default">알림을 받을 채널 선택하기</div>
              <Link to="/create-channel">
                <div className="flex items-center mr-1 mt-1 drop-shadow-shadow cursor-pointer">
                  <svg
                    xmlns="https://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth="1.5"
                    stroke="currentColor"
                    className="w-6 h-4"
                  >
                    <path d="M13.5 16.875h3.375m0 0h3.375m-3.375 0V13.5m0 3.375v3.375M6 10.5h2.25a2.25 2.25 0 002.25-2.25V6a2.25 2.25 0 00-2.25-2.25H6A2.25 2.25 0 003.75 6v2.25A2.25 2.25 0 006 10.5zm0 9.75h2.25A2.25 2.25 0 0010.5 18v-2.25a2.25 2.25 0 00-2.25-2.25H6a2.25 2.25 0 00-2.25 2.25V18A2.25 2.25 0 006 20.25zm9.75-9.75H18a2.25 2.25 0 002.25-2.25V6A2.25 2.25 0 0018 3.75h-2.25A2.25 2.25 0 0013.5 6v2.25a2.25 2.25 0 002.25 2.25z" />
                  </svg>
                  <div className="text-xs">새로운 Mattermost 채널 생성하기</div>
                </div>
              </Link>
            </div>
            <div className="relative">
              <MultipleLevelSelection
                initialItems={categoriesByteamId(0)}
                getItemKey={(item) => item.id}
                getItemLabel={(item) => item.displayName}
                getNestedItems={(item) => categoriesByteamId(item.id)}
                hasNestedItems={(_, level) => level < 2}
                isEqual={(item, item2) => item?.id === item2?.id}
                placeholder="내 MM의 모든 팀/채널 확인하기"
                onChange={setCategory}
              />
            </div>
          </div>

          <div className="mb-5">
            <div className="font-bold text-title cursor-default">밋업 이름 (변경 가능)</div>
            <div className="flex justify-center">
              <input
                defaultValue={category?.displayName}
                onChange={titleChange}
                type="text"
                placeholder="ex. 서울_1반_A102"
                className="w-full text-center placeholder-placeholder border-b-2 border-b-title py-1 px-2 mb-10 placeholder:text-s focus:outline-none focus:border-b-footer"
              />
            </div>
          </div>

          <div className="z-30">
            <div className="font-bold text-title cursor-default">달력에 표시할 색상</div>
            <div className="flex mt-2 mb-12">
              {/* 색상 선택 */}
              <div
                onClick={openColor}
                style={{ backgroundColor: `${color}` }}
                className="rounded-full m-[5px] w-[30px] h-[30px] cursor-pointer"
              ></div>
              {open ? (
                <div className="flex absolute z-30 items-start">
                  <ColorPicker color={color} onChange={(color) => setColor(color.hex)} />
                  <button onClick={openColor} className="ml-1 bg-primary text-background px-2 text-s rounded">
                    선택
                  </button>
                </div>
              ) : (
                <div></div>
              )}
              <div onClick={openColor} className="text-xs text-label pt-[9px] ml-1.5 cursor-pointer">
                {color}
              </div>
            </div>
          </div>

          <div className="relative">
            {again ? (
              <Alert severity="error" className="absolute w-full bottom-[11vh]">
                해당 채널로 생성된 밋업이 이미 존재합니다!
              </Alert>
            ) : (
              ''
            )}
            <button
              onClick={onSubmit}
              className="z-30 mt-7 bg-title rounded drop-shadow-shadow text-background font-medium w-full h-s my-2 hover:bg-hover"
            >
              밋업 생성하기
            </button>
          </div>
        </div>
      </Layout>
    );
  } else {
    return (
      <div className="pt-[30vh]">
        <Spinner />
      </div>
    );
  }
}

export default CreateMeetup;
