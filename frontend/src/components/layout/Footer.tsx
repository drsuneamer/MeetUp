import LogoImage from '../../assets/logo_title.png';

function Footer() {
  return (
    <div className="flex flex-col items-center bg-[#AEC2FA] w-full pt-2">
      <div className="flex w-full items-start justify-between px-[100px]">
        <img src={LogoImage} alt="logo" width="200px" />
        <div>
          <p>Members</p>
          <p>박성민</p>
          <p>김명석</p>
          <p>신선영</p>
          <p>연승용</p>
          <p>이규민</p>
          <p>채민진</p>
        </div>

        <div>
          <p>Project</p>
          <p>ucc</p>
          <p>presentation</p>
          <p>storyboard</p>
          <p>ERD diagram</p>
        </div>

        <div>
          <p>Contact Us</p>
          <p>FAQ</p>
        </div>
      </div>
      <hr />
      <div className="">ⓒ 2022 Team MTLD All rights reserved</div>
    </div>
  );
}

export default Footer;
