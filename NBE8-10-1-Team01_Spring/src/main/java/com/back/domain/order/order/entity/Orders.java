package com.back.domain.order.order.entity;

import com.back.domain.product.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@AllArgsConstructor
@Builder
public class Orders {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // Customer ID

    @Column(nullable = false)
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    public Orders(Long userId) {
        this.userId = userId;
        this.createDate = LocalDateTime.now();
    }


    public void addItem(Product product, Integer count) {
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }
        this.orderItems.add(new OrderItem(this, product, count));
    }
}
