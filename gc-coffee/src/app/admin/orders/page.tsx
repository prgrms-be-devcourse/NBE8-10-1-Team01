"use client";

import { useEffect, useMemo, useState } from "react";

const BASE = process.env.NEXT_PUBLIC_BACKEND_BASE_URL ?? "http://localhost:8080";

//백 주문 데이터 타입
type BO = {
  orderId: number;
  createDate: string;
  customerId: number;
  customerEmail: string;
  orderItems: { productId: number; productName: string; count: number; price: number }[];
};

/**
 * 주문을 "당일 14시" 기준으로 분류
 * - 14시 이전 주문  -> 배송중
 * - 14시 이후 주문  -> 주문완료
 * -취소가 가능한 주문 : 전날14시부터 오늘14시까지 
 * -오늘 14시가 넘어가면 배송완료로 바뀜
 * 오후14시 정각 발송
 */
const groupByToday14 = (iso: string) => {
  const d = new Date(iso);
  const now = new Date();
  const today14 = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 14, 0, 0, 0);
  const yesterday14 = new Date(now.getFullYear(), now.getMonth(), now.getDate()-1, 14, 0, 0, 0);

  
  if(now > today14){
    return d < today14 ? "ORDERED" : "IN_TRANSIT"
  }else{
    return d < yesterday14 ?  "ORDERED" : "IN_TRANSIT"
  }
};

export default function AdminOrdersPage() {
  const [orders, setOrders] = useState<BO[]>([]);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  //주문 목록 조회(GET /api/orders) 응답 {data : BO[]}
  useEffect(() => {
    const ac = new AbortController();
    (async () => {
      setLoading(true);
      setErr(null);
      try {
        const r = await fetch(`${BASE}/api/orders`, { signal: ac.signal, cache: "no-store" });
        if (!r.ok) throw new Error(`요청 실패 (${r.status}) ${await r.text().catch(() => "")}`);
        const j = (await r.json()) as { data: BO[] };
        setOrders((j.data ?? []).slice().sort((a, b) => +new Date(b.createDate) - +new Date(a.createDate)));  //최신 주문 위로
      } catch (e: any) {
        if (e?.name !== "AbortError") setErr(e?.message ?? "알 수 없는 오류");
      } finally {
        setLoading(false);
      }
    })();
    return () => ac.abort();
  }, []);

  //주문 목록 그룹으로 분리
  const [inTransit, ordered] = useMemo(() => {
    const a: BO[] = [], b: BO[] = [];
    orders.forEach((o) => (groupByToday14(o.createDate) === "IN_TRANSIT" ? a : b).push(o));
    return [a, b];
  }, [orders]);

  const Table = ({ list }: { list: BO[] }) => (
    <div className="rounded border overflow-x-auto">
      <table className="min-w-[980px] w-full text-sm">
        <thead className="bg-gray-50">
          <tr>
            <th className="text-left p-3">주문ID</th>
            <th className="text-left p-3">주문일시</th>
            <th className="text-left p-3">고객</th>
            <th className="text-left p-3">이메일</th>
            <th className="text-right p-3">총액</th>
            <th className="text-left p-3">아이템</th>
          </tr>
        </thead>
        <tbody>
          {loading && (
            <tr><td colSpan={6} className="p-4">로딩중...</td></tr>
          )}
          {!loading && err && (
            <tr><td colSpan={6} className="p-4 text-red-600">{err}</td></tr>
          )}
          {!loading && !err && list.length === 0 && (
            <tr><td colSpan={6} className="p-4">데이터가 없습니다.</td></tr>
          )}
          {!loading && !err && list.map((o) => {
            const total = (o.orderItems ?? []).reduce((s, it) => s + it.price * it.count, 0);
            const name = o.customerEmail?.split("@")[0] ?? "-";
            return (
              <tr key={o.orderId} className="border-t">
                <td className="p-3">{o.orderId}</td>
                <td className="p-3">{new Date(o.createDate).toLocaleString()}</td>
                <td className="p-3">{name} (#{o.customerId})</td>
                <td className="p-3">{o.customerEmail}</td>
                <td className="p-3 text-right">{total.toLocaleString()}원</td>
                <td className="p-3">
                  {(o.orderItems ?? []).slice(0, 2).map((it) => (
                    <div key={it.productId}>{it.productName} x {it.count}</div>
                  ))}
                  {(o.orderItems?.length ?? 0) > 2 && (
                    <div className="text-gray-500">외 {(o.orderItems?.length ?? 0) - 2}개</div>
                  )}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );

  return (
    <div className="p-6 space-y-6">
      <h1 className="text-xl font-semibold">관리자 주문 조회</h1>
      <div className="space-y-10">
        <section>
          <h2 className="text-lg font-semibold mb-2">배송중 (당일 14시 이전 주문)</h2>
          <Table list={inTransit} />
        </section>
        <section>
          <h2 className="text-lg font-semibold mb-2">주문완료 (당일 14시 이후 주문)</h2>
          <Table list={ordered} />
        </section>
      </div>
      <div className="text-sm text-gray-600">총 {orders.length}건</div>
    </div>
  );
}
