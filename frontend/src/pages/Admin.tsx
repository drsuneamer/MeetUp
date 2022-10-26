import LogoImage from '../assets/logo_title.png';

function Admin() {
  return (
    <div>
      {/* 헤더 */}
      <div className="fixed flex items-center justify-between bg-[white] w-full h-l border-b-2 border-line">
        <img className="h-s ml-2" src={LogoImage} alt="logo" />
        <div className="font-bold cursor-default mr-4">관리자</div>
      </div>

      {/* 내용 */}
      <div className="flex justify-center pt-[65px]">
        <div className="font-bold text-xl">MeetUp 관리자 페이지</div>
      </div>
    </div>
  );
}

export default Admin;
