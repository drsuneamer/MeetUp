import CircularProgress from '@mui/material/CircularProgress';

function LoginSpinner() {
  return (
    <div className="flex flex-col items-center justify-center">
      <CircularProgress sx={{ color: 'white' }} />
      <div className="mt-10 text-xl font-bold text-background">Loading</div>
      <div className="text-m mt-2 text-background">
        <span>첫 로그인시 </span>
        <span>데이터 동기화</span>
        <span>를 위한 시간이 소요됩니다 (최대 2분)</span>
      </div>
    </div>
  );
}

export default LoginSpinner;
