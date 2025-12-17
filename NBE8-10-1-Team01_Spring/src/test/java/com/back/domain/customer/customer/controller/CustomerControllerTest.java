package com.back.domain.customer.customer.controller;

import com.back.domain.customer.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("관리자 로그인 - admin 이메일로 /admin 리다이렉트")
    void login_Admin_Success() throws Exception {
        // given
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "admin@DevCourse.com");

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("로그인 완료되었습니다")))
                .andExpect(jsonPath("$.redirectUrl", is("/admin")));
    }

    @Test
    @DisplayName("고객 로그인 - 일반 이메일로 /orders 리다이렉트")
    void login_Customer_Success() throws Exception {
        // given
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "customer@test.com");

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("로그인 완료되었습니다")))
                .andExpect(jsonPath("$.redirectUrl", is("/orders")));
    }

    @Test
    @DisplayName("신규 고객 로그인 - DB에 자동 저장되고 /orders 리다이렉트")
    void login_NewCustomer_Success() throws Exception {
        // given
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "newcustomer@test.com");

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("로그인 완료되었습니다")))
                .andExpect(jsonPath("$.redirectUrl", is("/orders")));

        // 고객이 DB에 저장되었는지 확인
        assert customerRepository.findByEmail("newcustomer@test.com").isPresent();
    }

    @Test
    @DisplayName("여러 이메일 로그인 테스트")
    void login_MultipleEmails_Success() throws Exception {
        // 고객 1
        Map<String, String> customer1 = new HashMap<>();
        customer1.put("email", "user1@test.com");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.redirectUrl", is("/orders")));

        // 고객 2
        Map<String, String> customer2 = new HashMap<>();
        customer2.put("email", "user2@test.com");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.redirectUrl", is("/orders")));

        // 관리자
        Map<String, String> admin = new HashMap<>();
        admin.put("email", "admin@DevCourse.com");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.redirectUrl", is("/admin")));
    }
}

