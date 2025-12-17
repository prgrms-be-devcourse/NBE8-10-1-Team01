package com.back.domain.order.order.service;

import com.back.domain.customer.customer.entity.Customer;
import com.back.domain.customer.customer.repository.CustomerRepository;
import com.back.domain.order.order.dto.OrderCreateRequest;
import com.back.domain.order.order.dto.OrderCreateResponse;
import com.back.domain.order.order.dto.OrderDto;
import com.back.domain.order.order.dto.OrderItemDto;
import com.back.domain.order.order.entity.Orders;
import com.back.domain.order.order.repository.OrderRepository;
import com.back.domain.product.product.entity.Product;
import com.back.domain.product.product.repository.ProductRepository;
import com.back.global.exception.order.InvalidOrderException;
import com.back.global.exception.order.OrderNotFoundException;
import com.back.global.exception.product.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<OrderItemDto> getUserOrders(Long customerId) {
        return orderRepository.findByCustomer_IdOrderByCreateDateDesc(customerId)
                .stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(OrderItemDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderDto::new)
                .toList();
    }

    public OrderCreateResponse createOrder(OrderCreateRequest request) {
        validateOrderRequest(request);

        String email = request.getCustomer().getEmail().trim();

        Customer customer = customerRepository.findByEmail(email)
                .orElseGet(() -> customerRepository.save(
                        Customer.builder()
                                .email(email)
                                .address(request.getCustomer().getAddress())
                                .postcode(request.getCustomer().getPostcode())
                                .build()
                ));

        Orders order = new Orders();
        order.assignCustomer(customer);

        for (OrderCreateRequest.ProductDto p : request.getProducts()) {
            Product product = productRepository.findById(p.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + p.getProductId()));

            order.addItem(product, p.getCount());
        }

        Orders saved = orderRepository.save(order);

        List<OrderItemDto> items = saved.getOrderItems().stream()
                .map(OrderItemDto::new)
                .toList();

        OrderCreateResponse response = new OrderCreateResponse();
        response.setOrderId(saved.getId());
        response.setCustomerId(saved.getCustomer().getId());
        response.setCreateDate(saved.getCreateDate());
        response.setItems(items);
        response.setMessage("주문이 완료되었습니다");
        return response;
    }

    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException("해당 주문이 없습니다. ID: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    private void validateOrderRequest(OrderCreateRequest request) {
        if (request == null) throw new InvalidOrderException("요청이 비어있습니다.");
        if (request.getCustomer() == null) throw new InvalidOrderException("고객 정보가 필요합니다.");

        if (request.getCustomer().getEmail() == null || request.getCustomer().getEmail().trim().isEmpty())
            throw new InvalidOrderException("이메일은 필수입니다.");

        if (request.getCustomer().getAddress() == null || request.getCustomer().getAddress().trim().isEmpty())
            throw new InvalidOrderException("주소는 필수입니다.");

        if (request.getCustomer().getPostcode() == null || request.getCustomer().getPostcode().trim().isEmpty())
            throw new InvalidOrderException("우편번호는 필수입니다.");

        if (request.getProducts() == null || request.getProducts().isEmpty())
            throw new InvalidOrderException("주문 상품 목록이 비어있습니다.");

        for (OrderCreateRequest.ProductDto p : request.getProducts()) {
            if (p.getProductId() == null)
                throw new InvalidOrderException("상품 ID는 필수입니다.");
            if (p.getCount() == null)
                throw new InvalidOrderException("수량은 필수입니다. productId=" + p.getProductId());
            if (p.getCount() <= 0)
                throw new InvalidOrderException("수량은 1 이상이어야 합니다. productId=" + p.getProductId());
        }
    }

}
