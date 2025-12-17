package com.back.domain.order.order.controller;

import com.back.domain.order.order.dto.*;
import com.back.domain.order.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "주문 API")
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping
    @Operation(
            summary = "주문 생성",
            description = "고객 정보와 주문 상품 목록으로 주문을 생성합니다."
    )
    @ApiResponse(
            responseCode = "201",
            description = "주문 생성 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OrderCreateResponse.class),
                    examples = @ExampleObject(
                            name = "주문 생성 성공 예시",
                            value = """
                                    {
                                      "orderId": 1,
                                      "createAt": "2025-12-17T10:30:00"
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
                                      "error": "고객 정보는 필수입니다"
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<OrderCreateResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        OrderCreateResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // customer별 주문 아이템 조회
    @GetMapping("/{customerId}")
    @Operation(
            summary = "고객별 주문 아이템 조회",
            description = "특정 고객의 주문 아이템 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "주문 아이템 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OrderListResponse.class),
                    examples = @ExampleObject(
                            name = "주문 아이템 목록 예시",
                            value = """
                                    {
                                      "data": [
                                        {
                                          "productId": 2,
                                          "productName": "수정",
                                          "count": 2,
                                          "price": 1000
                                        },
                                        {
                                          "productId": 3,
                                          "productName": "선풍기",
                                          "count": 1,
                                          "price": 50000
                                        }
                                      ]
                                    }
                                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "고객을 찾을 수 없음",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "error": "고객을 찾을 수 없습니다"
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<OrderListResponse<List<OrderItemDto>>> getUserOrders(@PathVariable Long customerId) {
        return ResponseEntity.ok(OrderListResponse.of(orderService.getUserOrders(customerId)));
    }

    //모든 주문 내역 조회
    @GetMapping
    @Operation(
            summary = "모든 주문 내역 조회",
            description = "전체 주문 내역을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "주문 내역 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OrderListResponse.class),
                    examples = @ExampleObject(
                            name = "주문 내역 목록 예시",
                            value = """
                                    {
                                      "data": [
                                        {
                                          "orderId": 1,
                                          "customerId": 1,
                                          "customerEmail": "test@example.com",
                                          "customerAddress": "서울시 강남구",
                                          "customerPostcode": "12345",
                                          "createDate": "2025-12-17T10:30:00",
                                          "orderItems": [
                                            {
                                              "productId": 2,
                                              "productName": "수정",
                                              "count": 2,
                                              "price": 1000
                                            }
                                          ]
                                        }
                                      ]
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<OrderListResponse<List<OrderDto>>> getAllOrders() {
        return ResponseEntity.ok(OrderListResponse.of(orderService.getAllOrders()));
    }


    @DeleteMapping("/{orderId}")
    @Operation(
            summary = "주문 삭제",
            description = "주문 ID로 주문을 삭제합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "주문 삭제 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OrderDeleteResponse.class),
                    examples = @ExampleObject(
                            name = "주문 삭제 성공 예시",
                            value = """
                                    {
                                      "message": "주문 삭제가 완료되었습니다"
                                    }
                                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "주문을 찾을 수 없음",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "error": "주문을 찾을 수 없습니다"
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<OrderDeleteResponse> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok(new OrderDeleteResponse("주문 삭제가 완료되었습니다"));
    }

}