import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ColorPicker from 'react-pick-color';
import Layout from '../components/layout/Layout';
import Spinner from '../components/common/Spinner';
import MultipleLevelSelection from '../components/MultipleLevelSelection';
import { useSelector } from 'react-redux';

import { axiosInstance } from '../components/auth/axiosConfig';

interface Category {
  id: string;
  teamId?: string;
  displayName: string;
}

function CreateChannel() {
  const t = useSelector((state: any) => state.teamId.value).id;
  const navigate = useNavigate();

  const [lv1Categories, setLv1] = useState<any>([]);
  const [lv2Categories, setLv2] = useState<any>([]);

  useEffect(() => {
    axiosInstance.get('meetup/team').then((res) => {
      setLv1(res.data);
    });
  }, []);

  if (lv1Categories.length > 0) {
    if (lv1Categories[0].teamId === undefined) {
      for (let i = 0; i < lv1Categories.length; i++) {
        lv1Categories[i].teamId = '0';
      }
    }
  }

  useEffect(() => {
    axiosInstance.get(`/meetup/channel/${t}`).then((res) => {
      setLv2(res.data);
    });
  }, [t]);

  // 알림을 받을 채널 선택하기
  const [category, setCategory] = useState<Category>();
  // console.log(category); // 보내줘야 할 값 (저장하기 클릭 시)
  const [channelId, setChannelId] = useState('');

  useEffect(() => {
    if (category !== undefined) {
      setChannelId(category.id);
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

  const onSubmit = async () => {
    await axiosInstance.post('/meetup/', record).then((res) => {
      if (res.status === 201) {
        navigate('/');
      }
    });
  };

  if (lv1Categories.length > 0) {
    return (
      <Layout>
        <div className="text-m mx-[20vw] pt-[20vh] pb-[180px]">
          <div className="mb-10">
            <div className="font-bold text-title cursor-default">알림을 받을 채널 선택하기</div>
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
            <div className="font-bold text-title cursor-default">MM 채널 이름 (변경 가능)</div>
            <div className="flex justify-center">
              <input
                onChange={titleChange}
                type="text"
                placeholder="ex. 서울_1반_A102"
                className="w-full text-center placeholder-placeholder border-b-2 border-b-title py-1 px-2 mb-10 placeholder:text-s focus:outline-none focus:border-b-footer"
              />
            </div>
          </div>

          <div className="z-30">
            <div className="font-bold text-title cursor-default">달력에 표시할 색상 선택</div>
            <div className="flex mt-2 mb-12">
              {/* 색상 선택 */}
              <div
                onClick={openColor}
                style={{ backgroundColor: `${color}` }}
                className="rounded-full m-[5px] w-[30px] h-[30px] cursor-pointer"
              ></div>
              {open ? (
                <div onClick={openColor} className="absolute z-30">
                  <ColorPicker color={color} onChange={(color) => setColor(color.hex)} />
                </div>
              ) : (
                <div></div>
              )}
              <div onClick={openColor} className="text-xs text-label pt-[9px] ml-1.5 cursor-pointer">
                {color}
              </div>
            </div>
          </div>

          <button onClick={onSubmit} className="z-30 bg-title rounded drop-shadow-shadow text-background font-medium w-full h-s my-2 hover:bg-hover">
            저장하기
          </button>
        </div>
      </Layout>
    );
  } else {
    return <Spinner />;
  }
}

export default CreateChannel;
