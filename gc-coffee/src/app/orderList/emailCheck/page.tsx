"use client"

import { useRouter } from 'next/navigation';
import { useState } from 'react';

export default function OrderLookupForm() {
  const router = useRouter();
  
  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const form = e.target as HTMLFormElement;
    const email = form.elements.namedItem("email") as HTMLInputElement;
    email.value = email.value.trim();

    const SERVER_URL = "http://localhost:8080"
    fetch(`${SERVER_URL}/api/users`, {
      headers: {
        "Content-Type": "application/json"
      },
      method: "POST",
      body: JSON.stringify({
        email: email.value
      }),
    })
    .then(res=>{
      if(!res.ok){
        return res.json().then((err)=>{throw err;})
      }
      return res.json();
    })
    .then(data=>{
      router.replace(`/orderList/${data.customerId}`);
    }).catch((err)=>{
      alert("존재하지 않는 사용자입니다.")
    });
  };

  return (
    <div className="min-h-screen bg-amber-50">
      <section className="pt-32 pb-20 px-4">
        <div className="w-full max-w-sm mx-auto bg-white rounded-lg shadow-lg p-8">

          <h2 className="text-2xl font-bold text-amber-900 text-center mb-2">
            주문 조회
          </h2>
          <p className="text-sm text-amber-800 text-center mb-6">
            이메일 주소로 주문 내역을 확인하세요
          </p>

          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              
              <label
                htmlFor="email-input"
                className="block text-sm font-medium text-amber-900 mb-2"
              >
                이메일 주소
              </label>
              <input
                id="email"
                type="email"
                name="email"
                placeholder="your@email.com"
                className="w-full px-4 py-3 border border-amber-300 rounded-lg text-amber-900 placeholder:text-amber-400 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-amber-500"
              />
            </div>

            <button
              className="w-full bg-amber-500 text-white font-semibold py-3 px-4 rounded-lg hover:bg-amber-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-amber-500"
            >
              주문 내역 조회하기
            </button>
          </form>

          <p className="text-xs text-amber-700 text-center mt-6">
            주문 시 입력하신 이메일 주소를 정확히 입력해주세요
          </p>

          <div className="text-center mt-4 pt-4 border-t border-amber-200">
            <p className="text-sm text-amber-800">
              이메일을 찾을 수 없나요?{' '}
              <a href="#" className="font-medium text-amber-600 hover:text-amber-700">
                고객센터 문의
              </a>
            </p>
          </div>
        </div>
      </section>
    </div>

  );
}