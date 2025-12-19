package com.back.domain.order.order.entity;

import com.back.domain.customer.customer.entity.Customer;
import com.back.domain.product.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;


    public void assignCustomer(Customer customer) {
        this.customer = customer;
    }

    public void addItem(Product product, int count) {
        OrderItem item = new OrderItem(this, product, count);
        this.orderItems.add(item);
    }

    public void setCreateDateForTest(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * JPA 저장 전 createDate가 null이면 현재 시간으로 설정
     * 수동으로 설정된 경우 그대로 유지
     */
    @PrePersist
    protected void onCreate() {
        if (this.createDate == null) {
            this.createDate = LocalDateTime.now();
        }
    }
}