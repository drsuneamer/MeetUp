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
  const [post, setPost] = useState({ id: '', roleType: '' });
  const [get, setGet] = useState(false);

  // 유저 권한 설정
  useEffect(() => {
    axiosInstance.get('/admin').then((res) => {
      setUsers(res.data);
    });
  }, [get]);

  const onChanged = (e: any): any => {
    setPost({ id: e.target.id, roleType: e.target.value });
    setGet(false);
  };

  useEffect(() => {
    axiosInstance.post('/admin/role', [post]).then((res) => {
      setGet(true);
    });
  }, [post]);

  // 관리자 로그인 만료 여부 확인
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

        <div className="flex flex-col mx-[30vw] mt-[3vh]">
          <div className="font-bold text-title flex w-[80vw] mb-3">
            <p className="w-[300px]">닉네임</p>
            <p className="w-30">현재 권한</p>
          </div>
          {users.map((user, index) => (
            <div key={index} className="flex">
              <div className="flex w-[80vw]">
                <p className="w-[300px]">{user.nickname}</p>
                <p className="w-30">{user.role}</p>
              </div>
              <div className="flex">
                <label className="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-400"></label>
                <select
                  id={user.id}
                  className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  onChange={onChanged}
                  defaultValue={user.role}
                >
                  {/* <option selected>{user.role}</option> */}
                  <option value="Student">Student</option>
                  <option value="Consultant">Consultant</option>
                  <option value="Coach">Coach</option>
                  <option value="Pro">Pro</option>
                  <option value="Professor">Professor</option>
                  <option value="Admin">Admin</option>
                </select>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Admin;
