import LogoImage from '../../assets/logo_title.png';
import { Link } from 'react-router-dom';
import { useState, useCallback } from 'react';
import DeleteModal from '../modal/DeleteModal';

function Header() {
  const [isOpenModal, setIsOpenModal] = useState<boolean>(false)
  const [modalType, setModalType] = useState('')
  const onClickToggleModal = useCallback(() => {
    setIsOpenModal(!isOpenModal);
  }, [isOpenModal]);
  // const logout = (e: React.MouseEvent<SVGSVGElement, MouseEvent>) => {
  //   console.log('logout!');
  // };

  const nickname = window.localStorage.getItem('nickname');
  
  function logoutProps() {
    setModalType('logout')
    onClickToggleModal()
  }

  return (
    <div className="w-[100%] h-[100%] flex flex-col items-center">
      {isOpenModal && (
          <DeleteModal onClickToggleModal={onClickToggleModal} props={modalType}>
          </DeleteModal>
        )}
      <div className="fixed flex items-center justify-between bg-[white] w-full h-l border-b-2 border-line z-50">
        <div>
          <Link to="/">
            <img className="h-s ml-2" src={LogoImage} alt="logo" />
          </Link>
        </div>

        <div className="flex mr-2">
          <div className="font-bold pr-1">{nickname}</div>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            strokeWidth="1.5"
            stroke="currentColor"
            className="w-6 h-6 pt-0.5 cursor-pointer"
            onClick={logoutProps}
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M15.75 9V5.25A2.25 2.25 0 0013.5 3h-6a2.25 2.25 0 00-2.25 2.25v13.5A2.25 2.25 0 007.5 21h6a2.25 2.25 0 002.25-2.25V15m3 0l3-3m0 0l-3-3m3 3H9"
            />
          </svg>
        </div>
      </div>
    </div>
  );
}

export default Header;
