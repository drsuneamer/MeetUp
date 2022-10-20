import LoginForm from '../components/LoginForm';
import Image1 from '../assets/background1.png';

function Login() {
  return (
    <div className="Login" style={{ backgroundImage: `url(${Image1})` }}>
      <h1>Login</h1>
      <LoginForm />
    </div>
  );
}

export default Login;
