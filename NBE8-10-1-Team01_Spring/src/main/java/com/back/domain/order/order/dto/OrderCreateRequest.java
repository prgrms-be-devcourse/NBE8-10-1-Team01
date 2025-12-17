package com.back.domain.order.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Schema(description = "주문 생성 요청")
public class OrderCreateRequest {

    @Schema(description = "고객 정보", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "고객 정보는 필수입니다.")
    @Valid
    private CustomerDto customer;

    @Schema(description = "주문 상품 목록", requiredMode = Schema.RequiredMode.REQUIRED)
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

    @Schema(description = "고객 정보")
    public static class CustomerDto {

        @Schema(description = "이메일", example = "customer@test.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "이메일은 필수입니다.")
        private String email;

        @Schema(description = "주소", example = "서울시 강남구", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "주소는 필수입니다.")
        private String address;

        @Schema(description = "우편번호", example = "12345", requiredMode = Schema.RequiredMode.REQUIRED)
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

    @Schema(description = "주문 상품 정보")
    public static class ProductDto {

        @Schema(description = "상품 ID", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "상품 ID는 필수입니다.")
        private Long productId;

        @Schema(description = "수량", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
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