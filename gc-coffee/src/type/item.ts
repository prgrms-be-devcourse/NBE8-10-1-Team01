export type ProductDto  = {
    productId: number;
    name: string;
    description: string;
    image: string;
    price: string;
}
  
export type OrderDto ={
  product:ProductDto
  count: number;
}

