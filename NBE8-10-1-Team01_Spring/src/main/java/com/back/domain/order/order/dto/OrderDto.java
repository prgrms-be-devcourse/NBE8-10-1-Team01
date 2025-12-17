package com.back.domain.order.order.dto;

import com.back.domain.order.order.entity.Orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {

    private long orderId;
    private long customerId;
    private String customerEmail;
    private String customerAddress;
    private String customerPostcode;
    private LocalDateTime createDate;
    private List<OrderItemDto> orderItems;


    public OrderDto(Orders order) {
        this.orderId = order.getId();
        this.customerId = order.getCustomer().getId();
        this.customerEmail = order.getCustomer().getEmail();
        this.customerAddress = order.getCustomer().getAddress();
        this.customerPostcode = order.getCustomer().getPostcode();
        this.createDate = order.getCreateDate();
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(Collectors.toList());
    }
    public long getOrderId() { return orderId; }
    public long getCustomerId() { return customerId; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerAddress() { return customerAddress; }
    public String getCustomerPostcode() { return customerPostcode; }
    public LocalDateTime getCreateDate() { return createDate; }
    public List<OrderItemDto> getOrderItems() { return orderItems; }

    public void setOrderId(long orderId) { this.orderId = orderId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }
    public void setCustomerPostcode(String customerPostcode) { this.customerPostcode = customerPostcode; }
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }
    public void setOrderItems(List<OrderItemDto> orderItems) { this.orderItems = orderItems; }
}