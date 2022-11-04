import { useEffect, useState } from 'react';
import ColorPicker from 'react-pick-color';
import Layout from '../components/layout/Layout';
import { useSelector } from 'react-redux';
import { axiosInstance } from '../components/auth/axiosConfig';
import Spinner from '../components/common/Spinner';
import { useNavigate } from 'react-router-dom';

interface Channel {
  id: number;
  title: string;
  teamName: string;
  channelName: string;
  color: string;
}

function EditChannel() {
  const navigate = useNavigate();
  const [channel, setChannel] = useState<Channel>();

  const channelId: number = useSelector((state: any) => state.channelId.value);

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
  console.log(title, color);

  // 등록 (API 연결)
  const [record, setRecord] = useState({ title: title, color: '' });
  //   setRecord({ title: title, color: color });
  // }, [title, color]); // 각 값이 변경될 때마다 제출될 record에 반영

  useEffect(() => {
    axiosInstance.get(`/meetup/${channelId}`).then((res) => {
      setChannel(res.data);
      setColor(res.data.color);
      setTitle(res.data.title);
      setRecord({ title: res.data.title, color: res.data.color });
    });
  }, [channelId]);

  useEffect(() => {
    setRecord({ title: title, color: color });
  }, [title, color]);

  const onDelete = () => {
    axiosInstance.delete(`/meetup/${channelId}`).then((res) => {
      navigate(`/calendar/${localStorage.getItem('id')}`);
    });
  };

  const onSubmit = () => {
    console.log(title, color);
    console.log(record);
    axiosInstance.put(`/meetup/${channelId}`, record).then((res) => {
      console.log(res);
      if (res.status === 201) {
        navigate(`/calendar/${localStorage.getItem('id')}`);
      }
    });
  };

  if (channel) {
    return (
      <Layout>
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
          <div className="flex justify-between">
            {' '}
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
