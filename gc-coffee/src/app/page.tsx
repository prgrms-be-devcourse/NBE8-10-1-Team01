"use client"

import { useEffect, useState } from 'react'

interface Product {
  name: string;
  description: string;
  image: string;
  price: string;
}

export default function Home() {
  const backgroundImage = "";
  const backgroundImageURL = `url('${backgroundImage}')`;


  const SERVER_URL = "localhost:8080";
  const [_brandImage,setBrandImage] = useState<Product>();
  useEffect(()=>{
    fetch("http:localhost:8080/api/products")
    .then(res=>res.json())
    .then(res=>setBrandImage(res.data))
  },[]);

  const brandImages = [
    { name: 'Ethiopia Yirgacheffe', image: 'bg-amber-100',description:"content1" },
    { name: 'Colombia Supremo', image: 'bg-stone-200' ,description:"content2"},
    { name: 'Kenya AA', image: 'bg-orange-100' ,description:"content3"}
  ];

  return (
    <div className="min-h-screen bg-amber-50">
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

      {/* Brand Gallery */}
      <section className="py-20 px-4 bg-gradient-to-b from-orange-100 to-amber-50">
        <div className="max-w-7xl mx-auto">
          <h2 className="text-4xl font-bold text-center mb-12">
            세계 각지에서 직구한 최상품!
          </h2>
          <p className="text-xl text-amber-700 text-center mb-16 max-w-3xl mx-auto">
            전 세계에서, 온라인 오프라인에서 판매합니다. 
          </p>

          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            {brandImages.map((brand, index) => (
              <div
                key={index}
                className={`${brand.image} rounded-2xl aspect-square flex flex-col items-center justify-center hover:scale-105 transition-transform cursor-pointer shadow-lg`}
              >
                <p className="text-2xl font-bold text-amber-900">{brand.name}</p>
                <p className="text-gray-600 text-sm mt-1">{brand.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-amber-900 text-amber-100 py-4 px-4">
        <div className="max-w-7xl mx-auto">
          <div className="border-t border-amber-800 pt-8 flex flex-col md:flex-row justify-between items-center">
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
    </div>
  )
}