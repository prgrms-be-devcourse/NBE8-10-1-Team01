const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

export const apiFetch = (url:string,options?: RequestInit) => {
  return fetch(API_BASE_URL + url, options)
    .then(res => {
      if (!res.ok) throw res.json().catch(err=>{throw err});
      return res.json();
    });
};

