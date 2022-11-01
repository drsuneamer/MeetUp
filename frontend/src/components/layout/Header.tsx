import LogoImage from '../../assets/logo_title.png';
import { Link, useNavigate } from 'react-router-dom';
import { useState, useCallback } from 'react';
import DeleteModal from '../modal/DeleteModal';
import { axiosInstance } from '../auth/axiosConfig';

function Header() {
  const navigate = useNavigate();

  const [isOpenModal, setIsOpenModal] = useState<boolean>(false);
  const [modalType, setModalType] = useState('');
  const onClickToggleModal = useCallback(() => {
    setIsOpenModal(!isOpenModal);
  }, [isOpenModal]);

  const logout = async () => {
    await axiosInstance.get('/user/logout').then((res) => {
      if (res.status === 200) {
        window.localStorage.removeItem('accessToken');
        window.localStorage.removeItem('tokenExpiresIn');
        navigate('/login');
      }
    });
  };
  const nickname = window.localStorage.getItem('nickname');

  function logoutProps() {
    setModalType('logout');
    onClickToggleModal();
  }

  return (
    <div className="relative z-50 w-[100%] h-[100%] flex flex-col items-center">
      {isOpenModal && <DeleteModal onClickToggleModal={onClickToggleModal} props={modalType} submit={logout}></DeleteModal>}
      <div className="fixed flex items-center justify-between bg-[white] w-full h-l border-b-2 border-line z-50">
        <div>
          <Link to="/">
            <img className="h-s ml-2" src={LogoImage} alt="logo" />
          </Link>
        </div>

        <div className="flex mr-2">
          <Link to="/settings">
            <div className="font-bold pr-1 underline underline-offset-2">{nickname}</div>
          </Link>
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
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M15.75 9V5.25A2.25 2.25 0 0013.5 3h-6a2.25 2.25 0 00-2.25 2.25v13.5A2.25 2.25 0 007.5 21h6a2.25 2.25 0 002.25-2.25V15m3 0l3-3m0 0l-3-3m3 3H9"
            />
          </svg>
        </div>
      </div>
    </div>
  );
}

export default Header;
