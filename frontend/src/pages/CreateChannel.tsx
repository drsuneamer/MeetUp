import { useState, useEffect } from 'react';
import Layout from '../components/layout/Layout';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import { axiosInstance } from '../components/auth/axiosConfig';
import Chip from '@mui/material/Chip';
import CloseIcon from '@mui/icons-material/Close';
import CircularProgress from '@mui/material/CircularProgress';
import { useDidMountEffect } from '../hooks/useDidMountEffect';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';

interface Member {
  id: string;
  nickname: string;
}

interface Submit {
  teamId: string;
  displayName: string;
  name: string;
  type: string;
  inviteList: string[];
}

interface TeamOptionType {
  displayName: string;
  id: string;
  type: string;
}

function CreateChannel() {
  const navigate = useNavigate();
  const [submit, setSubmit] = useState<Submit>({ teamId: '', displayName: '', name: '', type: '', inviteList: [] });
  const [teamList, setTeamList] = useState([]);
  const [teamId, setTeamId] = useState('');
  const [name, setName] = useState('');
  const [type, setType] = useState('Open');
  const [load, setLoad] = useState(false);
  const [check, setChecked] = useState(false);
  const [ph, setPh] = useState('데이터를 불러오는 중입니다.'); // 멤버 목록 placeholder
  const [member, setMember] = useState<{ id: string; nickname: string }[]>([]);
  const [val, setVal] = useState<Member[]>([]); // 현재 선택된 멤버들
  const [memberId, setMemberId] = useState<string[]>([]);

  useDidMountEffect(() => {
    setSubmit({ teamId: teamId, displayName: name, name: name, type: type, inviteList: memberId });
  }, [teamId, name, type, memberId]);

  useEffect(() => {
    axiosInstance.get('meetup/team/activate').then((res) => {
      setTeamList(res.data);
    });
  }, []);

  // 드롭다운에 팀 포함시키기 위한 형식
  const teamProps = {
    options: teamList,
    getOptionLabel: (option: TeamOptionType) => option.displayName,
  };

  const selectTeam = (e: any, value: any): void => {
    if (value !== null) {
      setTeamId(value.id);
    }
  };

  // [2] 채널 이름 설정
  const nameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
  };

  // 채널 이름에 한글 있는지 검사
  // 이름 안에 한글 있는지 검사
  const korean = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
  console.log(korean.test(name));

  // [3] 공개/비공개 설정
  const changeType = (e: string) => {
    setType(e);
  };

  // [4] 초대할 멤버 선택
  // 불러오는 중 로딩 화면
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

  // 사용자가 선택한 멤버의 id만 따로 저장
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

  const onSubmit = () => {
    if (korean.test(submit?.name)) {
      Swal.fire({ text: '채널 이름은 반드시 2글자 이상의 영문(소문자) 또는 숫자여야 합니다.', icon: 'error', confirmButtonColor: '#0552AC' });
    } else {
      axiosInstance
        .post('/meetup/channel', submit)
        .then((res) => {
          if (res.status === 201) {
            navigate(`/calendar/${localStorage.getItem('id')}`);
          }
        })
        .catch((err) => {
          console.log(err);
          if (err.response.data.errorCode === '40905') {
            Swal.fire({ text: '같은 이름의 채널이 이미 존재합니다.', icon: 'error', confirmButtonColor: '#0552AC' });
          }
        });
    }
  };

  return (
    <Layout>
      <div className="flex flex-col items-center justify-center pt-[80px] text-m">
        <div className="my-2 w-[50vw]">
          <div className="font-bold text-title cursor-default">
            채널을 생성할 팀 선택<span className="text-cancel">&#42;</span>
          </div>
          <div className="flex justify-center">
            {/*팀 선택 드롭다운 */}
            <Autocomplete
              isOptionEqualToValue={(option, value) => option.id === value.id}
              onChange={selectTeam}
              className="w-full border-b-title"
              ListboxProps={{ style: { maxHeight: '150px', fontSize: '13px', textAlign: 'center', fontFamily: 'pretendard' } }}
              {...teamProps}
              renderInput={(params) => <TextField {...params} variant="standard" />}
            />
          </div>
        </div>

        <div className="mt-10">
          <div className="flex w-[50vw] font-bold text-title cursor-default">
            채널 이름<span className="text-cancel">&#42;</span>
            <span className="text-[8px] pt-2 pl-1">이름은 반드시 2글자 이상의 영문 소문자 또는 숫자여야 합니다.</span>
          </div>
          <input
            onChange={nameChange}
            type="text"
            placeholder="ex. A102_Archive"
            className="w-full text-center placeholder-placeholder border-b-2 border-b-placeholder py-1 px-2 mb-10 placeholder:text-s focus:outline-none focus:border-b-footer"
          />
        </div>

        {/* 공개 범위 설정 */}
        <div className="mt-2 mb-7">
          <div className="w-[50vw] font-bold text-title cursor-default">
            공개 범위<span className="text-cancel">&#42;</span>
          </div>

          <div className="flex items-center mb-4">
            <input
              onClick={() => {
                changeType('Open');
              }}
              defaultChecked
              type="radio"
              name="disabled-radio"
              className="w-4 h-4"
            />
            <label className="ml-2">공개</label>
            <p className="ml-2 pt-1 text-xs text-label">누구나 이 채널에 참여할 수 있습니다</p>
          </div>

          <div className="flex items-center">
            <input
              onClick={() => {
                changeType('Private');
              }}
              type="radio"
              name="disabled-radio"
              className="w-4 h-4"
            />
            <label className="ml-2">비공개</label>
            <p className="ml-2 pt-1 text-xs text-label">초대받은 사람만 이 채널에 참여할 수 있습니다.</p>
          </div>
        </div>

        <div className="mt-2 w-[50vw]">
          <div className="w-[50vw] font-bold text-title cursor-default">
            채널에 초대할 멤버<span className="text-cancel">&#42;</span>
          </div>
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
                    ListboxProps={{ style: { maxHeight: '15vh', fontSize: '13px', textAlign: 'center', fontFamily: 'pretendard' } }}
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
              <div className="relative h-[15vh] overflow-y-scroll scrollbar-hide">
                <div className="selectedTags">{valHtml}</div>
              </div>
            </div>
          ) : (
            <div className="mt-2 text-xs cursor-default">팀을 선택해주세요</div>
          )}
        </div>
        <div className="absolute bottom-[10vh] flex justify-center">
          <button onClick={onSubmit} className="font-bold bg-title hover:bg-hover text-background rounded w-[50vw] h-s drop-shadow-button">
            그룹 생성하기
          </button>
        </div>
      </div>
    </Layout>
  );
}

export default CreateChannel;
