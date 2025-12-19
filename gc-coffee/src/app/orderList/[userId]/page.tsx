"use client"

import { apiFetch } from "@/lib/backend/client";
import { OrderDto, OrderWithCreateDateDto } from "@/type/item";
import { useParams, useRouter } from "next/navigation";
import { use, useEffect, useState } from "react";

function UnTrackedList({untrackedOrderList}:{
  untrackedOrderList:OrderWithCreateDateDto[]
}) {

  return (
    <ul className="space-y-4">
      <h1 className="text-4xl font-extrabold text-gray-900 flex items-center">
        <span className="bg-orange-100 text-orange-800 text-sm font-medium px-3 py-1 mt-4 rounded-full uppercase tracking-wider shadow-sm">
          배송 완료
        </span>
      </h1>
      {untrackedOrderList.length > 0 && untrackedOrderList.map((order) => (
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
      {(!untrackedOrderList || untrackedOrderList.length === 0) &&
        <li>
          <div className="flex flex-col">
            <span className="font-semibold text-lg text-gray-900">배송 내역이 없습니다</span>
          </div>
        </li>
      }
    </ul>

  );
}


function TrackedList({ trackedOrderList, onDelete }: {
  trackedOrderList: OrderWithCreateDateDto[];
  onDelete: () => void;
}) {


  return (
    <ul className="space-y-4">
      <li className="flex space-around">
        <h1 className="text-4xl font-extrabold text-gray-900 flex items-center">
          <span className="bg-orange-100 text-orange-800 text-sm font-medium mr-3 px-3 py-1 rounded-full uppercase tracking-wider shadow-sm">
            배송 대기
          </span>
        </h1>
        {trackedOrderList.length > 0 &&
          <button
            onClick={onDelete}
            className="bg-red-500 text-white px-3 py-1 rounded-md text-sm hover:bg-red-600 transition cursor-pointer"
          >
            배송 취소
          </button>
        }
      </li>
      {trackedOrderList.map((order) => (
        <li
          key={order.productId}
          className="flex justify-between items-center p-4 bg-white rounded-lg shadow-md"
        >
          <div className="flex flex-col">
            <span className="font-semibold text-lg text-gray-900">{order.productName}</span>
            <span className="text-sm text-gray-500">(수량: {order.count}개)</span>
            <span className="font-bold text-indigo-600 mt-1">총: {Number(order.price) * Number(order.count)}원</span>
          </div>
        </li>
      ))}
      {(!trackedOrderList || trackedOrderList.length === 0) &&
        <li>
          <div className="flex flex-col">
            <span className="font-semibold text-lg text-gray-900">배송 내역이 없습니다</span>
          </div>
        </li>
      }
    </ul>
  );
}

export default function Page() {
  const { userId: userIdStr } = useParams<{ userId: string }>();
  const userId = Number(userIdStr);

  const router = useRouter();

  const [orderList, setOrderList] = useState<number[]>([]);
  const [trackedOrderList, setTrackedOrderList] = useState<OrderWithCreateDateDto[]>([]);
  const [untrackedOrderList, setUnTrackedOrderList] = useState<OrderWithCreateDateDto[]>([]);

  useEffect(() => {
    apiFetch(`/api/orders/${userId}`, {
      headers: { 'Content-Type': 'application/json' }
    })
      .then(res => {
        let _orderList: number[] = []
        const updatedItems = res.data.flatMap((ticket: any) => {
          _orderList.push(ticket.orderId);
          return ticket.orderItems.map((item: OrderWithCreateDateDto) => ({
            ...item,
            createDate: ticket.createDate
          }));
        })
        parseOrderList(updatedItems);
        setOrderList(_orderList);
      })
      .catch(err => {
        alert("에러가 발생했습니다: " + err.message);
      });

  }, []);

  const parseOrderList = (updatedItems: OrderWithCreateDateDto[]) => {
    const now = new Date();
    const today2PM = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 14, 0, 0);
    const yesterday2PM = new Date(today2PM);
    yesterday2PM.setDate(yesterday2PM.getDate() - 1);

    const tracked = updatedItems.filter((order: OrderWithCreateDateDto) => {
      const date = new Date(order.createDate);
      if (now > today2PM) {
        return date < today2PM ? false : true
      } else {
        return date < yesterday2PM ? false : true
      }
    });

    const untracked = updatedItems.filter((order: OrderWithCreateDateDto) => {
      const date = new Date(order.createDate);
      if (now > today2PM) {
        return date < today2PM ? true : false
      } else {
        return date < yesterday2PM ? true : false
      }
    });

    setTrackedOrderList(tracked);
    setUnTrackedOrderList(untracked);
  };

  const onDelete = async () => {
    if (!confirm("정말 삭제하시겠습니까? 배송이 취소됩니다.")) return;

    try {
      const deletePromises = orderList.map((orderId) =>
        apiFetch(`/api/orders/${orderId}`, { method: "DELETE" })
      );

      await Promise.all(deletePromises);

      alert("모든 주문이 삭제되었습니다.");
      setTrackedOrderList([]);

    } catch (error) {
      console.error(error);
      alert("삭제 중 에러가 발생했습니다." + error);
      router.refresh();
    }
  }

  return (
    <>
      <div className="min-h-screen bg-amber-50">
        <section className="pt-32 pb-20 px-4">
          <TrackedList trackedOrderList={trackedOrderList} onDelete={onDelete} />
          <UnTrackedList untrackedOrderList={untrackedOrderList}/>
        </section>
      </div >
    </>
  );
}