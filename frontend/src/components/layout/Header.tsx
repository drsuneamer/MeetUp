import LogoImage from '../../assets/logo_title.png';

function Header() {
  return (
    <div className="fixed flex items-center bg-[white] w-full h-l">
      <img className="h-s" src={LogoImage} alt="logo" />
    </div>
  );
}

export default Header;
