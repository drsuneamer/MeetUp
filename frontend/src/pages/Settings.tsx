import Layout from '../components/layout/Layout';
import { useState, useEffect } from 'react';
import { axiosInstance } from '../components/auth/axiosConfig';
import Alert from '@mui/material/Alert';
import Switch from '@mui/material/Switch';
import { setCreateGroupModalOpen } from '../stores/modules/modal';
import { useAppSelector, useAppDispatch } from '../stores/ConfigHooks';
import { fetchGroupList, groupsSelector } from '../stores/modules/groups';
import GroupListItem from '../components/GroupListItem';

interface Team {
  displayName: string;
  id: string;
  isActivate: boolean;
}

interface Group {
  id: number;
  leader: boolean;
  name: string;
}

function Settings() {
  const dispatch = useAppDispatch();
  const groups: Group[] = useAppSelector(groupsSelector).groups;
  const role = window.localStorage.getItem('roleType');

  const [url, setUrl] = useState<string>('Webex Link');
  const [input, setInput] = useState<object>({ webexUrl: '' });
  const [done, setDone] = useState<boolean>(false);
  const [teamSet, setTeamSet] = useState<boolean>(false);
  const [teamArray, setTeamArray] = useState<Team[]>([]);
  const [forUse, setForUse] = useState<Team[]>([]);

  // 렌더링과 동시에 webex 주소와 전체 팀 목록 호출
  useEffect(() => {
    axiosInstance.get('/user/webex').then((res) => {
      if (res.status === 200) {
        if (res.data.webexUrl === null) {
          setUrl('Webex Link');
        } else {
          setUrl(res.data.webexUrl);
        }
      }
    });

    axiosInstance.get('meetup/team/activate').then((res) => {
      setTeamArray(res.data);
    });
  }, []);

  // [1] 웹엑스 주소 변경
  const onInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUrl(e.target.value);
  };

  useEffect(() => {
    setInput({ webexUrl: url });
  }, [url]);

  const submit = () => {
    axiosInstance.put('/user/webex', input).then((res) => {
      if (res.status === 201) {
        setDone(true);
      }
    });
  };

  // [2] 내가 사용할 팀 관리
  useEffect(() => {
    setForUse([...teamArray]);
  }, [teamArray]);

  const handleChange = (id: string) => {
    axiosInstance.put('/meetup/team/activate', [{ teamId: id }]).then((res) => {
      setTeamSet(true);
    });
  };

  const buttonChange = (i: number) => {
    forUse[i].isActivate = !forUse[i].isActivate;
    setForUse([...forUse]);
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      setTeamSet(false);
    }, 5000);

    return () => {
      clearTimeout(timer);
    };
  }, [teamSet]);

  // [3] 그룹 관리
  // 그룹 목록 가져옴
  useEffect(() => {
    dispatch(fetchGroupList());
  }, [dispatch]);

  const handleCreateGroupModal = () => {
    dispatch(setCreateGroupModalOpen());
  };

  // 그룹 이름 중복 안내: 5초 뒤에 사라짐
  useEffect(() => {
    const timer = setTimeout(() => {
      setDone(false);
    }, 5000);

    return () => {
      clearTimeout(timer);
    };
  }, [done]);

  return (
    <Layout>
      <div className="relative text-m mx-[20vw] pt-[15vh] pb-[180px]">
        {/* 웹엑스 주소 관리*/}
        <div>
          <div className="flex items-center">
            <div className="font-bold text-title cursor-default">나의 Webex 주소 관리</div>
          </div>
          <div className="flex">
            <input
              onKeyDown={(e) => {
                if (e.key === 'Enter') {
                  submit();
                }
              }}
              onChange={onInput}
              type="text"
              placeholder={url}
              className="w-full text-center placeholder-label border-b-2 border-b-title py-1 px-2 mb-4 focus:outline-none focus:border-b-footer"
            />
            <button
              onClick={submit}
              className="mt-3 ml-3 w-[50px] h-[23px] bg-background border-title border-solid border-[2px] text-[13px] drop-shadow-shadow rounded font-bold text-title align-middle"
            >
              저장
            </button>
          </div>

          {done ? (
            <div className="absolute w-full">
              <Alert severity="success">저장이 완료되었습니다.</Alert>
            </div>
          ) : (
            ''
          )}
        </div>
        {/* 팀 비활성화 - 학생이 아닌 경우에만 표시 */}
        {role?.includes('Student') ? (
          ''
        ) : (
          <div>
            <div className="flex mt-[8vh]">
              <div className="font-bold text-title cursor-default">내가 사용할 팀 관리</div>
            </div>
            <div className="mt-3 grid grid-rows-4 grid-flow-col">
              {teamArray.map((team, index: number) => (
                <div key={index} className="flex my-1">
                  <div className="text-s w-2/3 cursor-default">{team.displayName}</div>
                  <div
                    onClick={() => {
                      handleChange(team.id);
                    }}
                  >
                    <Switch
                      id={team.id}
                      onChange={() => {
                        buttonChange(index);
                      }}
                      checked={team.isActivate}
                      inputProps={{ 'aria-label': 'controlled' }}
                    />
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
        {teamSet ? (
          <div className="absolute w-full">
            <Alert severity="success">팀 설정이 저장되었습니다.</Alert>
          </div>
        ) : (
          ''
        )}
        {/* 그룹 관리 */}
        <div>
          <div className="flex mt-[8vh]">
            <div className="font-bold text-title cursor-default">나의 그룹 관리</div>

            <svg
              onClick={handleCreateGroupModal}
              xmlns="https://www.w3.org/2000/svg"
              viewBox="0 0 20 20"
              fill="currentColor"
              className="mt-1 ml-2 w-6 h-6 text-point cursor-pointer"
            >
              <path
                fillRule="evenodd"
                d="M10 18a8 8 0 100-16 8 8 0 000 16zm.75-11.25a.75.75 0 00-1.5 0v2.5h-2.5a.75.75 0 000 1.5h2.5v2.5a.75.75 0 001.5 0v-2.5h2.5a.75.75 0 000-1.5h-2.5v-2.5z"
              />
            </svg>
            <span className="text-[8px] text-title font-bold pt-3 pl-1">
              그룹 설정 후 미팅 신청 시 그룹을 선택하면 해당 그룹 멤버들의 캘린더에 일정이 공유됩니다.
            </span>
          </div>
          <div className="flex flex-wrap mt-2">
            {groups.map((group: Group) => (
              <GroupListItem key={group.id} group={group} />
            ))}
          </div>
        </div>
      </div>
    </Layout>
  );
}

export default Settings;
