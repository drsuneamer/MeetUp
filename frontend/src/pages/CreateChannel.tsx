import Layout from '../components/layout/Layout';

function CreateChannel() {
  return (
    <Layout>
      <div className="text-m mx-[15vw] pt-[20vh]">
        <div className="mb-10">
          <div className="font-bold text-title">알림을 받을 채널 선택하기</div>
          <button className="bg-title rounded drop-shadow-shadow text-background text-s font-medium w-full h-s my-2">
            내 MM의 모든 팀/채널 확인하기
          </button>
        </div>

        <div className="mb-5">
          <div className="font-bold text-title">MM 채널 이름 (변경 가능)</div>
          <div className="flex justify-center">
            <input
              // onChange={}
              type="text"
              placeholder="ex. 서울_1반_A102"
              className="w-full text-center placeholder-placeholder border-b-2 border-b-title py-1 px-2 mb-10 placeholder:text-s focus:outline-none focus:border-b-footer"
            />
          </div>
        </div>

        <div className="font-bold text-title">
          <div>달력에 표시할 색상 선택</div>
        </div>

        <button className="bg-title rounded drop-shadow-shadow text-background font-medium w-full h-s my-2 hover:bg-hover">저장하기</button>
      </div>
    </Layout>
  );
}

export default CreateChannel;