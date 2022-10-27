import  { PropsWithChildren }from 'react';
import * as React from 'react';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';

interface ModalDefaultType {
  onClickToggleModal: () => void;  // 함수 타입 정의
}

interface ChannelOptionType {
  title: string;
}

const channels = [
  { title: '서울_1반_팀장채널'},
  { title: 'A101' },
  { title: 'A102' },
  { title: 'A103' },
  { title: 'A104' },
  { title: 'A105' },
  { title: 'A106' },
  { title: 'A107' },
  { title: 'A102_scrum' },
  { title: 'A102_jira_bot' },
];
function Modal({
  onClickToggleModal,
}: PropsWithChildren<ModalDefaultType>) {
  const defaultProps = {
    options: channels,
    getOptionLabel: (option: ChannelOptionType) => option.title,
  };
  const flatProps = {
    options: channels.map((option) => option.title),
  };
  const [value, setValue] = React.useState<ChannelOptionType | null>(null);
  // const ELEMENT_TO_SHOW = 4;
  return (
    <div className="w-[100%] h-[100%] fixed flex justify-center items-center">
      <div className="w-[600px] h-[600px] flex flex-col items-center bg-background z-10 rounded drop-shadow-shadow">
        <svg onClick={onClickToggleModal} xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" className="w-6 h-6 stroke-title mt-[15px] ml-[550px] cursor-pointer">
  <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
</svg>
        <div>
          <div className="mt-[20px]">
            <div className="text-title font-bold">미팅명</div>
            <input type="text" name="title" className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
          </div>
          <div className="mt-[20px]">
            <div className="text-title font-bold">날짜</div>
            <input type="text" name="title" className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
          </div>
          <div className="mt-[20px]">
            <div className="text-title font-bold">시간</div>
            <input type="text" name="title" className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
          </div>
          <div className="mt-[20px]">
            <div className="text-title font-bold">내용</div>
            <input type="text" name="title" className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
          </div>
          <div className="mt-[20px]">
            <div className="text-title font-bold">알림 보낼 채널</div>
            <Autocomplete
              className="w-[450px]"
              ListboxProps={{ style: { maxHeight: '150px' } }}
              {...defaultProps}
              id="select-channel"
              // options={channels.filter((el, i) => {  // here add a filter for options
              //   if (i < ELEMENT_TO_SHOW) return el;
              // })}
              renderInput={(params) => (
                <TextField {...params} label="채널 선택하기" variant="standard" />
              )}
            />
          </div>
          {/* <div>
            <span>팀원 초대하기</span>
                 
          </div> */}
          <button className="bg-title hover:bg-hover text-background mt-[50px] rounded w-[450px] h-s">밋업 등록하기</button>
        </div>
        </div>
      <div
        className='w-[100%] h-[100%] fixed top:0 z-9 bg-[rgba(0,0,0,0.45)]'
        onClick={(e: React.MouseEvent) => {
          e.preventDefault();

          if (onClickToggleModal) {
            onClickToggleModal();
          }
        }}
      />
    </div>
  );
}
export default Modal;