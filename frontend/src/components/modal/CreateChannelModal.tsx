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
          <div className="mt-[20px]">
            <div className="text-title font-bold">공개여부</div>
            <FormControl>
                <FormLabel id="demo-row-radio-buttons-group-label"></FormLabel>
                <RadioGroup
                aria-labelledby="demo-row-radio-buttons-group-label"
                name="row-radio-buttons-group"
                >
                <div className="flex items-center">
                    <FormControlLabel value="public" control={<Radio />} label="공개" />
                    <span className="text-placeholder">누구나 이 채널에 참여할 수 있습니다</span>
                </div>
                <div className="flex items-center">
                    <FormControlLabel value="private" control={<Radio />} label="비공개" />
                    <span className="text-placeholder">초대받은 사람만 이 채널에 참여할 수 있습니다</span>
                </div>
                </RadioGroup>
            </FormControl>
          </div>
          <div className="mt-[20px]">
            <div className="text-title font-bold">채널이름</div>
            <input type="text" name="title" className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
          </div>
          <div className="mt-[20px]">
            <div className="text-title font-bold">채널 URL</div>
            <input type="text" name="title" className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
          </div>
          <div className="mt-[20px]">
          </div>
          <button className="font-bold bg-title hover:bg-hover text-background mt-[50px] rounded w-[450px] h-s drop-shadow-button">밋업 등록하기</button>
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