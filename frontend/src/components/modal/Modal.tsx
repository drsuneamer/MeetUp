import  { PropsWithChildren }from 'react';
import * as React from 'react';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';

interface ModalDefaultType {
  onClickToggleModal: () => void;  // 함수 타입 정의
}

const top100Films = [
  { label: 'The Shawshank Redemption' },
  { label: 'The Godfather' },
  { label: 'The Godfather: Part II' },
  { label: 'The Dark Knight' },
  { label: '12 Angry Men' },
  { label: "Schindler's List" },
  { label: 'Pulp Fiction' },
  { label: 'Aliens' },
  { label: 'Oldboy' },
  { label: 'Once Upon a Time in America' },
  { label: 'Witness for the Prosecution' },
  { label: 'Das Boot' },
  { label: 'Citizen Kane' },
  { label: 'North by Northwest' },
  { label: 'Vertigo' },
];
function Modal({
  onClickToggleModal,
}: PropsWithChildren<ModalDefaultType>) {
  return (
    <div className="w-[100%] h-[100%] fixed flex justify-center items-center">
      <div className="w-[600px] h-[600px] flex flex-col items-center bg-background z-10 rounded drop-shadow-shadow">
        <div onClick={onClickToggleModal}>x</div>
        <div className="flex flex-col">
          <div>
            <span>미팅명</span>
            <input type="text" name="title" className="w-s h-[40px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
          </div>
          <div>
            <span>날짜</span>
            <input type="text" name="title" className="w-s h-[40px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
          </div>
          <div>
            <span>시간</span>
            <input type="text" name="title" className="w-s h-[40px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
          </div>
          <div>
            <span>내용</span>
            <input type="text" name="title" className="w-s h-[40px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
          </div>
          <div>
            <span>알림 보낼 채널</span>
            <Autocomplete
              disablePortal
              id="combo-box-demo"
              options={top100Films}
              sx={{ width: 300 }}
              renderInput={(params) => <TextField {...params} label="채널 선택하기" />}
            />
          </div>
          {/* <div>
            <span>팀원 초대하기</span>
                 
          </div> */}
          <button>밋업 등록하기</button>
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