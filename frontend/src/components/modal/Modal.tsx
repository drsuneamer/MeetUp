import React from 'react';
interface props {
  isOpen: boolean;
  isClose: () => void; // 함수 타입 정의
}
const Modal = (props: props): React.ReactElement => {
  const { isOpen, isClose } = props;

  return (
    <div>
      <div className={isOpen ? 'bg' : ''} />
      <div className={isOpen ? 'modal active' : 'modal'}>
        {isOpen ? (
          <div>
             <button className="close" onClick={isClose}> x </button>
          </div>
        ) : null}
      </div>
    </div>
  );
};
export default Modal;
