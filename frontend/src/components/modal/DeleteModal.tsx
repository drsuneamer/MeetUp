import { useCallback } from 'react';
import { useAppSelector, useAppDispatch } from '../../stores/ConfigHooks';
import { setDeleteModalOpen } from '../../stores/modules/modal';
import { axiosInstance } from '../auth/axiosConfig';
import { useNavigate } from 'react-router-dom';
import { deleteMeetingDetail, deleteScheduleDetail, detailSelector } from '../../stores/modules/schedules';

function DeleteModal() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { deleteModalIsOpen } = useAppSelector((state) => state.modal);
  const { deleteModalType } = useAppSelector((state) => state.modal);

  const meetingDetail = useAppSelector(detailSelector).scheduleModal.meetingDetail;
  const scheduleDetail = useAppSelector(detailSelector).scheduleModal.scheduleDetail;

  const handleToggleModal = useCallback(() => {
    dispatch(setDeleteModalOpen({ title: '', function: '' }));
  }, [dispatch]);

  const logout = async () => {
    await axiosInstance.get('/user/logout').then((res) => {
      if (res.status === 200) {
        window.localStorage.clear();
        navigate('/');
      }
    });
  };

  const handleDelete = () => {
    if (deleteModalType[1] === 'schedule') {
      dispatch(deleteScheduleDetail(scheduleDetail.id));
    } else if (deleteModalType[1] === 'meeting') {
      dispatch(deleteMeetingDetail(meetingDetail.id));
    }
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
        {deleteModalType[0] === 'delete' ? (
          <div>
            <div className="flex flex-col justify-center items-center text-l font-bold mt-[30px]">
              <div>
                등록한 <span className="text-cancel">일정</span>을
              </div>
              <div>
                <span className="text-cancel">삭제</span>하시겠습니까?
              </div>
            </div>
            <button
              onClick={handleDelete}
              className="text-[16px] font-bold bg-background border-solid border-2 border-cancel text-cancel hover:bg-cancelhover hover:text-background mt-[40px] rounded w-[350px] h-s drop-shadow-button"
            >
              삭제하기
            </button>
          </div>
        ) : (
          <div className="flex flex-col justify-center items-center">
            <div className="text-l font-bold mt-[60px]">
              <span className="text-cancel">로그아웃 </span>하시겠습니까?
            </div>
            <button
              onClick={logout}
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
