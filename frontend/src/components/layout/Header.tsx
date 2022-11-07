import LogoImage from '../../assets/logo_title.png';
import { Link, useNavigate } from 'react-router-dom';
import { useState, useCallback } from 'react';
import DeleteModal from '../modal/DeleteModal';
import { axiosInstance } from '../auth/axiosConfig';

function Header() {
  const userId = window.localStorage.getItem('id');
  const url = `/calendar/${userId}`;
  const navigate = useNavigate();

  const [isOpenModal, setIsOpenModal] = useState<boolean>(false);
  const [modalType, setModalType] = useState('');
  const onClickToggleModal = useCallback(() => {
    setIsOpenModal(!isOpenModal);
  }, [isOpenModal]);

  const navTo = () => {
    navigate(`${url}`);
    window.location.reload();
  };

  const logout = async () => {
    await axiosInstance.get('/user/logout').then((res) => {
      if (res.status === 200) {
        window.localStorage.removeItem('id');
        window.localStorage.removeItem('nickname');
        window.localStorage.removeItem('accessToken');
        window.localStorage.removeItem('tokenExpiresIn');
        window.localStorage.removeItem('roleType');
        navigate('/');
      }
    });
  };
  const nickname = window.localStorage.getItem('nickname');
  const role = window.localStorage.getItem('roleType');

  function logoutProps() {
    setModalType('logout');
    onClickToggleModal();
  }

  return (
    <div className="relative z-50 w-[100%] h-[100%] flex flex-col items-center">
      {isOpenModal && <DeleteModal onClickToggleModal={onClickToggleModal} props={modalType} submit={logout}></DeleteModal>}
      <div className="fixed flex items-center justify-between bg-[white] w-full h-l border-b-2 border-line z-50">
        <div className="flex items-center">
          <div onClick={navTo} className="cursor-pointer">
            <img className="h-s ml-2" src={LogoImage} alt="logo" />
          </div>
          <button
            onClick={() => window.open('https://meetup.gitbook.io/meetup-docs/release-notes', '_blank')}
            className="drop-shadow-shadow mt-1 border-placeholder bg-title text-background px-3 rounded h-[30px] font-bold ml-3 text-s font-dots pb-1"
          >
            v 1.1.0
          </button>
        </div>

        <div className="flex mr-2 items-center">
          <div className="font-script font-bold text-title mr-2 text-m mt-1">{role?.slice(5)}</div>
          <Link to="/settings">
            <div className="font-bold pr-1 underline underline-offset-2">{nickname}</div>
          </Link>
          <svg
            xmlns="https://www.w3.org/2000/svg"
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
