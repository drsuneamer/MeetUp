import LoginForm from '../components/LoginForm';
import Image1 from '../assets/background1.png';
// import Image2 from '../assets/background2.png';

function Login() {
  return (
    <div className="h-screen bg-cover flex items-center justify-center" style={{ backgroundImage: `url(${Image1})` }}>
      <LoginForm />
    </div>
  );
}

export default Login;
