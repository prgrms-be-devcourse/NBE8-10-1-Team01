"use client"

import { useEffect, useRef, useState } from "react";
import Script from 'next/script';
import { useRouter } from "next/navigation";

declare global {
  interface Window {
    daum: any;
  }
}

function Payment({ cart, totalAmount }: {
  cart: Order[],
  totalAmount: number
}) {
  const router = useRouter();

  const [postalCode, setPostalCode] = useState('');
  const [address1, setAddress1] = useState('');
  const [address2, setAddress2] = useState('');
  const inputRef = useRef(null);

  const handleAddressSearch = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();
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

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const form = e.target as HTMLFormElement;

    const emailInput = form.elements.namedItem("email") as HTMLInputElement;
    emailInput.value = emailInput.value.trim();

    const addressInput = form.elements.namedItem("address2") as HTMLInputElement;
    addressInput.value = addressInput.value.trim();

    const SERVER_URL = "http://localhost:8080";
    fetch(SERVER_URL + "/api/orders", {
      headers:{'Content-Type':'application/json'},
      method: "POST",
      body: JSON.stringify({
        customer: {
          email: emailInput.value,
          address: addressInput.value,
          postcode: postalCode
        },
        products: cart.map((item) => ({
          productId: item.product.productId,
          count: item.count
        }))
      })
    }).then(res => {
      if (!res.ok) throw new Error("주문에 실패했습니다.");
      return res.json();
    })
      .then(res => alert("결제가 완료되었습니다."))
      .catch(err => {
        console.error(err);
        alert("에러가 발생했습니다: " + err.message);
      });
    router.replace("/");
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

          <form onSubmit={handleSubmit} className="flex flex-col space-y-4 p-6 border border-gray-200 rounded-lg shadow-xl bg-white">

            {/* 이메일 입력 */}
            <label className="block text-sm font-medium text-gray-700 mt-2" htmlFor="email">이메일 주소</label>
            <input
              type="email"
              id="email"
              name="email"
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
                className="bg-gray-500 hover:bg-gray-600 text-white font-medium py-3 px-4 rounded-md transition duration-150 w-2/3 cursor-pointer"
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
              name="address2"
              className="mt-1 p-3 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-amber-500 focus:border-amber-500"
              placeholder="상세 주소 (예: 101동 202호)"
              required
              ref={inputRef}
            />

            {/* 최종 결제 버튼 */}
            <button
              type="submit"
              className="mt-6 bg-amber-700 hover:bg-amber-800 text-white font-semibold py-3 rounded-md transition duration-200 shadow-lg cursor-pointer"

            >
              총 {totalAmount.toLocaleString()}원 결제하기
            </button>
          </form>
        </div>
      </section>
    </>

  );
}

function Cart({ cart, totalAmount }: {
  cart: Order[],
  totalAmount: number
}) {
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
              <tr key={item.product.productId} className="hover:bg-gray-50">
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{item.product.name}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-center text-gray-500">{item.count}개</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-right text-gray-500">{item.product.price.toLocaleString()}원</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-right font-semibold text-gray-800">{(item.count * Number(item.product.price)).toLocaleString()}원</td>
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

function CartAndPayment({ cart }: {
  cart: Order[]
}) {

  const totalAmount = cart.reduce((acc, item) => acc + item.count * Number(item.product.price), 0);

  return (
    <>
      <Cart cart={cart} totalAmount={totalAmount} />
      <Payment cart={cart} totalAmount={totalAmount} />
    </>
  );
}

interface Product {
  productId: number;
  name: string;
  description: string;
  image: string;
  price: string;
}

function ItemList({ addToCart }: any) {
  const [productList, setProductList] = useState<Product[]>([]);

  const SERVER_URL = "http://localhost:8080";
  const headers = new Headers();
  headers.set("Content-Type", "application/json; charset=utf-8");
  useEffect(() => {
    fetch(SERVER_URL + "/api/products", { headers: headers })
      .then(res => res.json())
      .then(res => setProductList(res.data));

  }, []);


  const onClick = (product: Product) => {
    addToCart(product);
  };

  return (
    <>
      <section className="pt-32 pb-2 px-4 bg-gray-50">
        <div className="max-w-7xl mx-auto">
          <h2 className="text-3xl font-semibold mb-8 text-amber-900 border-b pb-2">오늘의 추천 원두</h2>
          <div className="flex space-x-6 overflow-x-auto pb-4">
            {productList.map((item, idx) => (
              <div
                key={idx}
                // 여기에 flex-shrink-0 클래스가 반드시 있어야 가로로 겹치지 않음
                className={`flex-shrink-0 w-64 p-5 rounded-xl shadow-lg border border-amber-200`}
              >
                <div className="h-24 w-full mb-3 rounded-md bg-white/70 flex items-center justify-center text-4xl text-amber-800">☕</div>
                <h3 className="text-lg font-bold text-amber-800 mb-1">{item.name}</h3>
                <p className="text-sm text-gray-600 mb-4">{item.description}</p>
                <button className="w-full bg-amber-600 text-white py-2 rounded-lg text-sm font-medium hover:bg-amber-700 transition cursor-pointer"
                  onClick={() => onClick(item)}>
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

interface Order {
  product: Product
  count: number;
}

export default function Home() {
  const [cart, setCart] = useState<Order[]>([]);

  const addToCart = (product: Product) => {
    const isExist = cart.find((item) => item.product.productId === product.productId);
    if (isExist) {
      setCart(cart.map((cartItem) => {
        if (cartItem.product.productId == product.productId) {
          return { ...cartItem, count: cartItem.count + 1 };
        }
        return cartItem;
      }));
    } else {
      setCart([...cart, { product, count: 1 }]);
    }
  };

  return (
    <div className="min-h-screen bg-amber-50">
      <ItemList addToCart={addToCart} />
      <CartAndPayment cart={cart} />
    </div>
  );
}