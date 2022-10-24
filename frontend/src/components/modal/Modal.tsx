import React, { useState, PropsWithChildren }from 'react';
import SearchableDropdown from "../SearchableDropdown";

interface ModalDefaultType {
  onClickToggleModal: () => void;  // 함수 타입 정의
}

function Modal({
  onClickToggleModal,
}: PropsWithChildren<ModalDefaultType>) {
  const [value, setValue] = useState("Select option...");
  const animals = ['a', 'b', 'c']
  const id=1
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
            {/* <SearchableDropdown
              options={animals}
              label="name"
              id={id}
              selectedVal={value}
              handleChange={(val:any) => setValue(val)}/> */}
          </div>
          <div>
            <span>팀원 초대하기</span>
                 
          </div>
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