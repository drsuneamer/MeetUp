import './App.css';

import { Routes, Route } from 'react-router-dom';

import Admin from './pages/Admin';
import AdminLogin from './pages/AdminLogin';
import CreateChannel from './pages/CreateChannel';
import EditChannel from './pages/EditChannel';
import Login from './pages/Login';
import MeetUp from './pages/MeetUp';
import NotFound from './pages/NotFound';
import Tutorial from './pages/Tutorial';

function App() {
  return (
    <div className="font-noto">
        <Routes>
          <Route path="/" element={<MeetUp />} />
          <Route path="/login" element={<Login />} />
          <Route path="/*" element={<NotFound />} />
          <Route path="/tutorial" element={<Tutorial />} />
          <Route path="/create-channel" element={<CreateChannel />} />
          <Route path="/edit-channel" element={<EditChannel />} />
          <Route path="/admin" element={<Admin />} />
          <Route path="/admin-login" element={<AdminLogin />} />
        </Routes>
    </div>
  );
}

export default App;
