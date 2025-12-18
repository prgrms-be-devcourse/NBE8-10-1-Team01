package com.back.domain.product.product.repository;

import com.back.domain.product.product.entity.Product;
import com.back.domain.product.product.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JPA 기본 메서드
    List<Product> findByStatus(ProductStatus status);

    // 활성 상품 전체 조회
    default List<Product> findAllActive() {
        return findByStatus(ProductStatus.ACTIVE);
    }

    // ID로 활성 상품 조회
    default Optional<Product> findByIdAndActive(Long id) {
        return findById(id)
                .filter(p -> p.getStatus() == ProductStatus.ACTIVE);
    }

    // 삭제된 상품 조회 (관리자용)
    default List<Product> findAllDeleted() {
        return findByStatus(ProductStatus.DELETED);
    }
}
