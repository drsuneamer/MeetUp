import CircularProgress from '@mui/material/CircularProgress';

function Spinner() {
  return (
    <div className="flex flex-col items-center justify-center pt-[35vh]">
      <CircularProgress />
      <div className="mt-10 text-m font-bold">Loading</div>
      <div className="mt-2">
        <span>Please wait, </span>
        <span className="text-title">MeetUp</span>
        <span> is working hard!</span>
      </div>
    </div>
  );
}

export default Spinner;
