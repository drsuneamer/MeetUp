import { useEffect, useState } from 'react';
import axios from 'axios';
// import {  useNavigate } from 'react-router-dom';
import ColorPicker from 'react-pick-color';
import Layout from '../components/layout/Layout';
import MultipleLevelSelection from '../components/MultipleLevelSelection';

interface Category {
  categoryId: string;
  parentId: string;
  name: string;
}

function CreateChannel() {
  // const navigate = useNavigate();

  const getChannel = async () => {
    await axios
      .get('http://localhost:8080/api/meetup/sync', {
        headers: {
          Authorization: `Bearer ${window.localStorage.getItem('accessToken')}`,
        },
      })
      .then((res) => {
        console.log(res);
      });
  };

  useEffect(() => {
    getChannel();
  });

  // 알림을 받을 채널 선택하기
  const [category, setCategory] = useState<Category>();
  // console.log(category); // 보내줘야 할 값 (저장하기 클릭 시)
  const [channelId, setChannelId] = useState('');

  useEffect(() => {
    if (category !== undefined) {
      setChannelId(category.categoryId);
    }
  }, [category]);

  const lv1Categories = [
    //팀
    { categoryId: '1', name: '1반', parentId: '0' },
    { categoryId: '2', name: '2반', parentId: '0' },
    { categoryId: '3', name: '3반', parentId: '0' },
    { categoryId: '4', name: '4반', parentId: '0' },
    { categoryId: '9', name: '9반', parentId: '0' },
  ];

  const lv2Categories = [
    //채널 (parentId에 따라 팀에 종속됨)
    { categoryId: '1', name: '101', parentId: '1' },
    { categoryId: '2', name: '201', parentId: '1' },
    { categoryId: '3', name: '202', parentId: '2' },
    { categoryId: '4', name: '203', parentId: '2' },
    { categoryId: '5', name: '204', parentId: '2' },
    { categoryId: '6', name: '205', parentId: '2' },
    { categoryId: '7', name: '206', parentId: '2' },
    { categoryId: '8', name: '207', parentId: '2' },
    { categoryId: '9', name: '208', parentId: '2' },
  ];

  const categories: Category[] = [...lv1Categories, ...lv2Categories];
  const categoriesByParentId = (parentId: string | number) => categories.filter((category) => category.parentId === `${parentId}`);

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

  return (
    <Layout>
      <div className="text-m mx-[20vw] pt-[20vh] pb-[180px]">
        <div className="mb-10 z-30">
          <div className="font-bold text-title">알림을 받을 채널 선택하기</div>
          <div className="">
            <MultipleLevelSelection
              initialItems={categoriesByParentId(0)}
              getItemKey={(item) => item.categoryId}
              getItemLabel={(item) => item.name}
              getNestedItems={(item) => categoriesByParentId(item.categoryId)}
              hasNestedItems={(_, level) => level < 2}
              isEqual={(item, item2) => item?.categoryId === item2?.categoryId}
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
            <div onClick={openColor} style={{ backgroundColor: `${color}` }} className="rounded-full m-[5px] w-[30px] h-[30px] cursor-pointer"></div>
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
}

export default CreateChannel;
