import { Link } from 'react-router-dom';

function NotFound() {
  return (
    <div
      className="flex flex-col w-screen h-screen
     bg-title items-center justify-center pb-[80px]"
    >
      <div className="font-damion text-background text-2xl">404</div>
      <div className="text-background text-m mt-[40px]">Page Not Found</div>
      <div className="font-bold text-m text-background mb-[60px]">페이지를 찾을 수 없습니다.</div>
      <Link to="/">
        <button className="bg-background rounded text-m font-bold drop-shadow-shadow h-s w-m text-title hover:bg-hover hover:text-background">
          내 캘린더로 돌아가기
        </button>
      </Link>
    </div>
  );
}

export default NotFound;
