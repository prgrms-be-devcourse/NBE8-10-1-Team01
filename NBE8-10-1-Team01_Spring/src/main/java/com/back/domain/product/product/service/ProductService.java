package com.back.domain.product.product.service;

import com.back.domain.product.product.dto.*;
import com.back.domain.product.product.entity.Product;
import com.back.domain.product.product.repository.ProductRepository;
import com.back.global.exception.product.InvalidProductException;
import com.back.global.exception.product.ProductNotFoundException;
import com.back.global.fileStorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;


    public ProductListResponse getProducts() {
        // 전체 상품 조회
        List<Product> products = productRepository.findAll();

        List<ProductListResponse.ProductDto> productDtos = new ArrayList<>();

        for (Product product : products) {
            ProductListResponse.ProductDto dto = new ProductListResponse.ProductDto();
            dto.setProductId(product.getId());
            dto.setName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setImage("/api/products/images/" + product.getImagePath());

            productDtos.add(dto);
        }

        ProductListResponse response = new ProductListResponse();
        response.setData(productDtos);
        return response;
    }


    public ProductDetailResponse getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));

        ProductDetailResponse response = new ProductDetailResponse();
        response.setProductId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescription());
        response.setImage("/api/products/images/" + product.getImagePath());

        return response;
    }


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

    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequest request, MultipartFile image) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new InvalidProductException("해당 상품이 없습니다.id=" + productId));

        if (request.getName() != null) product.setName(request.getName());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.storeFile(image);
            product.setImagePath(imagePath);
        }
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
