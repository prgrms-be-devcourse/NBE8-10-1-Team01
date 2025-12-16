package com.back.domain.order.order.entity;

import com.back.domain.product.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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

    private Long userId;

    @CreatedDate
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Orders(Long userId) {
        this.userId = userId;
        this.createDate = LocalDateTime.now();
    }

    // Product 기반으로 addItem 수정
    public void addItem(com.back.domain.product.product.entity.Product product, int count) {
        this.orderItems.add(new OrderItem(this, product, count));
    }
}