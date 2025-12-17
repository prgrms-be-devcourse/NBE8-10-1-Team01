package com.back.domain.order.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class OrderCreateRequest {

    @NotNull(message = "고객 정보는 필수입니다.")
    @Valid
    private CustomerDto customer;

    @NotEmpty(message = "주문 상품 목록은 비어 있을 수 없습니다.")
    @Valid
    private List<ProductDto> products;

    public CustomerDto getCustomer() {
        return customer;
    }
    public List<ProductDto> getProducts() {
        return products;
    }
    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }
    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    // ================== inner DTOs ==================

    public static class CustomerDto {

        @NotBlank(message = "이메일은 필수입니다.")
        private String email;

        @NotBlank(message = "주소는 필수입니다.")
        private String address;

        @NotBlank(message = "우편번호는 필수입니다.")
        private String postcode;

        public String getEmail() {
            return email;
        }
        public String getAddress() {
            return address;
        }
        public String getPostcode() {
            return postcode;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public void setAddress(String address) {
            this.address = address;
        }
        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }
    }

    public static class ProductDto {
        @NotNull(message = "상품 ID는 필수입니다.")
        private Long productId;

        @NotNull(message = "수량은 필수입니다.")
        @Positive(message = "수량은 1 이상이어야 합니다.")
        private Integer count;

        public Long getProductId() {
            return productId;
        }
        public Integer getCount() {
            return count;
        }
        public void setProductId(Long productId) {
            this.productId = productId;
        }
        public void setCount(Integer count) {
            this.count = count;
        }
    }
}