import { useEffect, useState } from 'react';
import ColorPicker from 'react-pick-color';
import Layout from '../components/layout/Layout';
import { useAppSelector, useAppDispatch } from '../stores/ConfigHooks';
import { axiosInstance } from '../components/auth/axiosConfig';
import Spinner from '../components/common/Spinner';
import { useNavigate } from 'react-router-dom';
import { setDeleteModalOpen } from '../stores/modules/modal';

interface Channel {
  id: number;
  title: string;
  teamName: string;
  channelName: string;
  color: string;
}

function EditChannel() {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const [channel, setChannel] = useState<Channel>();

  const channelId: number = useAppSelector((state: any) => state.channelInfo.value.id);

  // MM 채널 이름 수정
  const [title, setTitle] = useState('');
  const titleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTitle(e.target.value);
  };

  // 달력에 표시할 색상 선택
  const [color, setColor] = useState('');
  const [open, setOpen] = useState(false);

  const openColor = () => {
    setOpen(!open);
  };

  // 등록 (API 연결)
  const [record, setRecord] = useState({ title: title, color: '' });
  useEffect(() => {
    axiosInstance.get(`/meetup/${channelId}`).then((res) => {
      setChannel(res.data);
      setColor(res.data.color);
      setTitle(res.data.title);
      setRecord({ title: res.data.title, color: res.data.color });
    });
  }, [channelId]);

  // 이름이나 색 변경 시 제출 레코드에 반영
  useEffect(() => {
    setRecord({ title: title, color: color });
  }, [title, color]);

  // 삭제 버튼 클릭 시 삭제 모달 출력
  const onDelete = () => {
    dispatch(setDeleteModalOpen(['delete', 'meetup']));
  };

  // 삭제 완료 시 메인(캘린더) 화면으로 이동
  const onSubmit = () => {
    axiosInstance.put(`/meetup/${channelId}`, record).then((res) => {
      if (res.status === 201) {
        navigate(`/calendar/${localStorage.getItem('id')}`);
      }
    });
  };

  if (channel) {
    return (
      <Layout>
        <div className="z-50"></div>

        <div className="text-m mx-[20vw] pt-[20vh] pb-[180px]">
          <div className="mb-10 z-30">
            <div className="font-bold text-title">알림을 받을 채널 선택하기</div>
            <div className="relative flex bg-title rounded items-center justify-center drop-shadow-shadow text-background text-s font-medium w-full h-s my-2 cursor-pointer">
              {channel.teamName} / {channel.channelName}
            </div>
          </div>

          <div className="mb-5">
            <div className="font-bold text-title">MM 채널 이름 (변경 가능)</div>
            <div className="flex justify-center">
              <input
                onChange={titleChange}
                type="text"
                value={title}
                placeholder={channel.title}
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
          <div className="flex justify-between">
            {/* 저장/삭제 버튼 */}
            <button
              onClick={onSubmit}
              className="relative z-2 bg-title rounded drop-shadow-shadow text-background font-medium w-[28vw] h-s my-2 hover:bg-hover"
            >
              저장하기
            </button>
            <button
              onClick={onDelete}
              className="relative z-2 rounded border-2 bg-background border-cancel drop-shadow-shadow text-cancel font-medium w-[28vw] h-s my-2 hover:bg-cancel hover:text-background"
            >
              삭제하기
            </button>
          </div>
        </div>
      </Layout>
    );
  } else {
    return <Spinner />;
  }
}

export default EditChannel;
