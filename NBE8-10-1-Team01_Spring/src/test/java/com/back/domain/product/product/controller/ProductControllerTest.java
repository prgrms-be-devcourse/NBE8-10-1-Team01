package com.back.domain.product.product.controller;

import com.back.domain.customer.customer.entity.Customer;
import com.back.domain.customer.customer.repository.CustomerRepository;
import com.back.domain.product.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        customerRepository.deleteAll();

        testCustomer = Customer.builder()
                .email("test@example.com")
                .address("서울시 강남구")
                .postcode("12345")
                .build();
        testCustomer = customerRepository.save(testCustomer);
    }

    @Test
    @DisplayName("상품 등록 성공 - 201 Created")
    void createProduct_Success() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/products")
                        .file(image)
                        .param("name", "테스트 상품")
                        .param("price", "10000")
                        .param("description", "테스트 상품 설명")
                        .param("customerId", testCustomer.getId().toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId", notNullValue()))
                .andExpect(jsonPath("$.message", is("상품 등록이 완료되었습니다")));
    }


    @Test
    @DisplayName("상품 등록 실패 - 필수 필드 누락 (name)")
    void createProduct_Fail_MissingName() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/products")
                        .file(image)
                        .param("price", "10000")
                        .param("description", "테스트 상품 설명")
                        .param("customerId", testCustomer.getId().toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 등록 실패 - 필수 필드 누락 (price)")
    void createProduct_Fail_MissingPrice() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/products")
                        .file(image)
                        .param("name", "테스트 상품")
                        .param("description", "테스트 상품 설명")
                        .param("customerId", testCustomer.getId().toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 등록 실패 - 가격이 0보다 작음")
    void createProduct_Fail_NegativePrice() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/products")
                        .file(image)
                        .param("name", "테스트 상품")
                        .param("price", "-1000")
                        .param("description", "테스트 상품 설명")
                        .param("customerId", testCustomer.getId().toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}

