"use client"

import { useParams } from "next/navigation";
import { use, useEffect, useState } from "react";

interface Order {
  productId: number;
  productName: string;
  price: string;
  count: string;
  createDate: string;
}

export default function Page() {
  const { userId: userIdStr } = useParams<{ userId: string }>();
  const userId = Number(userIdStr);

  const [orderList, setOrderList] = useState<Order[]>([]);
  const [trackedOrderList, setTrackedOrderList] = useState<Order[]>([]);
  const [untrackedOrderList, setUnTrackedOrderList] = useState<Order[]>([]);
  useEffect(() => {
    const SERVER_URL = "http://localhost:8080";
    fetch(`${SERVER_URL}/api/orders/${userId}`, {
      headers: { 'Content-Type': 'application/json' }
    }).then(res => {
      if (!res.ok) throw new Error("주문에 실패했습니다.");
      return res.json();
    })
      .then(res => {
        const updatedItems = res.data.flatMap((ticket:any)=>{
          return ticket.orderItems.map((item: Order) => ({
            ...item,
            createDate: ticket.createDate
          }));
        })
        parseOrderList(updatedItems);
      })
      .catch(err => {
        console.error(err);
        alert("에러가 발생했습니다: " + err.message);
      });

  }, []);

  const parseOrderList = (updatedItems: Order[]) => {
    const now = new Date();
    const today2PM = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 14, 0, 0);
    const yesterday2PM = new Date(today2PM);
    yesterday2PM.setDate(yesterday2PM.getDate() - 1);

    const tracked = updatedItems.filter((order: Order) => {
      const date = new Date(order.createDate);
      return date > yesterday2PM && date < today2PM;
    });
    const untracked = updatedItems.filter((order: Order) => {
      const date = new Date(order.createDate);
      return date <= yesterday2PM || date >= today2PM;
    });
    setTrackedOrderList(tracked);
    setUnTrackedOrderList(untracked);
  };

  const onClick = (orderId: number) => {
    if(!confirm("정말 삭제하기겠습니까? 배송이 취소됩니다."))return;

    const SERVER_URL = "http://localhost:8080";
    fetch(`${SERVER_URL}/api/orders/${orderId}`,{method:"DELETE"})
    .then(res => {
      if (!res.ok) throw new Error("주문삭제에 실패했습니다.");
      return res.json();
    })
      .then(res => {
        alert("삭제가 완료되었습니다.")
      })
      .catch(err => {
        console.error(err);
        alert("에러가 발생했습니다: " + err.message);
      });
  };

  return (
    <>
      <div className="min-h-screen bg-amber-50">
        <section className="pt-32 pb-20 px-4">

          <ul className="space-y-4">
            <h1 className="text-4xl font-extrabold text-gray-900 flex items-center">
              <span className="bg-orange-100 text-orange-800 text-sm font-medium mr-3 px-3 py-1 rounded-full uppercase tracking-wider shadow-sm">
                배송 대기
              </span>
            </h1>
            {trackedOrderList.map((order) => (
              <li
                key={order.productId}
                className="flex justify-between items-center p-4 bg-white rounded-lg shadow-md"
              >

                <div className="flex flex-col">
                  <span className="font-semibold text-lg text-gray-900">{order.productName}</span>
                  <span className="text-sm text-gray-500">(수량: {order.count}개)</span>
                  <span className="font-bold text-indigo-600 mt-1">{order.price}원</span>
                </div>

                <button
                  onClick={() => onClick(order.id)}
                  className="bg-red-500 text-white px-3 py-1 rounded-md text-sm hover:bg-red-600 transition cursor-pointer"
                >
                  취소
                </button>
              </li>
            ))}
          </ul>

          <ul className="space-y-4">
            <h1 className="text-4xl font-extrabold text-gray-900 flex items-center">
              <span className="bg-orange-100 text-orange-800 text-sm font-medium px-3 py-1 mt-4 rounded-full uppercase tracking-wider shadow-sm">
                배송 완료
              </span>
            </h1>
            {untrackedOrderList.map((order) => (
              <li
                key={order.productId}
                className="flex justify-between items-center p-4 bg-white rounded-lg shadow-md"
              >

                <div className="flex flex-col">
                  <span className="font-semibold text-lg text-gray-900">{order.productName}</span>
                  <span className="text-sm text-gray-500">(수량: {order.count}개)</span>
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