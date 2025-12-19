"use client"

import { useEffect, useState } from 'react'

import { ProductDto } from '@/type/item';
import { apiFetch } from '@/lib/backend/client';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

function ProductList() {
  const [brandImages, setBrandImages] = useState<ProductDto[]|null>([]);
  useEffect(() => {
    apiFetch(`/api/products`)
      .then(res => setBrandImages(res.data))
  }, []);
  
  if(!brandImages){
    return(
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <p className="text-2xl font-bold text-amber-900">
          로딩 중입니다.
        </p>
      </div>
    );
  }
  if (brandImages.length == 0) {
    return (
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <p className="text-2xl font-bold text-amber-900">
          입고 중입니다.
        </p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
      {brandImages.map((brand, index) => (
        <div
          key={index}
          className={`rounded-2xl aspect-square flex flex-col items-center justify-center hover:scale-105 transition-transform cursor-pointer shadow-lg`}
        >
          <div className="relative aspect-square w-full overflow-hidden bg-gray-100">
            <img src={API_BASE_URL + brand.image}></img>
          </div>
          <div className="flex flex-col w-full">
            <p className="p-2 pb-0 text-2xl font-bold text-amber-900 truncate">{brand.name}</p>
            <p className="p-2 pt-0 text-gray-600 text-sm mt-1">{brand.description}</p>
          </div>
        </div>
      ))}
    </div>
  );
}

function Footer() {
  return (
    <footer className="bg-amber-900 text-amber-100 py-4 px-4">
      <div className="max-w-7xl mx-auto">
        <div className="border-amber-800 pt-3 flex flex-col md:flex-row justify-between items-center">
          <p className="text-sm mb-4 md:mb-0">© 2025 Shopify. All rights reserved.</p>
          <div className="flex items-center space-x-4">
            <a href="#" className="text-sm hover:text-orange-200 transition-colors">서비스 약관</a>
            <a href="#" className="text-sm hover:text-orange-200 transition-colors">개인정보처리방침</a>
            <select className="bg-amber-800 text-amber-100 px-3 py-1 rounded text-sm border border-amber-700">
              <option>대한민국 | 한국어</option>
            </select>
          </div>
        </div>
      </div>
    </footer>
  );
}

function Header() {
  return (
    <section className="pt-32 pb-20 px-4">
      <div className="max-w-7xl mx-auto text-center">
        <div className="mb-6">
          <p className="text-lg text-orange-700 mb-2">최상의 경험</p>
          <h1 className="text-6xl md:text-8xl font-bold mb-4">
            <span className="bg-gradient-to-r from-amber-800 via-orange-700 to-amber-700 bg-clip-text text-transparent animate-pulse">
              Grids & Circles
            </span>
          </h1>
        </div>

        <p className="text-xl text-amber-800 mb-12 max-w-3xl mx-auto">
          그저 더 나은 원두를 고객들에게 맛 보여 드리기 위해
        </p>
      </div>
    </section>
  );
}

export default function Home() {
  return (
    <div className="min-h-screen bg-amber-50">
      <Header />

      <section className="py-20 px-4 bg-gradient-to-b from-orange-100 to-amber-50">
        <div className="max-w-7xl mx-auto">
          <h2 className="text-4xl font-bold text-center mb-12">
            세계 각지에서 직구한 최상품!
          </h2>
          <p className="text-xl text-amber-700 text-center mb-16 max-w-3xl mx-auto">
            전 세계에서, 온라인 오프라인에서 판매합니다.
          </p>

          <ProductList />

        </div>
      </section>

      <Footer />

    </div>
  )
}