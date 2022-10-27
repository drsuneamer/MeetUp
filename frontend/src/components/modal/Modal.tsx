import  { PropsWithChildren }from 'react';
import * as React from 'react';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
// import Stack from '@mui/material/Stack';

interface ModalDefaultType {
  onClickToggleModal: () => void;  // 함수 타입 정의
}

interface ChannelOptionType {
  title: string;
}

const top100Films = [
  { title: 'The Shawshank Redemption', year: 1994 },
  { title: 'The Godfather', year: 1972 },
  { title: 'The Godfather: Part II', year: 1974 },
  { title: 'The Dark Knight', year: 2008 },
  { title: '12 Angry Men', year: 1957 },
  { title: "Schindler's List", year: 1993 },
  { title: 'Pulp Fiction', year: 1994 },
  {
    title: 'The Lord of the Rings: The Return of the King',
    year: 2003,
  },
  { title: 'The Good, the Bad and the Ugly', year: 1966 },
  { title: 'Fight Club', year: 1999 },
  {
    title: 'The Lord of the Rings: The Fellowship of the Ring',
    year: 2001,
  },
  {
    title: 'Star Wars: Episode V - The Empire Strikes Back',
    year: 1980,
  },
  { title: 'Forrest Gump', year: 1994 },
  { title: 'Inception', year: 2010 },
  {
    title: 'The Lord of the Rings: The Two Towers',
    year: 2002,
  },
  { title: "One Flew Over the Cuckoo's Nest", year: 1975 },
  { title: 'Goodfellas', year: 1990 },
  { title: 'The Matrix', year: 1999 },
  { title: 'Seven Samurai', year: 1954 },
];
function Modal({
  onClickToggleModal,
}: PropsWithChildren<ModalDefaultType>) {
  const defaultProps = {
    options: top100Films,
    getOptionLabel: (option: ChannelOptionType) => option.title,
  };
  const flatProps = {
    options: top100Films.map((option) => option.title),
  };
  const [value, setValue] = React.useState<ChannelOptionType | null>(null);
  
  return (
    <div className="w-[100%] h-[100%] fixed flex justify-center items-center">
      <div className="w-[600px] h-[600px] flex flex-col items-center bg-background z-10 rounded drop-shadow-shadow">
        <div onClick={onClickToggleModal}>x</div>
        <div className="flex flex-col">
          <div>
            <span>미팅명</span>
            <input type="text" name="title" className="w-s h-[40px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
          </div>
          <div>
            <span>날짜</span>
            <input type="text" name="title" className="w-s h-[40px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
          </div>
          <div>
            <span>시간</span>
            <input type="text" name="title" className="w-s h-[40px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
          </div>
          <div>
            <span>내용</span>
            <input type="text" name="title" className="w-s h-[40px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
          </div>
          <div>
            <span>알림 보낼 채널</span>
            {/* <Stack spacing={1} sx={{ width: 300 }}> */}
            <Autocomplete
              {...defaultProps}
              id="disable-close-on-select"
              disableCloseOnSelect
              renderInput={(params) => (
                <TextField {...params} label="채널 선택하기" variant="standard" />
              )}
            />
            {/* </Stack> */}
          </div>
          {/* <div>
            <span>팀원 초대하기</span>
                 
          </div> */}
          <button className="bg-title hover:bg-hover text-background py-2 px-4 rounded w-[500] h-s">밋업 등록하기</button>
        </div>
        </div>
      <div
        className='w-[100%] h-[100%] fixed top:0 z-9 bg-[rgba(0,0,0,0.45)]'
        onClick={(e: React.MouseEvent) => {
          e.preventDefault();

          if (onClickToggleModal) {
            onClickToggleModal();
          }
        }}
      />
    </div>
  );
}
export default Modal;