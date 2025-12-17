"use client"

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

interface Product {
  productId: number;
  name: string;
  content: string;
  image: string;
  price: string;
}
export default function Admin() {
  const router = useRouter();

  const [productList, setProductList] = useState<Product[]>([]);

  const SERVER_URL = "http://localhost:8080";
  const headers = new Headers();
  headers.set("Content-Type", "application/json; charset=utf-8");
  useEffect(() => {
    fetch(SERVER_URL + "/api/products", { headers: headers })
      .then(res => res.json())
      .then(res => setProductList(res.data));


  }, []);
  const DummyList: Product[] = [
    {
      productId: 1,
      name: "시그니처 다크 블렌드",
      content: "고소한 견과류의 풍미와 다크 초콜릿의 묵직한 바디감",
      image: "https://images.unsplash.com/photo-1559056199-641a0ac8b55e?q=80&w=200&h=200&fit=crop",
      price: "18,000"
    },
    {
      productId: 2,
      name: "에티오피아 예가체프",
      content: "은은한 꽃향기와 기분 좋은 산미가 어우러진 싱글 오리진",
      image: "https://images.unsplash.com/photo-1580915411954-282cb1b0d780?q=80&w=200&h=200&fit=crop",
      price: "22,000"
    },
    {
      productId: 3,
      name: "바닐라 빈 라떼",
      content: "천연 바닐라 빈을 사용하여 깊고 부드러운 달콤함",
      image: "https://images.unsplash.com/photo-1595434066389-0bc3013d1730?q=80&w=200&h=200&fit=crop",
      price: "6,500"
    },
    {
      productId: 4,
      name: "콜드브루 원액",
      content: "12시간 동안 저온에서 천천히 추출한 깔끔한 맛",
      image: "https://images.unsplash.com/photo-1517701604599-bb29b565090c?q=80&w=200&h=200&fit=crop",
      price: "15,000"
    },
    {
      productId: 5,
      name: "오트 사이드 카페라떼",
      content: "유당 불내증 걱정 없는 고소한 귀리 우유 베이스 라떼",
      image: "https://images.unsplash.com/photo-1551046710-0229a69d3701?q=80&w=200&h=200&fit=crop",
      price: "7,000"
    }
  ];
  const onDelete = (productId: number) => {
    fetch("");
  };

  return (
    <>
      <div className="min-h-screen bg-stone-50 p-8">
        <section className="max-w-5xl mx-auto bg-white rounded-xl shadow-sm overflow-hidden border border-stone-200 pt-32 pb-20 px-4">
        <div className="flex items-center justify-between border-b mb-4 pb-2">
          <h2 className="text-3xl font-semibold text-amber-900">상품</h2>
          <button className="px-4 hover:cursor-pointer bg-amber-600 text-white py-2 rounded-lg text-sm font-medium hover:bg-amber-700 transition" onClick={()=>{}}>등록</button>
        </div>

          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-stone-100 border-b border-stone-200 text-stone-700">
                <th className="p-4 font-semibold">ID</th>
                <th className="p-4 font-semibold">이름</th>
                <th className="p-4 font-semibold">내용</th>
                <th className="p-4 font-semibold">가격</th>
                <th className="p-4 font-semibold text-center" colSpan={2}>관리</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-stone-100">
              {DummyList.map((p) => (
                <tr key={p.productId} className="hover:bg-amber-50/50 transition-colors hover:cursor-pointer" onClick={()=>{router.push(`admin/products/${p.productId}/edit`)}}>
                  <td className="p-4 text-stone-500">{p.productId}</td>
                  <td className="p-4 font-medium text-stone-900">{p.name}</td>
                  <td className="p-4 text-stone-600">{p.content}</td>
                  <td className="p-4 text-amber-800 font-semibold">{p.price.toLocaleString()}원</td>
                  <td className="p-4 text-center">
                    <Link href={`admin/products/${p.productId}/edit`} className="text-amber-600 hover:text-amber-800 text-sm">
                      수정
                    </Link>
                  </td>
                  <td className="p-4 text-center">
                    <button
                      onClick={() => onDelete(p.productId)}
                      className="text-rose-600 hover:text-rose-800 text-sm hover:cursor-pointer"
                    >
                      삭제
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>
      </div>
    </>
  );
}