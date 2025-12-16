package com.back.domain.product.service;

import com.back.domain.product.product.dto.ProductUpdateRequest;
import com.back.domain.product.product.entity.Product;
import com.back.domain.product.product.repository.ProductRepository;
import com.back.domain.product.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
//@Transactional
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("상품 정보 수정")
    void updateProductTest() {
        Product savedProduct = productRepository.save(Product.builder()
                .name("원래 이름")
                .price(1000)
                .description("원래 설명")
                .imagePath("old_image.jpg")
                .build());

        ProductUpdateRequest updateRequest = new ProductUpdateRequest();
        updateRequest.setName("이름 수정");
        updateRequest.setPrice(2000);
        updateRequest.setDescription("설명 수정");

        productService.updateProduct(savedProduct.getId(), updateRequest, null);

        Product updatedProduct = productRepository.findById(savedProduct.getId()).get();

        assertThat(updatedProduct.getName()).isEqualTo("이름 수정");
        assertThat(updatedProduct.getPrice()).isEqualTo(2000);
        assertThat(updatedProduct.getDescription()).isEqualTo("설명 수정");
    }
}