import Header from './Header';
import Footer from './Footer';

function Layout(props: { children: React.ReactNode }) {
  if (window.location.pathname === '/login')
    return (
      <div className="">
        <main>{props.children}</main>
      </div>
    );
  else
    return (
      <div className="">
        <Header />
        <main className="min-h-[calc(100vh-60px)]">{props.children}</main>
        {/* padding-top: header와 겹치지 않기 위함 */}
        {/* min-height: 100vh - (헤더/푸터 크기) -> footer 하단 고정 위해 사용 */}
        <Footer />
      </div>
    );
}

export default Layout;
