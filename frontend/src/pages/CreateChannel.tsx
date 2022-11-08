import { useState, useEffect } from 'react';
import Layout from '../components/layout/Layout';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import { axiosInstance } from '../components/auth/axiosConfig';

/*
{
    "teamId" : "hkhoi73cn3f39c7xa86uxpabaw", -> 선택
    "displayName" : "a102_channel_test",
    "name" : "a102_channel_test_name",
    "type": "Private",     // Open: 공개 , Private: 비공개
    "inviteList" : [     // 초대할 유저 아이디 팀 선택하고 유저 챙겨서
        "m1ueqgdkifby7k68arnejn5ayr",
        "okdad9z7tfgh5byfzri9i7eybh",
        "syngmnbiytd8ugp5834doy1gme",
        "tjifxpgtjtrouxeade6kntxhha",
        "wrbeop3scbf9fx4zewgh7d7bka"
    ]
}

40905 "기존에 있는 채널네임이 중복되어 새로운 채널생성이 불가능합니다."
*/

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
  const [submit, setSubmit] = useState<Submit>();
  const [teamList, setTeamList] = useState([]);
  const [teamId, setTeamId] = useState('');
  const [name, setName] = useState('');
  const [displayName, setDisplayName] = useState('');
  const [type, setType] = useState('');
  const [inviteList, setInviteList] = useState([]);

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

  // const selectTeam = (e: any) => {
  //   console.log(e);
  // };

  const selectTeam = (e: any, value: any) => {
    console.log(value.displayName);
    // setAlarmChannelId(alarmChannelValue);
  };

  return (
    <Layout>
      <div className="flex flex-col items-center justify-center pt-[80px] text-m">
        <div className="my-2">
          <div className="font-bold text-title cursor-default">채널을 생성할 팀 선택</div>
          <Autocomplete
            onChange={selectTeam}
            className="w-[50vw] border-b-title"
            ListboxProps={{ style: { maxHeight: '150px', fontSize: '13px' } }}
            {...teamProps}
            id="select-channel"
            renderInput={(params) => <TextField {...params} variant="standard" />}
          />
        </div>

        <div className="mt-10">
          <div className="w-[50vw] font-bold text-title cursor-default">채널 이름</div>
          <input
            // onChange={titleChange}
            type="text"
            placeholder="ex. A102_Archive"
            className="w-full text-center placeholder-placeholder border-b-2 border-b-placeholder py-1 px-2 mb-10 placeholder:text-s focus:outline-none focus:border-b-footer"
          />
        </div>

        <div className="mt-2">
          <div className="w-[50vw] font-bold text-title cursor-default">공개 범위</div>

          <div className="flex items-center mb-4">
            <input
              defaultChecked
              id="disabled-radio-1"
              type="radio"
              value=""
              name="disabled-radio"
              className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
            />
            <label className="ml-2">공개</label>
            <p className="ml-2 pt-1 text-xs text-label">누구나 이 채널에 참여할 수 있습니다</p>
          </div>
          <div className="flex items-center">
            <input
              id="disabled-radio-2"
              type="radio"
              value=""
              name="disabled-radio"
              className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
            />
            <label className="ml-2">비공개</label>
            <p className="ml-2 pt-1 text-xs text-label">초대받은 사람만 이 채널에 참여할 수 있습니다.</p>
          </div>
        </div>
      </div>
    </Layout>
  );
}

export default CreateChannel;
