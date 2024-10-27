# [MUSINSA] Java(Kotlin) Backend Engineer - 과제

- [0. 지원자 정보](#0-지원자-정보)
- [1. Quick Start](#1-quick-start)
    - [테스트 코드 실행](#테스트-코드-실행)
    - [서버 실행](#서버-실행)
- [2. 프로젝트 설명](#2-프로젝트-설명)
    - [2-1. 구현 범위](#2-1-구현-범위)
    - [2-2. 프로젝트 구조](#2-2-프로젝트-구조)
    - [2-3. 아키텍처 고려사항](#2-3-아키텍처-고려사항)
- [3. API 명세](#3-api-명세)
- [4. 기타 항목]

## 0. 지원자 정보

name: 김병준

email: kbj9704@gmail.com

phone: 010-4199-4157

## 1. Quick Start

### 테스트 코드 실행

```shell
./gradlew test
```

### 서버 실행

8080 포트로 서버가 실행됩니다.

```shell
./gradlew :bootRun
```

## 2. 프로젝트 설명

이 프로젝트는 패션 상품과 브랜드 데이터를 관리하고, 상품의 최저/최고 가격 및 브랜드와 카테고리별 가격 정보를 제공하는 서비스입니다.  
주요 목표는 효율적인 브랜드 및 카테고리 관리를 통해 다양한 상품의 가격 정보를 제공하며, 상품의 CRUD 및 가격 관련 기능을 지원합니다.

### 2-1. 구현 범위

1. 브랜드 관리
    - 생성, 조회, 수정, 삭제 API 제공
    - 브랜드별 카테고리 및 상품 관리
2. 카테고리 관리
    - 카테고리 생성, 조회, 수정, 삭제 API 제공
    - 카테고리별 상품의 최저가 및 최고가 조회 기능
3. 상품 관리
    - 상품 생성, 조회, 수정, 삭제 API 제공
    - 브랜드와 카테고리 간의 매핑 및 상품 가격 업데이트
4. 가격 정보 제공
    - 최저가 브랜드: 모든 카테고리를 포함하고 최저가 가격 합산이 가장 낮은 브랜드를 조회하는 기능
    - 카테고리별 최저/최고 가격 조회: 특정 카테고리 내에서 최저가 상품과 최고가 상품 조회 기능
    - 브랜드별 카테고리 합산 최저가 브랜드 조회: 가장 많은 카테고리를 보유한 브랜드 중에서, 카테고리 상품 가격 합산이 가장 낮은 브랜드를 조회

### 2-2. 프로젝트 구조

이 프로젝트는 3-Tier Architecture를 기반으로 설계되었습니다.

1. Presentation Layer (프레젠테이션 계층)

   이 계층은 사용자와 상호작용하는 부분으로, 주로 사용자로부터의 HTTP 요청을 처리하고 응답을 반환합니다. 이 계층은 Controller 클래스들로 구성되며, 사용자의 요청을 받아 서비스 계층으로 전달하고, 다시 서비스 계층으로부터 처리 결과를 받아 클라이언트에게 응답을 반환합니다.
    - 구현 요소
        - `BrandController`
        - `CategoryController`
        - `ProductController`
        - `PriceController`
    - 역할
        - 사용자의 요청 수신 및 검증
        - 서비스 계층으로 요청 전달
        - 클라이언트에게 응답 반환

2. Business Logic Layer (비즈니스 로직 계층)

   비즈니스 로직 계층은 애플리케이션의 핵심 로직을 처리하는 계층입니다. Presentation Layer로부터 전달된 요청에 대해 실제로 비즈니스 로직을 수행하며, 데이터 접근 계층으로부터 데이터를 가져오거나 갱신 작업을 처리합니다.
    - 구현 요소
        - `BrandService`
        - `CategoryService`
        - `ProductService`
        - `PriceService`
        - `BrandCategoryService`
    - 역할:
        - 비즈니스 로직 수행
        - 데이터 가공 및 처리
        - 데이터 접근 계층과의 상호작용 관리

3. Data Access Layer (데이터 접근 계층)

   이 계층은 데이터베이스와의 상호작용을 관리합니다. Repository 패턴을 통해 데이터베이스에 대한 CRUD 작업을 수행하며, 데이터베이스 접근 관련된 로직을 캡슐화하여 비즈니스 로직 계층에서 사용될 수 있도록 제공합니다.
    - 구현 요소
        - `BrandRepository`
        - `CategoryRepository`
        - `ProductRepository`
        - `BrandCategoryRepository`
    - 역할
        - 데이터베이스와의 상호작용 (CRUD 작업)
        - 쿼리 실행 및 결과 반환
        - 비즈니스 계층에 데이터 전달

### 2-3. 아키텍처 고려사항
- 기본적으로 다수의 상품이 존재하고 잦은 변경이 발생할 것을 가정하였습니다.
- 스케일 아웃 환경이 고려되지 않아서 기본적으로 로컬 캐시를 사용하였습니다.
- 각 리소스(브랜드, 카테고리, 상품)의 벌크 업데이트는 고려하지 않았습니다. 기본적으로 단건 수정이 발생할 것을 가정합니다.
- DB는 h2를 사용하였습니다.

## 3. API 명세

1. Brand API
    - 브랜드 목록 조회
        - GET `http://localhost:8080/api/brands`
           ```shell
           curl --location 'http://localhost:8080/api/brands'
           ```
    - 특정 브랜드 조회
        - GET `http://localhost:8080/api/brands/{brandId}`
           ```shell
           curl --location 'http://localhost:8080/api/brands/1'
           ```
    - 브랜드 생성
        - POST `http://localhost:8080/api/brands`
           ```shell
           curl --request POST \
             --url http://localhost:8080/api/brands \
             --header 'Content-Type: application/json' \
             --data '{
               "name": "Nike"
             }'
           ```
    - 브랜드 수정
        - PUT `http://localhost:8080/api/brands/{brandId}`
           ```shell
           curl --request PUT \
             --url http://localhost:8080/api/brands/1 \
             --header 'Content-Type: application/json' \
             --data '{
               "name": "Adidas"
             }'
           ```
    - 브랜드 삭제
        - DELETE `http://localhost:8080/api/brands/{brandId}`
           ```shell
           curl --request DELETE \
             --url http://localhost:8080/api/brands/1
           ```

2. Category API
    - 카테고리 목록 조회
        - GET `http://localhost:8080/api/categories`
           ```shell
           curl --location 'http://localhost:8080/api/categories'
           ```
    - 특정 카테고리 조회
        - GET `http://localhost:8080/api/categories/{categoryId}`
           ```shell
           curl --location 'http://localhost:8080/api/categories/1'
           ```
    - 카테고리 생성
        - POST `http://localhost:8080/api/categories`
           ```shell
           curl --request POST \
             --url http://localhost:8080/api/categories \
             --header 'Content-Type: application/json' \
             --data '{
               "name": "Shoes"
             }'
           ```
    - 카테고리 수정
        - PUT `http://localhost:8080/api/categories/{categoryId}`
           ```shell
           curl --request PUT \
             --url http://localhost:8080/api/categories/1 \
             --header 'Content-Type: application/json' \
             --data '{
               "name": "Clothes"
             }'
           ```
    - 카테고리 삭제
        - DELETE `http://localhost:8080/api/categories/{categoryId}`
           ```shell
           curl --request DELETE \
             --url http://localhost:8080/api/categories/1
           ```

3. Product API
    - 상품 목록 조회
        - GET `http://localhost:8080/api/products`
           ```shell
           curl --location 'http://localhost:8080/api/products'
           ```
    - 특정 상품 조회
        - GET `http://localhost:8080/api/products/{productId}`
           ```shell
           curl --location 'http://localhost:8080/api/products/1'
           ```
    - 상품 생성
        - POST `http://localhost:8080/api/products`
           ```shell
           curl --request POST \
             --url http://localhost:8080/api/products \
             --header 'Content-Type: application/json' \
             --data '{
               "name": "Air Force 1",
               "brand": {
                 "id": 1
               },
               "category": {
                 "id": 1
               },
               "price": 100000
             }'
           ```
    - 상품 수정
        - PUT `http://localhost:8080/api/products/{productId}`
           ```shell
           curl --request PUT \
             --url http://localhost:8080/api/products/1 \
             --header 'Content-Type: application/json' \
             --data '{
               "name": "Air Force 1",
               "brand": {
                 "id": 1
               },
               "category": {
                 "id": 1
               },
               "price": 90000
             }'
           ```
    - 상품 삭제
        - DELETE `http://localhost:8080/api/products/{productId}`
           ```shell
           curl --request DELETE \
             --url http://localhost:8080/api/products/1
           ```

4. Lowest/Highest Price API
    - 카테고리별 최저가 상품 조회
        - GET `http://localhost:8080/api/prices/lowest/categories`
            ```shell
            curl --location 'http://localhost:8080/api/prices/lowest/categories'
            ```
    - 카테고리 합산 최저가 브랜드 조회
        - GET `http://localhost:8080/api/prices/lowest/brands`
            ```
            curl --location 'http://localhost:8080/api/prices/lowest/brands'
            ```
    - 특정 카테고리의 최저가 및 최고가 조회
        - GET `http://localhost:8080/api/prices/lowest-highest?categroyName={categoryName}`
            ```
            curl --location 'http://localhost:8080/api/prices/lowest-highest?categoryName=Shoes'
            ```

## 4. 기타 항목

과제에서 제시된 샘플 데이터는 `http/*.http`파일에 데이터 삽입 요청이 작성되어있습니다.
- `http/brand.http`
- `http/category.http`
- `http/product.http`

순차적으로 실행하시면 샘플 데이터 입력이 완료됩니다.