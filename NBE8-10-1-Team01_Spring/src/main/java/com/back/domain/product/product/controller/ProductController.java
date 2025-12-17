package com.back.domain.product.product.controller;

import com.back.domain.product.product.dto.ProductCreateRequest;
import com.back.domain.product.product.dto.ProductCreateResponse;
import com.back.domain.product.product.dto.ProductDetailResponse;
import com.back.domain.product.product.dto.ProductUpdateRequest;
import com.back.domain.product.product.dto.ProductListResponse;
import com.back.domain.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "상품 API")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(
            summary = "상품 목록 조회",
            description = "등록된 상품 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "상품 목록 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductListResponse.class),
                    examples = @ExampleObject(
                            name = "상품 목록 예시",
                            value = """
                                    {
                                      "data": [
                                        {
                                          "productId": 2,
                                          "name": "수정",
                                          "price": 1000,
                                          "image": "/api/products/images/27f59056-b059-408e-a9ff-54078d07e15e.png"
                                        }
                                      ]
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<ProductListResponse> getProducts() {
        ProductListResponse response = productService.getProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "상품 상세 조회",
            description = "상품 ID로 상품 상세 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "상품 상세 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductDetailResponse.class),
                    examples = @ExampleObject(
                            name = "상품 상세 예시",
                            value = """
                                    {
                                      "productId": 2,
                                      "name": "수정",
                                      "price": 1000,
                                      "description": "수정된 상품 설명입니다",
                                      "image": "/api/products/images/27f59056-b059-408e-a9ff-54078d07e15e.png"
                                    }
                                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "상품을 찾을 수 없음",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "error": "상품을 찾을 수 없습니다"
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<ProductDetailResponse> getProduct(@PathVariable Long id) {
        ProductDetailResponse response = productService.getProduct(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "상품 등록",
            description = "이미지와 함께 상품을 등록합니다."
    )
    @ApiResponse(
            responseCode = "201",
            description = "상품 등록 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductCreateResponse.class),
                    examples = @ExampleObject(
                            name = "상품 등록 성공 예시",
                            value = """
                                    {
                                      "productId": 3,
                                      "message": "상품 등록이 완료되었습니다"
                                    }
                                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "error": "상품명은 필수입니다"
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<ProductCreateResponse> createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") Integer price,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("image") MultipartFile image) {

        ProductCreateRequest request = new ProductCreateRequest();
        request.setName(name);
        request.setPrice(price);
        request.setDescription(description);

        ProductCreateResponse response = productService.createProduct(request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "상품 수정",
            description = "이미지와 함께 상품 정보를 수정합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "상품 수정 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = @ExampleObject(value = "상품 수정 완료")
            )
    )
    public ResponseEntity<String> updateProduct(
            @PathVariable Long productId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "price", required = false) Integer price,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName(name);
        request.setPrice(price);
        request.setDescription(description);

        productService.updateProduct(productId, request, image);

        return ResponseEntity.ok("상품 수정 완료");
    }

    @DeleteMapping("/{productId}")
    @Operation(
            summary = "상품 삭제",
            description = "상품 ID로 상품을 삭제합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "상품 삭제 성공",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class),
                    examples = @ExampleObject(value = "상품 삭제 완료")
            )
    )
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("상품 삭제 완료");
    }
}
