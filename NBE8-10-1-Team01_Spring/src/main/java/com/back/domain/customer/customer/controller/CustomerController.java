package com.back.domain.customer.customer.controller;

import com.back.domain.customer.customer.dto.CustomerLoginRequest;
import com.back.domain.customer.customer.dto.CustomerLoginResponse;
import com.back.domain.customer.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "고객 API")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(
            summary = "고객 로그인",
            description = "이메일을 확인하여 로그인 처리 후 고객 ID와 메시지를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CustomerLoginResponse.class),
                    examples = {
                            @ExampleObject(
                                    name = "로그인 성공",
                                    value = """
                                            {
                                              "customerId": 1,
                                              "message": "로그인 완료되었습니다"
                                            }
                                            """
                            )
                    }
            )
    )
    public ResponseEntity<CustomerLoginResponse> login(@RequestBody CustomerLoginRequest request) {
        CustomerLoginResponse response = customerService.checkLogin(request);
        return ResponseEntity.ok(response);
    }
}
