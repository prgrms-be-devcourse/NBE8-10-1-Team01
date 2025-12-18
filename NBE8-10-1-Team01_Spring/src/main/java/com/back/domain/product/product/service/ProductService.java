package com.back.domain.product.product.service;

import com.back.domain.product.product.dto.*;
import com.back.domain.product.product.entity.Product;
import com.back.domain.product.product.entity.ProductStatus;
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
        // 활성 상태 상품만 조회 (소프트 삭제된 상품 제외)
        List<Product> products = productRepository.findAllActive();

        List<ProductListResponse.ProductDto> productDtos = new ArrayList<>();

        for (Product product : products) {
            ProductListResponse.ProductDto dto = new ProductListResponse.ProductDto();
            dto.setProductId(product.getId());
            dto.setName(product.getName());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setImage("/api/products/images/" + product.getImagePath());

            productDtos.add(dto);
        }

        ProductListResponse response = new ProductListResponse();
        response.setData(productDtos);
        return response;
    }


    public ProductDetailResponse getProduct(Long productId) {
        // 활성 상태 상품만 조회 (삭제된 상품은 조회 불가)
        Product product = productRepository.findByIdAndActive(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다"));

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

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .imagePath(imagePath)
                .status(ProductStatus.ACTIVE)  // 신규 상품은 활성 상태로 생성
                .build();

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
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다"));

        if (request.getName() != null) product.setName(request.getName());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.storeFile(image);
            product.setImagePath(imagePath);
        }
    }

    @Transactional
    public void deleteProduct(Long productId) {
        // 활성 상태 상품만 조회 가능
        Product product = productRepository.findByIdAndActive(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));

        // 소프트 삭제: 실제 DELETE 대신 상태만 변경
        product.setStatus(ProductStatus.DELETED);

        // JPA의 변경 감지(Dirty Checking)로 자동 UPDATE
        // productRepository.save() 호출 불필요
    }

    private void validateProductRequest(ProductCreateRequest request) {
        // 필수 필드 검증
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new InvalidProductException("상품명은 필수입니다");
        }

        if (request.getPrice() == null) {
            throw new InvalidProductException("가격은 필수입니다");
        }

        // 가격 범위 검증
        if (request.getPrice() <= 0) {
            throw new InvalidProductException("가격은 0보다 커야 합니다");
        }
    }


}
