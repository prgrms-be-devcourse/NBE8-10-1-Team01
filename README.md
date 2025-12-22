## 1. 프로젝트 개요

### 프로젝트명

카페 메뉴 관리 서비스

### 프로젝트 소개

Spring Framework를 활용하여 카페의 커피 메뉴 데이터를 관리하는 웹 서비스를 개발한다.

HTTP 메서드를 기반으로 **메뉴 생성, 조회, 수정, 삭제(CRUD)** 기능을 구현하는 것이 목표이다.

본 프로젝트는 백엔드 개발의 기본 구조와 REST API 설계에 대한 이해를 목적으로 진행되었다.

---

## 2. 일정 및 팀 구성

| 구분 | 내용 |
| --- | --- |
| 프로젝트 기간 | 12/15 (월) ~ 12/22 (월)  |
| 프로젝트 발표 | 12/22 (월) |
| 팀 구성 | 백엔드 3명 / 프론트엔드 1명 |

---

## 3. 기획 배경

소규모 로컬 카페 **‘Grids & Circles’** 는 온라인 웹사이트를 통해 커피 원두 패키지를 판매한다.

현재 카페에서는 상품 정보와 주문 처리를 체계적으로 관리할 수 있는 시스템이 필요하다.

또한 주문은 **전날 오후 2시부터 당일 오후 2시까지의 주문을 기준으로 묶어 처리**되며,

고객은 회원 가입 없이 **이메일 주소를 통해 주문을 구분**한다.

이러한 시나리오를 바탕으로, 카페의 상품 데이터를 효율적으로 관리할 수 있는

기본적인 백엔드 CRUD 시스템을 구현하게 되었다.

---

## 4. 주요 기능

### ☕ 메뉴 관리 기능

- 메뉴 등록 (Create)
- 전체 메뉴 조회 (Read)
- 특정 메뉴 수정 (Update)
- 특정 메뉴 삭제 (Delete)

### 🛒 주문 관리

- 주문 생성
    - 고객 이메일 기반 주문
    - 주문 시점에 따라 배송 기준일 결정 (오후 2시 기준)
- 주문 조회
    - 고객 이메일 기준 주문 조회
    - 기준 시간(오후 2시) 단위로 묶인 주문 확인

### 🔧 기술적 특징

- HTTP Method 기반 REST API 설계
    - `POST`, `GET`, `PUT`, `DELETE`
- 데이터베이스 연동
    - SQL 또는 ORM(JPA)을 활용한 데이터 관리

 ---

 ## 5. ERD
 
 ### 개요
 고객이 여러 주문을 생성하고, 각 주문에 여러 상품이 포함되는 구조를 가진다. 데이터 일관성을 위해 기본 키와 외래 키를 사용하고, 상품의 상태 관리를 위해 enum 타입을 활용.
<img width="858" height="442" alt="스크린샷 2025-12-18 오후 3 42 09" src="https://github.com/user-attachments/assets/9b1170f9-460e-411d-bdb7-6f4be02fa67f" />



### 엔티티 정의

#### PRODUCTS 테이블

- 목적: 상품 정보를 저장하는 엔티티로, 주문 시 참조되는 기본 데이터.
- 주요 컬럼:
    - `id` (PK, BIGINT, AUTO_INCREMENT): 상품을 유일하게 식별하는 키.
    - `name` (VARCHAR(255), NOT NULL): 상품명.
    - `description` (TEXT): 상품 설명.
    - `image_path` (VARCHAR(255), NOT NULL): 상품 이미지 경로.
    - `price` (INT, NOT NULL): 상품 가격.
    - `status` (ENUM('ACTIVE','DELETED'), NOT NULL): 상품의 노출/삭제 상태.

#### CUSTOMERS 테이블

- 목적: 주문을 생성하는 고객 정보를 저장하는 엔티티.
- 주요 컬럼:
    - `id` (PK, BIGINT, AUTO_INCREMENT): 고객 식별자.
    - `email` (VARCHAR(255), NOT NULL, UNIQUE): 고객 이메일, 중복을 허용하지 않는 고유 값.
    - `address` (VARCHAR(255), NOT NULL): 고객 주소.
    - `postcode` (VARCHAR(255), NOT NULL): 우편번호.

#### ORDERS 테이블

- 목적: 고객이 생성한 주문 정보를 저장하는 엔티티.
- 주요 컬럼:
    - `id` (PK, BIGINT, AUTO_INCREMENT): 주문 식별자.
    - `create_date` (DATETIME, NOT NULL): 주문 생성 일시.
    - `customer_id` (BIGINT, FK): 주문을 생성한 고객 ID, `customers.id`를 참조.

#### ORDER_ITEM 테이블

- 목적: 주문에 포함된 개별 상품과 수량 정보를 저장하는 주문 상세 엔티티.
- 주요 컬럼:
    - `id` (PK, BIGINT, AUTO_INCREMENT): 주문 상세 식별자.
    - `order_id` (BIGINT, FK): 상위 주문 ID, `orders.id`를 참조.
    - `product_id` (BIGINT, FK): 주문한 상품 ID, `products.id`를 참조.
    - `count` (INT): 주문한 상품 수량.

### 관계 정의

#### CUSTOMER – ORDER 관계

- 관계 유형: 1:N (한 고객은 여러 주문을 가질 수 있고, 하나의 주문은 한 고객에만 속함).
- 구현:
    - `orders.customer_id`가 `customers.id`를 참조하는 외래 키.
- 의미:
    - 한 고객이 여러 번 쇼핑을 할 수 있으며, 각 주문 기록은 해당 고객과 연결된다.

#### ORDER – ORDER_ITEM – PRODUCT 관계

- ORDER – ORDER_ITEM
    - 관계 유형: 1:N (한 주문에 여러 주문상품 레코드가 포함될 수 있음).
    - 구현: `order_item.order_id` → `orders.id` 외래 키.
- PRODUCT – ORDER_ITEM
    - 관계 유형: 1:N (하나의 상품이 여러 주문에 포함될 수 있음).
    - 구현: `order_item.product_id` → `products.id` 외래 키.
- 의미:
    - 주문과 상품 사이의 N:M 관계를 `order_item`이 풀어 주는 구조로, 한 주문에 여러 상품, 한 상품이 여러 주문에 포함되는 패턴을 표현한다.

#### 제약 및 비즈니스 규칙

- 기본 키 제약:
    - 각 테이블의 `id` 컬럼은 엔티티를 유일하게 구분하는 식별자 역할을 한다.
- 유니크 제약:
    - `customers.email`은 UNIQUE로, 같은 이메일을 가진 고객 계정이 중복 생성되지 않는다.
- 외래 키 제약:
    - `orders.customer_id` → `customers.id`: 존재하지 않는 고객에 대한 주문을 방지한다.
    - `order_item.order_id` → `orders.id`: 존재하지 않는 주문에 상세가 생기는 것을 방지한다.
    - `order_item.product_id` → `products.id`: 존재하지 않는 상품을 주문에 추가할 수 없다.
- 상태 관리:
    - `products.status`을 통해 상품을 물리적으로 삭제하지 않고도 노출/비노출을 제어하는 소프트 삭제 전략을 따른다.
