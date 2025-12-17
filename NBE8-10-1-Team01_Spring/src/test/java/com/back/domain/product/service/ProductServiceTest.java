package com.back.domain.product.service;

import com.back.domain.product.product.dto.ProductUpdateRequest;
import com.back.domain.product.product.entity.Product;
import com.back.domain.product.product.repository.ProductRepository;
import com.back.domain.product.product.service.ProductService;
import com.back.global.exception.product.InvalidProductException;
import com.back.global.exception.product.ProductNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
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

    @Test
    @DisplayName("상품 삭제")
    void deleteProductTest() {

        Product savedProduct = productRepository.save(Product.builder()
                .name("삭제 테스트용")
                .price(1000)
                .description("삭제 테스트 설명")
                .imagePath("delete.jpg")
                .build());

        Long productId = savedProduct.getId();

        productService.deleteProduct(productId);

        Optional<Product> deletedProduct = productRepository.findById(productId);

        assertThat(deletedProduct).isEmpty();
    }

    @Test
    @DisplayName("상품 삭제 존재하지 않는 ID")
    void deleteProduct_Fail(){
        Long ProductId = 9999L;

        assertThatThrownBy(() -> productService.deleteProduct(ProductId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("상품을 찾을 수 없습니다. ID: " + ProductId);
    }
}