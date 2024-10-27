package musinsa.service;

import jakarta.persistence.EntityNotFoundException;
import musinsa.dto.brand.BrandPrice;
import musinsa.dto.category.CategoryPrice;
import musinsa.dto.brand.LowestPriceBrand;
import musinsa.dto.product.Product;
import musinsa.entity.BrandCategoryEntity;
import musinsa.entity.CategoryEntity;
import musinsa.entity.ProductEntity;
import musinsa.mapper.ProductMapper;
import musinsa.repository.BrandCategoryRepository;
import musinsa.repository.CategoryRepository;
import musinsa.repository.ProductRepository;
import musinsa.struct.common.CachePrefix;
import musinsa.struct.response.LowestHighestPriceCategoryResponse;
import musinsa.struct.response.LowestPriceBrandResponse;
import musinsa.struct.response.LowestPriceCategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toSet;

@Service
@Transactional
public class PriceService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandCategoryRepository brandCategoryRepository;

    @Autowired
    ProductMapper productMapper;

    /**
     * 카테고리별 최저가 상품 조회
     * 우선 인덱싱으로 구현하였으나, 데이터 양이 많아지면 중간 테이블을 두어 상품 생성/수정/삭제 시 최저가 상품을 저장하는 중간테이블을 수정, 조회하는 방식으로 변경해야함
     * @param categoryIds 카테고리 ID 목록
     * @return 카테고리별 최저가 상품 목록
     */
    @Cacheable(value = CachePrefix.LOWEST_PRICE_CATEGORIES_3S)
    @Transactional(readOnly = true)
    public LowestPriceCategoryResponse getLowestPriceByCategories(Collection<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            List<CategoryEntity> categories = categoryRepository.findAll();
            if (categories.isEmpty()) {
                return new LowestPriceCategoryResponse();
            }

            categoryIds = categories.stream()
                .map(CategoryEntity::getId)
                .collect(toSet());
        }

        if (categoryIds.isEmpty()) {
            return new LowestPriceCategoryResponse();
        }

        List<ProductEntity> lowestPriceProductEntities = productRepository.findLowestPriceByCategoryIds(categoryIds);

        List<Product> lowestPriceProducts = productMapper.toDtos(lowestPriceProductEntities);

        Double totalPrice = lowestPriceProductEntities.stream()
            .map(ProductEntity::getPrice)
            .mapToDouble(Double::doubleValue)
            .sum();

        return new LowestPriceCategoryResponse(lowestPriceProducts, totalPrice);
    }

    /**
     * 카테고리 합산 최저가 브랜드 조회
     * 정책적으로 모든 브랜드에 모든 카테고리 상품이 존재하지 않을 거란 가정으로 아래의 조건 정책을 잡음
     * 1. 카테고리 항목이 많은 브랜드를 먼저 선출
     * 2. 카테고리 합산 최저가 브랜드를 선정
     * @return 카테고리 합산 최저가 브랜드
     */
    @Cacheable(value = CachePrefix.LOWEST_PRICE_BRANDS_3S)
    @Transactional(readOnly = true)
    public LowestPriceBrandResponse getLowestPriceBrand() {
        // 1. 카테고리 항목이 많은 브랜드를 먼저 선출
        List<Object[]> brandsWithMostCategories = brandCategoryRepository.findBrandsWithMostCategories();

        // 2. 각 브랜드의 카테고리 항목 중 최저가 가격 합산을 계산하여 최저가 브랜드 선정
        LowestPriceBrand bestBrand = brandsWithMostCategories.stream()
            .map(brandWithCount -> {
                Long brandId = (Long) brandWithCount[0];
                String brandName = (String) brandWithCount[1];
                List<BrandCategoryEntity> categoryEntities = brandCategoryRepository.findAllByBrandId(brandId);

                double totalPrice = categoryEntities.stream()
                    .mapToDouble(bce -> bce.getLowestPriceProductEntity().getPrice())
                    .sum();

                // DTO 변환
                List<CategoryPrice> categoryPrices = categoryEntities.stream()
                    .map(bce -> new CategoryPrice(
                        bce.getCategoryEntity().getName(),
                        bce.getLowestPriceProductEntity().getPrice()
                    ))
                    .toList();

                return new LowestPriceBrand()
                    .setBrand(brandName)
                    .setCategory(categoryPrices)
                    .setTotalPrice(totalPrice);
            })
            .min(Comparator.comparingDouble(LowestPriceBrand::getTotalPrice))
            .orElseThrow(() -> new EntityNotFoundException("No brands found"));

        // 최종 결과 반환
        return new LowestPriceBrandResponse(bestBrand);
    }

    /**
     * 단건 카테고리 최저가, 최고가 조회
     * @param categoryName 카테고리명
     * @return 해당 카테고리 최저가, 최고가
     */
    @Cacheable(value = CachePrefix.LOWEST_HIGHEST_CATEGORY_3S, key = "#categoryName")
    @Transactional(readOnly = true)
    public LowestHighestPriceCategoryResponse getLowestHighestPriceCategory(String categoryName) {
        BrandPrice lowewstBrandPrice = productRepository.findLowestPriceByCategoryName(categoryName)
            .map(e -> new BrandPrice(e.getBrandEntity().getName(), e.getPrice()))
            .orElse(null);
        BrandPrice highestBrandPrice = productRepository.findHighestPriceByCategoryName(categoryName)
            .map(e -> new BrandPrice(e.getBrandEntity().getName(), e.getPrice()))
            .orElse(null);

        return new LowestHighestPriceCategoryResponse()
            .setCategory(categoryName)
            .setLowestPrice(lowewstBrandPrice)
            .setHighestPrice(highestBrandPrice);
    }
}