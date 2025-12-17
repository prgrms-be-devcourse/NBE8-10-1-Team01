"use client"

import { useState } from 'react';

export default function OrderLookupForm() {
  const [email, setEmail] = useState('');

  const handleSubmit = () => {
    if (email) {
      console.log('조회할 이메일:', email);
      alert(`이메일 조회: ${email}`);
    }
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

          <div className="mb-4">
            <label
              htmlFor="email-input"
              className="block text-sm font-medium text-amber-900 mb-2"
            >
              이메일 주소
            </label>
            <input
              id="email-input"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="your@email.com"
              className="w-full px-4 py-3 border border-amber-300 rounded-lg text-amber-900 placeholder:text-amber-400 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-amber-500"
            />
          </div>

          <button
            onClick={handleSubmit}
            className="w-full bg-amber-500 text-white font-semibold py-3 px-4 rounded-lg hover:bg-amber-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-amber-500"
          >
            주문 내역 조회하기
          </button>

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