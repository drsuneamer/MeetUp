import { useState } from 'react';
import Modal from '../components/modal/Modal';

function CreateChannel() {
  const [visibleModal, setVisibleModal] = useState<boolean>(false)
  
  function open() {
    setVisibleModal(!visibleModal);
  }
  
  function onClose() {
    setVisibleModal(!visibleModal);
  }
  return (
    <div className="CreateChannel">
      <h1>CreateChannel</h1>
      <button className='bg-title hover:bg-blue-700 text-white font-bold py-2 px-4 rounded' onClick={open}>Show Modal</button>
      <Modal isOpen={visibleModal} isClose={onClose}></Modal>
    </div>
  );
}

export default CreateChannel;
