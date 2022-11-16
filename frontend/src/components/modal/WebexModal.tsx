import { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { useAppSelector, useAppDispatch } from '../../stores/ConfigHooks';
import { ModalSelector, setWebexModalOpen } from '../../stores/modules/modal';
import { axiosInstance } from '../auth/axiosConfig';

function WebexModal() {
  const params = useParams();
  const dispatch = useAppDispatch();
  const modalSelector = useAppSelector(ModalSelector);

  // 모달 온오프
  const { webexModalIsOpen } = useAppSelector((state) => state.modal);
  const handleToggleModal = useCallback(() => {
    dispatch(setWebexModalOpen());
  }, [dispatch]);

  // webex 주소값, 닉네임 가져오기
  const [nickname, setNickname] = useState<string>('');
  const [webex, setWebex] = useState<string>('아직 설정된 주소가 없습니다.');

  useEffect(() => {
    const userId = params.userId;
    if (modalSelector.webexModalIsOpen) {
      axiosInstance.get(`/user/webex/${userId}`).then((res) => {
        if (res.data.webexUrl !== null) {
          setWebex(res.data.webexUrl);
        }
      });
      axiosInstance.get(`/user/nickname/${userId}`).then((res) => {
        setNickname(res.data);
      });
    }
  });

  // 웹엑스 이동 if
  const moveOut = () => {
    if (webex.includes('https://') || webex.includes('http://')) {
      window.open(webex, '_blank');
    } else {
      window.open('https://' + webex);
    }
  };

  return (
    <div className={`${webexModalIsOpen ? 'fixed' : 'hidden'} w-[100%] h-[100%] flex justify-center items-center z-30`}>
      <div className="w-[100%] h-[100%] fixed flex justify-center items-center">
        <div className="w-[450px] h-[300px] flex flex-col items-center bg-background z-10 rounded drop-shadow-shadow">
          <svg
            onClick={handleToggleModal}
            xmlns="https://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            strokeWidth="2.5"
            className="w-6 h-6 stroke-title mt-[15px] ml-[400px] cursor-pointer"
          >
            <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
          </svg>
          <div className="flex flex-col justify-center items-center mt-[60px]">
            <p className="text-[16px] cursor-default">{webex}</p>
            {webex !== '아직 설정된 주소가 없습니다.' ? (
              <button
                onClick={moveOut}
                className="text-[16px] bg-title text-background hover:bg-hover mt-[40px] rounded w-[350px] h-s drop-shadow-button"
              >
                {nickname}의 웹엑스로 이동하기
              </button>
            ) : (
              <button className="text-[16px] bg-footer mt-[40px] rounded w-[350px] h-s drop-shadow-button cursor-default">
                {nickname}의 웹엑스로 이동하기
              </button>
            )}
          </div>
        </div>
        <div
          className="w-[100%] h-[100%] fixed top:0 z-9 bg-[rgba(0,0,0,0.45)]"
          onClick={(e: React.MouseEvent) => {
            e.preventDefault();

            if (handleToggleModal) {
              handleToggleModal();
            }
          }}
        />
      </div>
    </div>
  );
}
export default WebexModal;
