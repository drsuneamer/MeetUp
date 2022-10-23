import React, { PropsWithChildren }from 'react';
interface ModalDefaultType {
  onClickToggleModal: () => void;  // 함수 타입 정의
}

function Modal({
  onClickToggleModal,
}: PropsWithChildren<ModalDefaultType>) {
  return (
    <div className="w-[100%] h-[100%] fixed flex justify-center items-center">
      <div className="w-[600px] h-[600px] flex flex-col items-center bg-body z-10">
        <div onClick={onClickToggleModal}>x</div>
        어쩌고저쩌고
        </div>
      <div
        className='w-[100%] h-[100%] fixed top:0 z-9 bg-title'
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