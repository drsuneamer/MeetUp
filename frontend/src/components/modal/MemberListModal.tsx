import  { PropsWithChildren }from 'react';

interface ModalDefaultType {
  onClickToggleModal: () => void; 
}
function MemberListModal({
  onClickToggleModal,
}:
PropsWithChildren<ModalDefaultType>) {
  return (
    <div className="w-[100%] h-[100%] fixed flex justify-center items-center">
      <div className="w-[450px] h-[400px] bg-background z-10 rounded drop-shadow-shadow">
        <svg onClick={onClickToggleModal} xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" className="w-6 h-6 stroke-title mt-[15px] ml-[420px] cursor-pointer">
  <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
  </svg>
  <p className="text-placeholder text-[16px] ml-[5px]">서울1반_팀장채널 내 멤버</p>
        <div className="mt-[10px] h-[320px] overflow-auto scrollbar-hide">
          <div className="p-[5px] hover:bg-line">
            <div className="text-[18px] font-bold">이태희(컨설턴트)</div>
            <div className="text-[18px]">@fusanova</div>
          </div>
          <div className="p-[5px] hover:bg-line">
            <div className="text-[18px] font-bold">이태희(컨설턴트)</div>
            <div className="text-[18px]">@fusanova</div>
          </div>
          <div className="p-[5px] hover:bg-line">
            <div className="text-[18px] font-bold">이태희(컨설턴트)</div>
            <div className="text-[18px]">@fusanova</div>
          </div>
          <div className="p-[5px] hover:bg-line">
            <div className="text-[18px] font-bold">이태희(컨설턴트)</div>
            <div className="text-[18px]">@fusanova</div>
          </div>
          <div className="p-[5px] hover:bg-line">
            <div className="text-[18px] font-bold">이태희(컨설턴트)</div>
            <div className="text-[18px]">@fusanova</div>
          </div>
          <div className="p-[5px] hover:bg-line">
            <div className="text-[18px] font-bold">이태희(컨설턴트)</div>
            <div className="text-[18px]">@fusanova</div>
          </div>
          <div className="p-[5px] hover:bg-line">
            <div className="text-[18px] font-bold">이태희(컨설턴트)</div>
            <div className="text-[18px]">@fusanova</div>
          </div>
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

export default MemberListModal;
