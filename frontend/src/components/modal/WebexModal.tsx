import  { PropsWithChildren }from 'react';

interface ModalDefaultType {
  onClickToggleModal: () => void; // 함수 타입 정의
}

function WebexModal({
  onClickToggleModal,
}:
PropsWithChildren<ModalDefaultType>) {
  return (
    <div className="w-[100%] h-[100%] fixed flex justify-center items-center">
      <div className="w-[450px] h-[300px] flex flex-col items-center bg-background z-10 rounded drop-shadow-shadow">
        <svg onClick={onClickToggleModal} xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" className="w-6 h-6 stroke-title mt-[15px] ml-[400px] cursor-pointer">
  <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
  </svg>
        <div className="flex flex-col justify-center items-center text-l font-bold mt-[60px]">
          <p className="text-[16px]">https://ssafyclass.webex.com/meet/fusanova</p>
          <button className="text-[16px] font-bold bg-title text-background hover:bg-hover mt-[40px] rounded w-[350px] h-s drop-shadow-button">이태희(컨설턴트)님의 웹엑스로 이동하기</button>
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
export default WebexModal;
