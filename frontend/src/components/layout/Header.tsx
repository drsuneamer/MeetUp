import DeleteModal from '../modal/DeleteModal';
import CreateGroupModal from '../modal/CreateGroupModal';
import MemberListModal from '../../components/modal/MemberListModal';
import Modal from '../../components/modal/Modal';
import DetailModal from '../../components/modal/DetailModal';
import WebexModal from '../../components/modal/WebexModal';
import EditModal from '../../components/modal/EditModal';
import HeaderLayout from './HeaderLayout';

function Header() {
  return (
    <>
      <div className="relative z-50 w-[100%] h-[100%] flex flex-col items-center">
        <MemberListModal />
        <CreateGroupModal />
        <DeleteModal />
        <Modal />
        <DetailModal />
        <EditModal />
        <WebexModal />
      </div>
      <div className="relative z-30 w-[100%] h-[100%] flex flex-col items-center">
        <HeaderLayout />
      </div>
    </>
  );
}

export default Header;
