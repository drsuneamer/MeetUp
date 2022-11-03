import LogoImage from '../assets/logo_title.png';
import { useEffect, useState } from 'react';
import { axiosInstance } from '../components/auth/axiosConfig';
import { Navigate } from 'react-router-dom';

interface User {
  id: string;
  nickname: string;
  role: string;
}

function Admin() {
  const [users, setUsers] = useState<User[]>([]);
  const [post, setPost] = useState<object>({ id: '', roleType: '' });
  const [get, setGet] = useState<boolean>(false);

  const [role, setRole] = useState<string>('');
  const [name, setName] = useState<string>('');

  // 유저 권한 설정
  useEffect(() => {
    axiosInstance.get('/admin').then((res) => {
      setUsers(res.data);
    });
  }, [get]);

  const onChanged = (e: any): any => {
    setPost({ id: e.target.id, roleType: `ROLE_${e.target.value}` });
    setGet(false);
  };

  useEffect(() => {
    axiosInstance.post('/admin/role', [post]).then((res) => {
      setGet(true);
    });
  }, [post]);

  // 검색
  const [filtered, setFiltered] = useState<User[]>([]);

  const searchRole = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setRole(e.target.value);
  };

  const searchName = (e: React.ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
  };

  useEffect(() => {
    if (role === undefined && name === undefined) {
      setFiltered(users);
    } else {
      const filteredList = users.reduce((acc: User[], cur) => {
        const roleCondition = role && role.length > 0 ? cur.role.includes(role) : true;
        const nameCondition = name && name.length > 0 ? cur.nickname.includes(name) : true;
        if (roleCondition && nameCondition) {
          acc.push(cur);
        }
        return acc;
      }, []);

      setFiltered(filteredList);
    }
  }, [users, role, name]);

  // 관리자 로그인 만료 여부 확인 (캘린더로 리다이렉트)
  const tokenExpiresIn = Number(localStorage.getItem('tokenExpiresIn'));
  const today = new Date();
  const parsedToday = today.getTime();
  const isExpired = tokenExpiresIn - parsedToday < 0;

  if (isExpired) {
    return <Navigate to="/admin-login" />;
  }

  return (
    <div>
      {/* 헤더 */}
      <div className="fixed flex items-center justify-between bg-[white] w-full h-l border-b-2 border-line">
        <img className="h-s ml-2" src={LogoImage} alt="logo" />
        <div className="font-bold cursor-default mr-4">관리자</div>
      </div>

      {/* 내용 */}
      <div className="flex flex-col w-screen items-center justify-center pt-[65px]">
        <div className="font-bold text-xl">MeetUp 관리자 페이지</div>
        <div>처음 로그인 후 새로고침!! (관리자 계정 로그인 필수)</div>

        {/* 검색 */}
        <div className="flex">
          {/* 권한 필터링 */}
          <select className="w-[150px] outline-none" onChange={searchRole} defaultValue={'role'}>
            <option value="ROLE">권한</option>
            <option value="ROLE_Student">Student</option>
            <option value="ROLE_Consultant">Consultant</option>
            <option value="ROLE_Coach">Coach</option>
            <option value="ROLE_Pro">Pro</option>
            <option value="ROLE_Professor">Professor</option>
            <option value="ROLE_Admin">Admin</option>
          </select>

          {/* 닉네임 필터링 */}
          <div className="relative w-[300px] outline-none">
            {/* <div className=" outline-none flex absolute inset-y-0 left-0 items-center pl-3">
              <svg aria-hidden="true" className="w-5 h-5 text-gray-500" fill="currentColor" viewBox="0 0 20 20" xmlns="https://www.w3.org/2000/svg">
                <path
                  fillRule="evenodd"
                  d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                  clipRule="evenodd"
                ></path>
              </svg>
            </div> */}
            <input
              onChange={searchName}
              type="text"
              id="simple-search"
              className="outline-none bg-gray-50 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 p-2.5  dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
              placeholder="이름"
            />
          </div>
        </div>

        {/* 유저 조회*/}
        <div className="flex flex-col mx-[30vw] mt-[3vh]">
          {/* 컬럼명 */}
          <div className="font-bold text-title flex w-[80vw] mb-3">
            <p className="w-[30vw]">닉네임</p>
            <p className="w-[30vw]">현재 권한</p>
            <p className="">권한 설정하기</p>
          </div>

          {/* 리스트 */}
          {filtered.map((user, index) => (
            <div key={index} className="flex mb-2">
              <p className="w-[30vw]">{user.nickname}</p>
              <p className="w-[30vw]">{user.role}</p>
              <select
                id={user.id}
                className="outline-none bg-title text-center text-background rounded"
                onChange={onChanged}
                defaultValue={'default'}
              >
                <option className="bg-background text-center" value="default" disabled hidden>
                  {user.role.slice(5)}
                </option>
                <option className="bg-background text-center" value="Student">
                  Student
                </option>
                <option value="Consultant">Consultant</option>
                <option value="Coach">Coach</option>
                <option value="Pro">Pro</option>
                <option value="Professor">Professor</option>
                <option value="Admin">Admin</option>
              </select>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Admin;
