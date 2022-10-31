import Layout from '../components/layout/Layout';

function Settings() {
  const url = 'https://ssafyclass.webex.com/meet/psm7335';

  return (
    <Layout>
      <div className="text-m mx-[20vw] pt-[20vh] pb-[180px]">
        <div>
          <div className="font-bold text-title">나의 Webex 주소 관리</div>
          <input
            // onChange={onChangeID}
            type="text"
            placeholder={url}
            className="w-full text-center placeholder-[black] border-b-2 border-b-title py-1 px-2 mb-8 focus:outline-none focus:border-b-footer"
          />
        </div>
      </div>
    </Layout>
  );
}

export default Settings;
