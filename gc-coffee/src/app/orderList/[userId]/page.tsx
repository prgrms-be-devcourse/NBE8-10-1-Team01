"use client"

import { use, useEffect, useState } from "react";

interface Order {
  id: number;
  name: string;
  content: string;
  price: string;
  quantity: string;
  createDate: string;
}

export default function Page({ params }: { params: Promise<{ id: string }> }) {
  const { id: userIdStr } = use(params);
  const userId = parseInt(userIdStr);

  const [orderList,setOrderList] = useState<Order[]>([]);
  useEffect(() => {
    fetch("")
  }, []);

  const dummyData: Order[] = [
    { id: 1, name: "상품 1", content: "내용 1", price: "1000", quantity: "1" ,createDate:"2025-12-17"},
    { id: 2, name: "상품 2", content: "내용 2", price: "2000", quantity: "1" ,createDate:"2025-12-17" },
  ];

  const onClick = (orderId:number) => {
    //정말 주문을 취소하시겠습니까?
    confirm();
    
    fetch("");
    setOrderList([]);
  };

  return (
    <>
      <div className="min-h-screen bg-amber-50">
        <section className="pt-32 pb-20 px-4">
        
          <ul className="space-y-4">
          <h1 className="text-4xl font-extrabold text-gray-900 flex items-center">
            <span className="bg-orange-100 text-orange-800 text-sm font-medium mr-3 px-3 py-1 
                          rounded-full uppercase tracking-wider shadow-sm">
              배송 대기
            </span>
          </h1>
            {dummyData.map((order) => (
              <li 
                key={order.id} 
                className="flex justify-between items-center p-4 bg-white rounded-lg shadow-md"
              >
                
                <div className="flex flex-col">
                  <span className="font-semibold text-lg text-gray-900">{order.name}</span>
                  <span className="text-sm text-gray-500">{order.content} (수량: {order.quantity}개)</span>
                  <span className="font-bold text-indigo-600 mt-1">{order.price}원</span>
                </div>
                
                <button 
                  onClick={() => onClick(order.id)}
                  className="bg-red-500 text-white px-3 py-1 rounded-md text-sm hover:bg-red-600 transition"
                >
                  취소
                </button>
              </li>
            ))}
          </ul>

          <ul className="space-y-4">
          <h1 className="text-4xl font-extrabold text-gray-900 flex items-center">
            <span className="bg-orange-100 text-orange-800 text-sm font-medium px-3 py-1 mt-4 
                          rounded-full uppercase tracking-wider shadow-sm">
              배송 완료
            </span>
          </h1>
            {dummyData.map((order) => (
              <li 
                key={order.id} 
                className="flex justify-between items-center p-4 bg-white rounded-lg shadow-md"
              >
                
                <div className="flex flex-col">
                  <span className="font-semibold text-lg text-gray-900">{order.name}</span>
                  <span className="text-sm text-gray-500">{order.content} (수량: {order.quantity}개)</span>
                  <span className="font-bold text-indigo-600 mt-1">{order.price}원</span>
                </div>
              </li>
            ))}
          </ul>

        </section>
      </div >

    </>
  );
}