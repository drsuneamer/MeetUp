import { useState, useEffect, useCallback } from 'react';
import { useAppSelector, useAppDispatch } from '../../stores/ConfigHooks';
import { setCreateGroupModalOpen } from '../../stores/modules/modal';
import { axiosInstance } from '../../components/auth/axiosConfig';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import { useDidMountEffect } from '../../hooks/useDidMountEffect';
import Chip from '@mui/material/Chip';
import CloseIcon from '@mui/icons-material/Close';
import CircularProgress from '@mui/material/CircularProgress';
import { useNavigate } from 'react-router-dom';
import { fetchGroupList, groupSelector } from '../../stores/modules/groups';

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
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [teamList, setTeamList] = useState([]);
  const [teamId, setTeamId] = useState('');
  const [name, setName] = useState(''); // 그룹 이름
  const [load, setLoad] = useState(false);
  const [check, setChecked] = useState(false);
  const [ph, setPh] = useState('데이터를 불러오는 중입니다.'); // 멤버 목록 placeholder
  const [member, setMember] = useState<{ id: string; nickname: string }[]>([]);
  const [val, setVal] = useState<Member[]>([]); // 현재 선택된 멤버들
  const [memberId, setMemberId] = useState<string[]>([]);
  const [submit, setSubmit] = useState<Submit>({ name: '', members: [] });

  // [1] 멤버를 선택할 팀 설정
  // 렌더링과 동시에 활성화된 팀 목록만 가져옴
  useEffect(() => {
    axiosInstance.get('meetup/team').then((res) => {
      setTeamList(res.data);
    });
  }, []);

  // 팀 드롭다운에 props로 넘기기 위해 사용
  const teamProps = {
    options: teamList,
    getOptionLabel: (option: Team) => option.displayName,
  };

  // 팀 선택 시 id값 teamNow에 저장
  const selectTeam = (e: any, value: any): void => {
    if (value !== null) {
      setTeamId(value.id);
    }
  };

  // 드롭다운에서 팀을 선택하면 (teamNow값이 생기면) placeholder를 바꾸고 로딩 스피너를 띄움
  useDidMountEffect(() => {
    setPh('데이터를 불러오는 중입니다.');
    setLoad(false);
    getMembers();
  }, [teamId]);

  // 팀 선택 시 해당 멤버 요청
  const getMembers = () => {
    axiosInstance
      .get(`meetup/userList/${teamId}`)
      .then((res) => {
        setPh('멤버 목록');
        setLoad(true);
        setChecked(true);
        setMember(res.data);
      })
      .catch((err) => {
        setLoad(false);
      });
  };

  // 로딩 완료 후 체크 - 3초 뒤 사라짐
  useEffect(() => {
    const timer = setTimeout(() => {
      setChecked(false);
    }, 3000);

    return () => {
      clearTimeout(timer);
    };
  }, [check]);

  // [2] 그룹 이름 설정
  const nameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
  };

  // [3] 멤버 선택
  const valHtml = val.map((option: Member, index) => {
    // This is to handle new options added by the user (allowed by freeSolo prop).
    const label: string = option.nickname;
    return (
      // 각 멤버 닉네임 Chip
      <Chip
        className="mr-1 mb-1 font-pre"
        key={index}
        label={label}
        deleteIcon={<CloseIcon />}
        onDelete={() => {
          setVal(val.filter((entry) => entry !== option));
        }}
      />
    );
  });

  useDidMountEffect(() => {
    const m: string[] = [];
    for (let i = 0; i < val.length; i++) {
      if (val[i].id !== window.localStorage.getItem('id')) {
        m.push(val[i].id);
      }
    }
    if (m.length > 0) {
      setMemberId(m);
    }
  }, [val]);

  useDidMountEffect(() => {
    setSubmit({ name: name, members: memberId });
  }, [name, memberId]);

  // [4] POST 요청
  const onSubmit = () => {
    axiosInstance
      .post('/group', submit)
      .then((res) => {
        if (res.status === 201) {
          handleToggleModal();
          dispatch(fetchGroupList());
        }
      })
      .catch((err) => {
        console.log(err);
        if (err.response.data.errorCode === '40907') {
          alert('같은 이름의 그룹이 존재합니다');
        }
      });
  };

  // [*] 모달 닫힘 관리
  const { createGroupModalIsOpen } = useAppSelector((state) => state.modal);
  const handleToggleModal = useCallback(() => {
    dispatch(setCreateGroupModalOpen());
  }, [dispatch]);

  return (
    <div className={`${createGroupModalIsOpen ? 'fixed' : 'hidden'} w-[100%] h-[100%] flex justify-center items-center `}>
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
              isOptionEqualToValue={(option, value) => option.id === value.id}
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

          {/* 팀이 선택되었을 때에만 멤버 드롭다운 레이아웃 보임 */}
          {teamId ? (
            <div className="items-center justify-center overflow-hidden">
              <div className="flex">
                <div style={{ width: 500, fontFamily: 'pretendard' }}>
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
                    ListboxProps={{ style: { maxHeight: '150px', fontSize: '13px', textAlign: 'center', fontFamily: 'pretendard' } }}
                    renderInput={(params) => <TextField {...params} variant="standard" placeholder={ph} margin="normal" fullWidth />}
                  />
                </div>
                {!load ? <CircularProgress sx={{ color: '#0552AC', position: 'absolute', right: '8%' }} size="1.5rem" /> : ''}
                {check ? (
                  <svg
                    xmlns="https://www.w3.org/2000/svg"
                    fill="none"
                    strokeWidth="2"
                    stroke="#0552AC"
                    className="w-6 h-6 animate-bounce absolute right-10"
                  >
                    <path strokeLinecap="round" strokeLinejoin="round" d="M4.5 12.75l6 6 9-13.5" />
                  </svg>
                ) : (
                  ''
                )}
              </div>
              <div className="relative h-[12vh] overflow-y-scroll">
                <div className="selectedTags">{valHtml}</div>
              </div>
            </div>
          ) : (
            <div className="mt-2">팀을 선택해주세요</div>
          )}
        </div>
        <div className="flex justify-center">
          <button
            onClick={onSubmit}
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
