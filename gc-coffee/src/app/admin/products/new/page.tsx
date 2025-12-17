"use client"

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";


interface Product {
  name: string;
  content: string;
  image: string;
  price: string;
}

export default function NewProduct() {
  const router = useRouter();
  
  const [formData, setFormData] = useState<Product>({
    name: '',
    content: '',
    image: '',
    price: ''
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    fetch("");
  };


  return (
    <>
      <div className="min-h-screen bg-stone-50 p-8">
        <section className="max-w-5xl mx-auto bg-white rounded-xl shadow-sm overflow-hidden border border-stone-200 pt-32 pb-20 px-4">
          <div className="min-h-screen py-12 px-4">
            <div className="max-w-md mx-auto bg-white rounded-2xl shadow-sm border border-stone-200 p-8">
              <h2 className="text-2xl font-bold text-stone-800 mb-6 border-b border-stone-100 pb-4">
                신규 상품 등록
              </h2>

              <form onSubmit={handleSubmit} className="space-y-5">
                <div>
                  <label className="block text-sm font-semibold text-stone-700 mb-1">상품명</label>
                  <input
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    placeholder="예: 에스프레소 블렌드"
                    className="w-full px-4 py-2 rounded-lg border border-stone-300 focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none transition"
                    required
                  />
                </div>

                {/* 설명 */}
                <div>
                  <label className="block text-sm font-semibold text-stone-700 mb-1">내용</label>
                  <textarea
                    name="content"
                    value={formData.content}
                    onChange={handleChange}
                    placeholder="상품에 대한 설명을 입력하세요"
                    rows={3}
                    className="w-full px-4 py-2 rounded-lg border border-stone-300 focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none transition"
                    required
                  />
                </div>

                {/* 이미지 URL */}
                <div>
                  <label className="block text-sm font-semibold text-stone-700 mb-1">이미지 URL</label>
                  <input
                    type="text"
                    name="image"
                    value={formData.image}
                    onChange={handleChange}
                    placeholder="https://example.com/image.jpg"
                    className="w-full px-4 py-2 rounded-lg border border-stone-300 focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none transition"
                  />
                </div>

                {/* 가격 */}
                <div>
                  <label className="block text-sm font-semibold text-stone-700 mb-1">가격</label>
                  <div className="relative">
                    <input
                      type="text"
                      name="price"
                      value={formData.price}
                      onChange={handleChange}
                      placeholder="0"
                      className="w-full pl-4 pr-10 py-2 rounded-lg border border-stone-300 focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none transition"
                      required
                    />
                    <span className="absolute right-3 top-2 text-stone-500">원</span>
                  </div>
                </div>

                {/* 버튼 세트 */}
                <div className="flex gap-3 pt-4">
                  <button
                    type="button"
                    className="flex-1 py-3 rounded-xl border border-stone-300 text-stone-600 font-medium hover:bg-stone-50 transition hover:cursor-pointer"
                    onClick={()=>router.push("/admin")}
                  >
                    취소
                  </button>
                  <button
                    type="submit"
                    className="flex-1 py-3 rounded-xl bg-amber-700 text-white font-semibold hover:bg-amber-800 shadow-md shadow-amber-900/10 transition hover:cursor-pointer"
                  >
                    등록하기
                  </button>
                </div>
              </form>
            </div>
          </div>
        </section>
      </div>

    </>
  );
}