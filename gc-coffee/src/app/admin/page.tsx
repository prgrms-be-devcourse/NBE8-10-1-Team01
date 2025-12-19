"use client"

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

interface Product {
  productId: number;
  name: string;
  description: string;
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
  
  const onDelete = (productId: number) => {
    if (!confirm("정말 삭제하시겠습니까?")) return;
    fetch(SERVER_URL + `/api/products/${productId}`,{headers:headers,method:"DELETE"})
    .then((res) => {
      if (!res.ok) {
        return res.json().then((error) => { throw error; });
      }
      return res.json(); 
    })
    .then(()=>{
      setProductList(productList.filter((product)=>product.productId != productId));
    })
    .catch((err=>alert("삭제에러 : " + err)));
    
  };

  return (
    <>
      <div className="min-h-screen bg-stone-50 p-8">
        <section className="max-w-5xl mx-auto bg-white rounded-xl shadow-sm overflow-hidden border border-stone-200 pt-32 pb-20 px-4">
          <div className="flex items-center justify-between border-b mb-4 pb-2">
            <h2 className="text-3xl font-semibold text-amber-900">상품</h2>
            <div className="flex gep-2">
              <button className="px-4 bg-stone-200 text-stone-700 py-2 rounded-lg text-sm font-medium hover:bg-stone-300 transition mx-2 cursor-pointer" onClick={() => router.push("/admin/orders")}>주문 내역</button>
              <button className="px-4 hover:cursor-pointer bg-amber-600 text-white py-2 rounded-lg text-sm font-medium hover:bg-amber-700 transition" onClick={() => { router.push("/admin/products/new") }}>등록</button>
            </div>
          </div>

          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-stone-100 border-b border-stone-200 text-stone-700">
                <th className="p-4 font-semibold">ID</th>
                <th className="p-4 font-semibold">이름</th>
                <th className="p-4 font-semibold">내용</th>
                <th className="p-4 font-semibold">가격</th>
                <th className="p-4 font-semibold text-center">관리</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-stone-100">
              {productList.map((p) => (
                <tr key={p.productId} className="hover:bg-amber-50/50 transition-colors hover:cursor-pointer"
                  onClick={(e) => {
                    router.push(`admin/products/${p.productId}`);
                  }}>
                  <td className="p-4 text-stone-500">{p.productId}</td>
                  <td className="p-4 font-medium text-stone-900">{p.name}</td>
                  <td className="p-4 text-stone-600">{p.description}</td>
                  <td className="p-4 text-amber-800 font-semibold">{p.price.toLocaleString()}원</td>
                  <td className="p-4 text-center">
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        onDelete(p.productId);
                      }
                      }
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