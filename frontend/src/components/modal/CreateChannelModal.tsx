import  { PropsWithChildren }from 'react';
import * as React from 'react';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';

interface ModalDefaultType {
  onClickToggleModal: () => void;  // 함수 타입 정의
}

function CreateChannelModal({
  onClickToggleModal,
}: PropsWithChildren<ModalDefaultType>) {
  return (
    <div className="w-[100%] h-[100%] fixed flex justify-center items-center">
      <div className="w-[600px] h-[600px] flex flex-col items-center bg-background z-10 rounded drop-shadow-shadow">
        <svg onClick={onClickToggleModal} xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" className="w-6 h-6 stroke-title mt-[15px] ml-[550px] cursor-pointer">
  <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
</svg>
        <div>
          <p className="text-placeholder text-[15px]">mm에 새로운 팀 채널 생성하기</p>
          <div className="mt-[35px]">
            <div className="text-title font-bold text-[20px] mb-[10px]">공개여부<span className="text-cancel">&#42;</span></div>
                <div className="flex items-center mb-[5px]">
                    <input id="default-radio-1" type="radio" value="public" name="default-radio" className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:blue-500" />
                    <label className="ml-2 font-bold text-[16px]">공개</label>
                    <span className="ml-[12px] text-placeholder text-[14px]">누구나 이 채널에 참여할 수 있습니다</span>
                </div>
                <div className="flex items-center">
                    <input checked id="default-radio-2" type="radio" value="private" name="default-radio" className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:blue-500" />
                    <label className="ml-2 font-bold text-[16px]">비공개</label>
                    <span className="ml-[12px] text-placeholder text-[14px]">초대받은 사람만 이 채널에 참여할 수 있습니다</span>
                </div>
          </div>
          <div className="mt-[35px]">
            <div className="text-title font-bold text-[20px]">채널이름<span className="text-cancel">&#42;</span></div>
            <input type="text" name="title" className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point text-[16px]"/>
          </div>
          <div className="mt-[35px]">
            <div className="text-title font-bold text-[20px]">채널 URL<span className="text-cancel">&#42;</span></div>
            <input type="text" name="title" className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point text-[16px]"/>
          </div>
          {/* <div className="mt-[20px]">
            <div className="text-title font-bold">팀원 초대하기</div>
          </div> */}
          <button className="font-bold bg-title hover:bg-hover text-background mt-[70px] rounded w-[450px] h-s drop-shadow-button">팀 채널 생성하기</button>
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
export default CreateChannelModal;