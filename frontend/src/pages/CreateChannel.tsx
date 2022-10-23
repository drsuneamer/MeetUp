import { useState, useCallback } from 'react';
import Modal from '../components/modal/Modal';

function CreateChannel() {
  const [isOpenModal, setIsOpenModal] = useState<boolean>(false)
  const onClickToggleModal = useCallback(() => {
    setIsOpenModal(!isOpenModal);
  }, [isOpenModal]);

  return (
    <div className="CreateChannel">
      <div className="w-[100%] h-[100%] flex flex-col items-center">
        <h1>CreateChannel</h1>
        {isOpenModal && (
          <Modal onClickToggleModal={onClickToggleModal}>
          </Modal>
        )}
        <button className='bg-title hover:bg-blue-700 text-white font-bold py-2 px-4 rounded w-[200px] h-[50px]' onClick={onClickToggleModal}>Show Modal</button>
      </div>
      
    </div>
  );
}

export default CreateChannel;