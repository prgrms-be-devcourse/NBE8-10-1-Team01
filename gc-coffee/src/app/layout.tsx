import type { Metadata } from 'next'
import './globals.css'
import Link from 'next/link';

export const metadata: Metadata = {
  title: 'Grids & Circles - 커피 로스터리',
  description: '그저 더 나은 원두를 고객들에게 맛 보여 드리기 위해',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  const menuItems = [
    { name: '장바구니 내역', href:"/order" },
    { name: '관리자', href:"/admin" }
  ];

  return (
    <html lang="ko">
      <body>
        <header className="fixed top-0 left-0 right-0 bg-amber-900/95 backdrop-blur-md border-b border-amber-800/50 z-50">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex items-center justify-between h-16">
            <div className="flex items-center">
              <Link 
                href="/" 
                className="text-2xl font-bold bg-gradient-to-r from-amber-200 to-orange-200 bg-clip-text text-transparent"
              >
                Grids & Circles
              </Link>
            </div>

              {/* Desktop Navigation */}
              <nav className="hidden md:flex items-center space-x-8">
                {menuItems.map((item) => (
                  <div key={item.name} className="relative">
                    <Link 
                      href={item.href || '#'} // ⬅️ item.href를 사용하여 경로 지정
                      className="flex items-center space-x-1 text-amber-100 hover:text-orange-200 transition-colors"
                  >
                      <span className="font-medium">{item.name}</span>
                  </Link>
                  </div>
                ))}
              </nav>

              {/* CTA Buttons */}
              <div className="hidden md:flex items-center space-x-4">
                <Link 
                    href="/orderList/emailCheck" 
                    className="text-amber-100 hover:text-orange-200 font-medium transition-colors"
                >
                    주문내역 확인하러 가기
                </Link>
                
                {/* 2. 장바구니 담기 링크 (CTA 버튼 스타일) */}
                <Link 
                    href="/order"
                    className="bg-gradient-to-r from-orange-400 to-amber-500 text-amber-900 px-6 py-2.5 rounded-full font-medium hover:shadow-lg hover:scale-105 transition-all"
                >
                    장바구니 담기
                </Link>
              </div>
            </div>
          </div>
        </header>
        {children}
      </body>
    </html>
  )
}