package com.back.domain.order.order.entity;

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

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    @Column(nullable = false)
    private List<OrderItem> orderItems = new ArrayList<>();


    public Orders(Long userId) {
        this.userId = userId;
        this.createDate = LocalDateTime.now();
    }
    public void addItem(
            Long productId,
            String productName,
            Integer productPrice,
            Integer count
    ) {
        this.orderItems.add(
                new OrderItem(this, productId, productName, productPrice, count)
        );
    }
}
