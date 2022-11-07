import { useState, useCallback } from 'react';
import { useAppSelector, useAppDispatch } from '../../stores/ConfigHooks';
import { logout, setDeleteModalOpen } from '../../stores/modules/modal';
import { ModalSelector } from '../../stores/modules/modal';
import { axiosInstance } from '../auth/axiosConfig';
import { Link, useNavigate } from 'react-router-dom';

// import { PropsWithChildren } from 'react';

// interface ModalDefaultType {
//   onClickToggleModal: () => void; // 함수 타입 정의
// }

// interface ModalList extends ModalDefaultType {
//   props: string;
//   submit: () => void;
// }

// interface LogoutModal extends ModalList {
//   submit: () => void;
// }

// interface Modal {
//   props: string;
//   submit: () => void;
// }

function DeleteModal() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const props = 'delete';
  // const deleteModalSelector = useAppSelector(ModalSelector);
  // console.log(deleteModalSelector);
  const { deleteModalIsOpen } = useAppSelector((state) => state.modal);
  const { deleteModalType } = useAppSelector((state) => state.modal);
  // console.log(deleteModalIsOpen);
  // console.log(deleteModalType.type);

  const handleToggleModal = useCallback(() => {
    dispatch(setDeleteModalOpen({ title: '', function: '' }));
  }, [dispatch]);

  const getLogout = () => {
    dispatch(logout());
  };

  return (
    <div className={`${deleteModalIsOpen ? 'fixed' : 'hidden'} w-[100%] h-[100%] flex justify-center items-center`}>
      <div className="w-[450px] h-[300px] flex flex-col items-center bg-background z-[10000] rounded drop-shadow-shadow">
        <svg
          onClick={handleToggleModal}
          xmlns="https://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth="2.5"
          className="w-6 h-6 stroke-cancel mt-[15px] ml-[400px] cursor-pointer"
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
        </svg>
        {deleteModalType === 'delete' ? (
          <div>
            <div className="flex flex-col justify-center items-center text-l font-bold mt-[30px]">
              <div>
                등록한 <span className="text-cancel">일정</span>을
              </div>
              <div>
                <span className="text-cancel">삭제</span>하시겠습니까?
              </div>
            </div>
            <button className="text-[16px] font-bold bg-background border-solid border-2 border-cancel text-cancel hover:bg-cancelhover hover:text-background mt-[40px] rounded w-[350px] h-s drop-shadow-button">
              삭제하기
            </button>
          </div>
        ) : (
          <div className="flex flex-col justify-center items-center">
            <div className="text-l font-bold mt-[60px]">
              <span className="text-cancel">로그아웃 </span>하시겠습니까?
            </div>
            <button
              onClick={getLogout}
              className="text-[16px] font-bold bg-background border-solid border-2 border-cancel text-cancel hover:bg-cancelhover hover:text-background mt-[40px] rounded w-[350px] h-s drop-shadow-button"
            >
              로그아웃하기
            </button>
          </div>
        )}
      </div>
      <div
        className="w-[100%] h-[100%] fixed top:0 z-49 bg-[rgba(0,0,0,0.45)]"
        onClick={(e: React.MouseEvent) => {
          e.preventDefault();

          if (handleToggleModal) {
            handleToggleModal();
          }
        }}
      />
    </div>
  );
}
export default DeleteModal;
