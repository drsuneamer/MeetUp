import LogoImage from '../../assets/logo_title.png';
import gitlab from '../../assets/gitlab_icon.png';
import notion from '../../assets/notion_icon.png';
import insta from '../../assets/insta_icon.png';

function Footer() {
  return (
    <div className="bg-footer w-full pt-[200px]">
      <div className="flex w-full items-start justify-between px-[90px]">
        <img src={LogoImage} alt="logo" width="200px" />
        <div className="flex flex-col pt-3 cursor-default font-pre">
          <p className="font-bold cursor-default mb-2">Members</p>
          <a href="https://github.com/seongminP98">박성민</a>
          <a href="https://github.com/audtjr9514">김명석</a>
          <a href="https://github.com/drsuneamer">신선영</a>
          <a href="https://github.com/silversalmon216">연승용</a>
          <a href="https://github.com/qminlee723">이규민</a>
          <a href="https://github.com/MinjinChae">채민진</a>
        </div>

        <div className="cursor-default pt-3">
          <p className="font-bold mb-2">Project</p>
          <p>ucc</p>
          <p>presentation</p>
          <p>storyboard</p>
          <p>ERD diagram</p>
        </div>

        <div className="cursor-default pt-3">
          <p className="font-bold mb-2">Contact Us</p>
          <p>FAQ</p>
        </div>
      </div>
      <div className="flex justify-end mr-[50px] mb-3">
        <a href="https://www.instagram.com/hellossafy/">
          <img className="w-[40px] mx-1.5" src={insta} alt="instagram" />
        </a>
        <a href="https://www.notion.so/MEET-UP-6622422c3f554e6e852e7996eefeec77">
          <img className="w-[40px] mx-1.5" src={notion} alt="notion" />
        </a>
        <a href="https://lab.ssafy.com/s07-final/S07P31A102">
          <img className="w-[40px] mx-1.5" src={gitlab} alt="gitlab" />
        </a>
      </div>
      <hr className="bg-hover border-[0.5px] border-hover mx-[50px]" />

      <div className="text-right text-[13px] mt-[20px] mr-[50px] pb-[30px]">© 2022 Team MTLD All rights reserved</div>
    </div>
  );
}

export default Footer;
