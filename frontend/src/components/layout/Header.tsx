import LogoImage from '../../assets/logo_title.png';
import { Link } from 'react-router-dom';

function Header() {
  return (
    <nav>
      <div className="fixed flex items-center bg-[white] w-full h-l">
        <Link to="/">
          <img className="h-s ml-2" src={LogoImage} alt="logo" />
        </Link>
      </div>
    </nav>
  );
}

export default Header;
