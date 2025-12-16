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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    @DisplayName("상품 목록 조회 성공 - 빈 목록")
    void getProducts_Success_EmptyList() throws Exception {
        // given - setUp()에서 이미 데이터 삭제됨

        // when & then
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(empty())));
    }

    @Test
    @DisplayName("상품 목록 조회 성공 - 단일 상품")
    void getProducts_Success_SingleProduct() throws Exception {
        // given - 상품 1개 등록
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        mockMvc.perform(multipart("/api/products")
                        .file(image)
                        .param("name", "테스트 상품")
                        .param("price", "10000")
                        .param("description", "테스트 상품 설명")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

        // when & then
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].productId", notNullValue()))
                .andExpect(jsonPath("$.data[0].name", is("테스트 상품")))
                .andExpect(jsonPath("$.data[0].price", is(10000)))
                .andExpect(jsonPath("$.data[0].image", notNullValue()))
                .andExpect(jsonPath("$.data[0].image", startsWith("/api/products/images/")));
    }

    @Test
    @DisplayName("상품 목록 조회 성공 - 여러 상품")
    void getProducts_Success_MultipleProducts() throws Exception {
        // given - 상품 3개 등록
        for (int i = 1; i <= 3; i++) {
            MockMultipartFile image = new MockMultipartFile(
                    "image",
                    "test-image-" + i + ".jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    ("test image content " + i).getBytes()
            );

            mockMvc.perform(multipart("/api/products")
                            .file(image)
                            .param("name", "테스트 상품 " + i)
                            .param("price", String.valueOf(10000 * i))
                            .param("description", "테스트 상품 설명 " + i)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isCreated());
        }

        // when & then
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].productId", notNullValue()))
                .andExpect(jsonPath("$.data[0].name", notNullValue()))
                .andExpect(jsonPath("$.data[0].price", notNullValue()))
                .andExpect(jsonPath("$.data[0].image", notNullValue()))
                .andExpect(jsonPath("$.data[1].productId", notNullValue()))
                .andExpect(jsonPath("$.data[2].productId", notNullValue()));
    }

    @Test
    @DisplayName("상품 목록 조회 성공 - 이미지 URL 형식 확인")
    void getProducts_Success_ImageUrlFormat() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test-image.png",
                MediaType.IMAGE_PNG_VALUE,
                "test image content".getBytes()
        );

        mockMvc.perform(multipart("/api/products")
                        .file(image)
                        .param("name", "이미지 테스트 상품")
                        .param("price", "15000")
                        .param("description", "이미지 URL 테스트")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

        // when & then
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].image", matchesPattern("/api/products/images/[a-f0-9\\-]+\\.png")));
    }
}

