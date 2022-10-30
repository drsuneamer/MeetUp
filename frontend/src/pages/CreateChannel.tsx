import { useEffect, useState } from 'react';
import axios from 'axios';
// import {  useNavigate } from 'react-router-dom';
import ColorPicker from 'react-pick-color';
import Layout from '../components/layout/Layout';
import MultipleLevelSelection from '../components/MultipleLevelSelection';
import { useSelector } from 'react-redux';

interface Category {
  id: string;
  teamId?: string;
  displayName: string;
}

function CreateChannel() {
  const t = useSelector((state: any) => state.teamNum.value).id;
  // const navigate = useNavigate();

  const [lv1Categories, setLv1] = useState<any>([]);
  const [lv2Categories, setLv2] = useState<any>([]);

  useEffect(() => {
    axios
      .get('http://localhost:8080/api/meetup/team', {
        headers: {
          Authorization: `Bearer ${window.localStorage.getItem('accessToken')}`,
        },
      })
      .then((res) => {
        setLv1(res.data);
        console.log(res.data);
      });
  }, []);
  console.log(lv1Categories);

  if (lv1Categories.length > 0) {
    if (lv1Categories[0].teamId === undefined) {
      for (let i = 0; i < lv1Categories.length; i++) {
        lv1Categories[i].teamId = '0';
      }
    }
  }

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/meetup/channel/${t}`, {
        headers: {
          Authorization: `Bearer ${window.localStorage.getItem('accessToken')}`,
        },
      })
      .then((res) => {
        console.log(res);
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

  // const lv2Categories = [
  //   //채널 (teamId에 따라 팀에 종속됨)
  //   { id: '1', displayName: '101', teamId: '84s3g1pdttg1bjjo1s5fhrdf1c' },
  //   { id: '2', displayName: '201', teamId: '84s3g1pdttg1bjjo1s5fhrdf1c' },
  //   { id: '3', displayName: '202', teamId: '84s3g1pdttg1bjjo1s5fhrdf1c' },
  //   { id: '4', displayName: '203', teamId: '84s3g1pdttg1bjjo1s5fhrdf1c' },
  //   { id: '5', displayName: '204', teamId: 'hgpfx9pqxiyij8zt9qz9tk8yga' },
  //   { id: '6', displayName: '205', teamId: 'hgpfx9pqxiyij8zt9qz9tk8yga' },
  //   { id: '7', displayName: '206', teamId: 'hgpfx9pqxiyij8zt9qz9tk8yga' },
  //   { id: '8', displayName: '207', teamId: 'hgpfx9pqxiyij8zt9qz9tk8yga' },
  //   { id: '9', displayName: '208', teamId: 'hgpfx9pqxiyij8zt9qz9tk8yga' },
  // ];

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

  const onSubmit = () => {
    console.log(record);
  };

  // const onSubmit = async () => {
  //   await axios
  //     .post('http://localhost:8080/api/?', record, {
  //       headers: {
  //         Authorization: `Bearer ${window.localStorage.getItem('accessToken')}`,
  //       },
  //     })
  //     .then((res) => {
  //       console.log(res);
  //     });
  // };
  if (lv1Categories.length > 0) {
    return (
      <Layout>
        <div className="text-m mx-[20vw] pt-[20vh] pb-[180px]">
          <div className="mb-10 z-30">
            <div className="font-bold text-title">알림을 받을 채널 선택하기</div>
            <div className="">
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
            <div className="font-bold text-title">MM 채널 이름 (변경 가능)</div>
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
            <div className="font-bold text-title">달력에 표시할 색상 선택</div>
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

          <button
            onClick={onSubmit}
            className="relative z-2 bg-title rounded drop-shadow-shadow text-background font-medium w-full h-s my-2 hover:bg-hover"
          >
            저장하기
          </button>
        </div>
      </Layout>
    );
  } else {
    return <div>Loading...</div>;
  }
}

export default CreateChannel;
