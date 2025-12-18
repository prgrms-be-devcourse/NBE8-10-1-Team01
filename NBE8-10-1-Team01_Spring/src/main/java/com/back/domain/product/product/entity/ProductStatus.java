package com.back.domain.product.product.entity;

/**
 * 상품 상태를 나타내는 Enum
 * ACTIVE: 활성 상태 (판매 중)
 * DELETED: 삭제된 상태 (소프트 삭제)
 */
public enum ProductStatus {
    ACTIVE,   // 활성 상태 (정상 판매)
    DELETED   // 삭제된 상태 (판매 중지)
}

