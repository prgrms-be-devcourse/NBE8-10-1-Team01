"use client"

import { useEffect, useRef, useState } from "react";
import Script from 'next/script';

declare global {
  interface Window {
    daum: any;
  }
}

function Payment({ totalAmount }: any) {
  const [postalCode, setPostalCode] = useState('');
  const [address1, setAddress1] = useState('');
  const [address2, setAddress2] = useState('');
  const inputRef = useRef(null);

  const handleAddressSearch = () => {
    if (window.daum && window.daum.Postcode) {
      new window.daum.Postcode({
        oncomplete: function (data: any) {

          // 도로명 주소
          let roadAddr = data.roadAddress;
          // 법정동/법인명칭의 경우 추가 주소
          let extraRoadAddr = '';

          if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
            extraRoadAddr += data.bname;
          }
          if (data.buildingName !== '' && data.apartment === 'Y') {
            extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
          }
          if (extraRoadAddr !== '') {
            extraRoadAddr = ' (' + extraRoadAddr + ')';
          }

          setPostalCode(data.zonecode);         // 새 우편번호 (5자리)
          setAddress1(roadAddr + extraRoadAddr); // 기본 주소
          setAddress2('');                      // 상세 주소는 비워두고 사용자 포커스 유도
          if (inputRef.current) {
            inputRef.current.focus();
          }
        }
      }).open();
    }
  };

  return (
    <>
      <Script
        src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"
        strategy="beforeInteractive" // 중요: 로드 시점 지정
      />
      <section className="py-16 px-4 bg-white border-t border-gray-100">
        <div className="max-w-xl mx-auto">
          <h2 className="text-3xl font-semibold mb-8 text-amber-900 border-b pb-2">배송 및 결제 정보</h2>

          <form className="flex flex-col space-y-4 p-6 border border-gray-200 rounded-lg shadow-xl bg-white">

            {/* 이메일 입력 */}
            <label className="block text-sm font-medium text-gray-700 mt-2" htmlFor="email">이메일 주소</label>
            <input
              type="email"
              id="email"
              className="mt-1 p-3 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-amber-500 focus:border-amber-500"
              placeholder="user@example.com"
              required
            />

            {/* 주소 검색 그룹 */}
            <label className="block text-sm font-medium text-gray-700" htmlFor="postalCode">주소</label>
            <div className="flex space-x-3">
              <input
                type="text"
                id="postalCode"
                className="p-3 border border-gray-300 rounded-md shadow-sm w-1/3"
                placeholder="우편번호"
                readOnly
                required
                value={postalCode}
              />
              <button
                type="button"
                onClick={handleAddressSearch}
                className="bg-gray-500 hover:bg-gray-600 text-white font-medium py-3 px-4 rounded-md transition duration-150 w-2/3"
              >
                우편번호 검색
              </button>
            </div>

            {/* 기본 주소 */}
            <input
              type="text"
              id="address1"
              className="mt-1 p-3 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-amber-500 focus:border-amber-500"
              placeholder="기본 주소 (검색 결과)"
              readOnly
              required
              value={address1}
            />

            {/* 상세 주소 */}
            <input
              type="text"
              id="address2"
              className="mt-1 p-3 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-amber-500 focus:border-amber-500"
              placeholder="상세 주소 (예: 101동 202호)"
              required
              ref={inputRef}
            />

            {/* 최종 결제 버튼 */}
            <button
              type="submit"
              className="mt-6 bg-amber-700 hover:bg-amber-800 text-white font-semibold py-3 rounded-md transition duration-200 shadow-lg"
            >
              총 {totalAmount.toLocaleString()}원 결제하기
            </button>
          </form>
        </div>
      </section>
    </>

  );
}

function Cart({ cart, totalAmount }: any) {
  return (
    <section className="py-16 px-4 bg-white">
      <div className="max-w-4xl mx-auto">
        <h2 className="text-3xl font-semibold mb-8 text-amber-900 border-b pb-2">장바구니 ({cart.length})</h2>

        <table className="min-w-full divide-y divide-gray-200 border border-gray-100 shadow-md rounded-lg overflow-hidden">
          <thead className="bg-amber-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-amber-700 uppercase tracking-wider">이름</th>
              <th className="px-6 py-3 text-center text-xs font-medium text-amber-700 uppercase tracking-wider">수량</th>
              <th className="px-6 py-3 text-right text-xs font-medium text-amber-700 uppercase tracking-wider">가격</th>
              <th className="px-6 py-3 text-right text-xs font-medium text-amber-700 uppercase tracking-wider">소계</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-100">
            {cart.map((item) => (
              <tr key={item.id} className="hover:bg-gray-50">
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{item.name}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-center text-gray-500">{item.quantity}개</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-right text-gray-500">{item.price.toLocaleString()}원</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-right font-semibold text-gray-800">{(item.quantity * item.price).toLocaleString()}원</td>
              </tr>
            ))}
          </tbody>
          <tfoot>
            <tr className="bg-amber-100 font-bold">
              <td colSpan={3} className="px-6 py-3 text-lg text-left text-amber-900">총 결제 금액</td>
              <td className="px-6 py-3 text-lg text-right text-amber-900">{totalAmount.toLocaleString()}원</td>
            </tr>
          </tfoot>
        </table>
      </div>
    </section>
  );
}

function CartAndPayment({cart}:any) {
  
  const totalAmount = cart.reduce((acc, item) => acc + item.quantity * item.price, 0);

  return (
    <>
      <Cart cart={cart} totalAmount={totalAmount} />
      <Payment totalAmount={totalAmount} />
    </>
  );
}

function ItemList({setCart}:any) {
  const SERVER_URL = "localhost:8080";
  const [_brandImage, setBrandImage] = useState([]);

  useEffect(() => {
    fetch("")
  }, []);

  const brandImages = [
    { name: 'Ethiopia Yirgacheffe', img: 'bg-amber-100', content: "content1" },
    { name: 'Colombia Supremo', img: 'bg-stone-200', content: "content2" },
    { name: 'Kenya AA', img: 'bg-orange-100', content: "content3" },
    { name: 'Brazil Santos', img: 'bg-neutral-200', content: "content4" },
    { name: 'Guatemala Antigua', img: 'bg-yellow-100', content: "content5" },
    { name: 'Costa Rica Tarrazu', img: 'bg-amber-50', content: "content6" },
    { name: 'Peru Organic', img: 'bg-stone-100', content: "content7" },
    { name: 'Sumatra Mandheling', img: 'bg-orange-50', content: "content8" }
  ];

  const onClick=(item:any)=>{
    setCart(item)
  };

  return (
    <>
      <section className="pt-32 pb-2 px-4 bg-gray-50">
        <div className="max-w-7xl mx-auto">
          <h2 className="text-3xl font-semibold mb-8 text-amber-900 border-b pb-2">오늘의 추천 원두</h2>
          <div className="flex space-x-6 overflow-x-auto pb-4">
            {brandImages.map((image, idx) => (
              <div
                key={idx}
                // 여기에 flex-shrink-0 클래스가 반드시 있어야 가로로 겹치지 않음
                className={`flex-shrink-0 w-64 p-5 rounded-xl shadow-lg border border-amber-200`}
              >
                <div className="h-24 w-full mb-3 rounded-md bg-white/70 flex items-center justify-center text-4xl text-amber-800">☕</div>
                <h3 className="text-lg font-bold text-amber-800 mb-1">{image.name}</h3>
                <p className="text-sm text-gray-600 mb-4">{image.content}</p>
                <button className="w-full bg-amber-600 text-white py-2 rounded-lg text-sm font-medium hover:bg-amber-700 transition"
                  onClick={()=>onClick(image)}>
                  장바구니 담기
                </button>
              </div>
            ))}
          </div>
        </div>
      </section>
    </>
  );
}

export default function Home() {
  const [cart, setCart] = useState([
    { id: 101, name: 'Ethiopia Yirgacheffe', price: 18000 },
    { id: 102, name: 'Colombia Supremo', price: 15000 },
  ]);

  const addToCart = (item:any) =>{
    setCart([...cart,{...item,quantity:quantity+1}])
  };

  return (
    <div className="min-h-screen bg-amber-50">
      <ItemList setCart={setCart}/>
      <CartAndPayment cart={cart}/>
    </div>
  );
}