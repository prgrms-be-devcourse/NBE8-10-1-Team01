package com.back.domain.product.product.controller;

import com.back.domain.product.product.dto.ProductCreateRequest;
import com.back.domain.product.product.dto.ProductCreateResponse;
import com.back.domain.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductCreateResponse> createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") Integer price,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("image") MultipartFile image) {

        // DTO 생성
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName(name);
        request.setPrice(price);
        request.setDescription(description);


        ProductCreateResponse response = productService.createProduct(request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
