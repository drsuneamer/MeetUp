import { useState, useEffect, useCallback } from 'react';
import { useAppSelector, useAppDispatch } from '../../stores/ConfigHooks';
import { setCreateGroupModalOpen } from '../../stores/modules/modal';
import { axiosInstance } from '../../components/auth/axiosConfig';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import { useDidMountEffect } from '../../hooks/useDidMountEffect';
import Chip from '@mui/material/Chip';
import RemoveIcon from '@mui/icons-material/Remove';
import CircularProgress from '@mui/material/CircularProgress';

interface Team {
  id: string;
  displayName: string;
  type: string;
}

interface Submit {
  name: string;
  members: string[];
}

interface Member {
  id: string;
  nickname: string;
}

function CreateGroupModal() {
  const dispatch = useAppDispatch();

  const [teamList, setTeamList] = useState([]);
  const [teamNow, setTeamNow] = useState('');
  const [name, setName] = useState('');
  const [submit, setSubmit] = useState<Submit>({ name: '', members: [] });

  const [load, setLoad] = useState(false);
  const [ph, setPh] = useState('데이터를 불러오는 중입니다.');

  const [member, setMember] = useState<{ id: string; nickname: string }[]>([]);
  const getMembers = () => {
    axiosInstance
      .get(`meetup/userList/${teamNow}`)
      .then((res) => {
        setPh('멤버 목록');
        setLoad(true);
        setMember(res.data);
      })
      .catch((err) => {
        setLoad(false);
      });
  };

  console.log(teamNow);
  useDidMountEffect(() => {
    setPh('데이터를 불러오는 중입니다.');
    setLoad(false);
    getMembers();
  }, [teamNow]);

  // 팀 드롭다운에 props로 넘기기 위해 사용
  const teamProps = {
    options: teamList,
    getOptionLabel: (option: Team) => option.displayName,
  };

  useEffect(() => {
    // 활성화된 팀만 가져옴
    axiosInstance.get('meetup/team').then((res) => {
      setTeamList(res.data);
    });
  }, []);

  // 드롭다운에서 팀을 선택하면 id값 teamNow에 저장
  const selectTeam = (e: any, value: any): void => {
    if (value !== null) {
      setTeamNow(value.id);
    }
  };

  const nameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
  };

  const [val, setVal] = useState<Member[]>([]);
  console.log(val);

  const valHtml = val.map((option: Member, index) => {
    // This is to handle new options added by the user (allowed by freeSolo prop).
    const label: string = option.nickname;
    return (
      <Chip
        key={index}
        label={label}
        deleteIcon={<RemoveIcon />}
        onDelete={() => {
          setVal(val.filter((entry) => entry !== option));
        }}
      />
    );
  });

  // 모달 닫힘 관리
  const { createGroupModalIsOpen } = useAppSelector((state) => state.modal);
  const handleToggleModal = useCallback(() => {
    dispatch(setCreateGroupModalOpen());
  }, [dispatch]);

  return (
    <div className={`${createGroupModalIsOpen ? 'fixed' : 'hidden'} w-[100%] h-[100%] flex justify-center items-center`}>
      <div className="w-[50vw] h-[70vh] bg-background z-10 rounded drop-shadow-shadow">
        <svg
          onClick={handleToggleModal}
          xmlns="https://www.w3.org/2000/svg"
          strokeWidth="2.5"
          className="w-6 h-6 stroke-title cursor-pointer absolute right-2 top-2"
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
        </svg>
        <p className="text-placeholder text-[14px] ml-4 mt-3">새로운 그룹 생성하기</p>

        {/* 전체 내용 */}
        <div className="mx-[5vw] mt-3">
          {/* 팀 선택*/}
          <div className="text-s text-title font-bold">
            멤버를 선택할 팀<span className="ml-1 text-cancel">&#42;</span>
          </div>
          <div className="flex justify-center">
            {/*팀 선택 드롭다운 */}
            <Autocomplete
              onChange={selectTeam}
              className="w-[40vw] border-b-title"
              ListboxProps={{ style: { maxHeight: '150px', fontSize: '13px', textAlign: 'center', fontFamily: 'pretendard' } }}
              {...teamProps}
              renderInput={(params) => <TextField {...params} variant="standard" />}
            />
          </div>
          <div className="text-s text-title font-bold mt-8">
            그룹 이름<span className="ml-1 text-cancel">&#42;</span>
          </div>
          <input
            onChange={nameChange}
            type="text"
            placeholder="ex. 서울 1반 2팀"
            className="w-full text-center placeholder-placeholder border-b-2 border-b-placeholder py-1 px-2 mb-10 placeholder:text-s focus:outline-none focus:border-b-footer"
          />
          <div className="text-s text-title font-bold">
            멤버 선택<span className="ml-1 text-cancel">&#42;</span>
          </div>
          {teamNow ? (
            <div className="flex items-center justify-center">
              <div style={{ width: 500 }}>
                <Autocomplete
                  multiple
                  id="tags-standard"
                  freeSolo
                  filterSelectedOptions
                  options={member}
                  onChange={(e, newValue: any) => setVal(newValue)}
                  getOptionLabel={(option) => option.nickname}
                  renderTags={(): any => {}}
                  value={val}
                  renderInput={(params) => <TextField {...params} variant="standard" placeholder={ph} margin="normal" fullWidth />}
                />
                <div className="selectedTags">{valHtml}</div>
              </div>
              {!load ? (
                <CircularProgress sx={{ color: 'blue' }} size="1.5rem" />
              ) : (
                <svg
                  xmlns="https://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth="1.5"
                  stroke="blue"
                  className="w-6 h-6 animate-bounce"
                >
                  <path strokeLinecap="round" strokeLinejoin="round" d="M4.5 12.75l6 6 9-13.5" />
                </svg>
              )}
            </div>
          ) : (
            <div className="mt-2">팀을 선택해주세요</div>
          )}
        </div>
        <div className="flex justify-center">
          <button
            // onClick={handleSubmitToYou}
            className="absolute bottom-4 font-bold bg-title hover:bg-hover text-background rounded w-[43vw] h-s drop-shadow-button"
          >
            그룹 생성하기
          </button>
        </div>
      </div>

      {/* 모달 배경 -> 클릭 시 모달 닫힘 */}
      <div
        className="w-[100%] h-[100%] fixed top:0 z-9 bg-[rgba(0,0,0,0.45)]"
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

export default CreateGroupModal;
