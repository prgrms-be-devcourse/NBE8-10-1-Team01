package com.back.domain.product.product.service;

import com.back.domain.customer.customer.repository.CustomerRepository;
import com.back.domain.product.product.dto.ProductCreateRequest;
import com.back.domain.product.product.dto.ProductCreateResponse;
import com.back.domain.product.product.entity.Product;
import com.back.domain.product.product.repository.ProductRepository;

import com.back.global.exception.product.InvalidProductException;
import com.back.global.fileStorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request, MultipartFile image) {
        // 입력값 검증
        validateProductRequest(request);

        // 파일 저장
        String imagePath = fileStorageService.storeFile(image);

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setImagePath(imagePath);


        // 저장
        Product savedProduct = productRepository.save(product);

        ProductCreateResponse response = new ProductCreateResponse();
        response.setProductId(savedProduct.getId());
        response.setMessage("상품 등록이 완료되었습니다");
        return response;

    }

    private void validateProductRequest(ProductCreateRequest request) {
        // 필수 필드 검증
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new InvalidProductException("상품명은 필수입니다.");
        }

        if (request.getPrice() == null) {
            throw new InvalidProductException("가격은 필수입니다.");
        }

        // 가격 범위 검증
        if (request.getPrice() <= 0) {
            throw new InvalidProductException("가격은 0보다 커야 합니다.");
        }
    }

}
