"use client"

import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";


interface Product {
  name: string;
  description: string;
  price: string;
}

export default function DetailProduct() {
  const { productId: productIdStr } = useParams();
  const productId = Number(productIdStr);
  const router = useRouter();
  const [formData, setFormData] = useState<Product>({
    name: '',
    description: '',
    price: ''
  });
  const [image, setImage] = useState<File | null>(null);
  const [preview, setPreview] = useState("");

  const SERVER_URL = "http://localhost:8080";
  const headers = new Headers();
  headers.set("Content-Type", "application/json; charset=utf-8");
  useEffect(() => {
    fetch(`${SERVER_URL}/api/products/${productId}`, { headers: headers })
      .then(res => res.json())
      .then(data => {
        setFormData(data);
        if (data.image) {
          setPreview(SERVER_URL + data.image);
        }
      })
  }, []);

  useEffect(() => {
    if (!image) {
      return;
    }
    const url = URL.createObjectURL(image);
    setPreview(url);

    return () => URL.revokeObjectURL(url);
  }, [image]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const form = e.target as HTMLFormElement;
    const nameInput = form.elements.namedItem("name") as HTMLInputElement;
    const priceInput = form.elements.namedItem("price") as HTMLInputElement;
    const descriptionInput = form.elements.namedItem("description") as HTMLInputElement;

    const htmlFormData = new FormData();
    htmlFormData.append("name", nameInput.value.trim());
    htmlFormData.append("price", priceInput.value.trim());
    htmlFormData.append("description", descriptionInput.value.trim());
    if (image) {
      htmlFormData.append("image", image);
    }

    fetch(`${SERVER_URL}/api/products/${productId}`, {
      method: "PUT",
      body: htmlFormData
    })
    .then(res => {
      if (res.ok){
        alert("수정되었습니다.");
      }else{
        alert("수정에 실패하셨습니다.");
      }
    });
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setImage(e.target.files[0]);
    }
  };

  return (
    <>
      <div className="min-h-screen bg-stone-50 p-8">
        <section className="max-w-5xl mx-auto bg-white rounded-xl shadow-sm overflow-hidden border border-stone-200 pt-32 pb-20 px-4">
          <div className="min-h-screen py-12 px-4">
            <div className="max-w-md mx-auto bg-white rounded-2xl shadow-sm border border-stone-200 p-8">
              <h2 className="text-2xl font-bold text-stone-800 mb-6 border-b border-stone-100 pb-4">
                상품 상세페이지
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
                  />
                </div>

                {/* 설명 */}
                <div>
                  <label className="block text-sm font-semibold text-stone-700 mb-1">내용</label>
                  <textarea
                    name="description"
                    value={formData.description}
                    onChange={handleChange}
                    placeholder="상품에 대한 설명을 입력하세요"
                    rows={3}
                    className="w-full px-4 py-2 rounded-lg border border-stone-300 focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none transition"
                  />
                </div>

                {/* 이미지 URL */}
                {/* <div>
                  <label className="block text-sm font-semibold text-stone-700 mb-1">이미지</label>
                  <input
                    type="text"
                    name="image"
                    value={SERVER_URL + formData.image}
                    onChange={handleChange}
                    placeholder="https://example.com/image.jpg"
                    className="w-full px-4 py-2 rounded-lg border border-stone-300 focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none transition"
                  />
                </div> */}

                <div>
                  <label className="block text-sm font-semibold text-stone-700 mb-1">이미지</label>
                  <label
                    htmlFor="file-upload"
                    className="cursor-pointer flex items-center justify-between border border-stone-300 rounded-lg px-4 py-2 hover:bg-gray-50 transition"
                  >
                    <span className="text-gray-400">
                      {
                        image ? image.name : formData.name
                      }
                    </span>
                    <span className="bg-blue-500 text-white px-3 py-1 rounded text-sm">
                      찾아보기
                    </span>
                  </label>
                  <input
                    type="file"
                    id="file-upload"
                    accept="image/*"
                    onChange={handleFileChange}
                    className="hidden w-full px-4 py-2 rounded-lg border border-stone-300 focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none transition hover:cursor-pointer" />
                </div>
                {preview &&
                  <img className="object-contain w-full"
                    src={preview}
                    alt={formData.name}
                  />
                }

                {/* 가격 */}
                <div>
                  <label className="block text-sm font-semibold text-stone-700 mb-1">가격</label>
                  <div className="relative">
                    <input
                      type="number"
                      name="price"
                      value={formData.price}
                      onChange={handleChange}
                      placeholder="0"
                      className="w-full pl-4 pr-10 py-2 rounded-lg border border-stone-300 focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none transition"
                    />
                    <span className="absolute right-3 top-2 text-stone-500">원</span>
                  </div>

                </div>

                <div className="flex gap-3 pt-4">
                  <button
                    type="submit"
                    className="flex-1 py-3 rounded-xl bg-amber-700 text-white font-semibold hover:bg-amber-800 shadow-md shadow-amber-900/10 transition hover:cursor-pointer"
                  >
                    수정하기
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