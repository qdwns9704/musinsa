package musinsa.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import musinsa.entity.BrandEntity;
import musinsa.entity.CategoryEntity;
import musinsa.entity.ProductEntity;
import musinsa.repository.BrandRepository;
import musinsa.repository.CategoryRepository;
import musinsa.repository.ProductRepository;
import musinsa.dto.product.Product;
import musinsa.mapper.ProductMapper;
import musinsa.struct.response.ListResponse;
import musinsa.struct.common.CachePrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BrandCategoryService brandCategoryService;

    @Autowired
    ProductMapper productMapper;

    /**
     * 단건 상품 조회
     * 1분간 유효한 캐시를 사용하여 상품을 조회합니다.
     * @param productId 상품 ID
     * @return 상품 정보
     *
     */
    @Cacheable(value = CachePrefix.PRODUCT_1M, key = "#productId")
    @Transactional(readOnly = true)
    public Product getProduct(@NonNull Long productId) {
        return productRepository.findByIdWithDetails(productId)
            .map(productMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }

    /**
     * 상품 목록 조회
     * 3초간 유효한 캐시를 사용하여 상품 목록을 조회합니다.
     * @param page 페이지
     * @param count 페이지 당 조회 수
     * @param criteria 정렬 기준
     * @param sort 정렬 방식
     * @return 상품 목록
     */
    @Cacheable(value = CachePrefix.PRODUCT_3S, key = "{#page, #count, #criteria, #sort}")
    @Transactional(readOnly = true)
    public ListResponse<Product> getProducts(int page, int count, String criteria, String sort) {
        PageRequest pageRequest = PageRequest.of(page - 1, count, Sort.Direction.fromString(sort), criteria);
        Page<ProductEntity> productEntities = productRepository.findAll(pageRequest);

        return ListResponse.of(
            Optional.of(productEntities.getContent()).map(productMapper::toDtos).orElseGet(ArrayList::new)
            , Optional.of(productEntities.getTotalElements()).map(Number::intValue).orElse(0)
        );
    }

    /**
     * 상품 생성
     * @param product 상품 정보
     * @return 생성된 상품 정보
     */
    @Transactional
    public Product createProduct(@NonNull Product product) {
        BrandEntity brandEntity = brandRepository.findById(product.getBrand().getId())
            .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + product.getBrand().getId()));

        CategoryEntity categoryEntity = categoryRepository.findById(product.getCategory().getId())
            .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + product.getCategory().getId()));

        ProductEntity productEntity = productMapper.toEntity(product)
            .setBrandEntity(brandEntity)
            .setCategoryEntity(categoryEntity);

        productEntity = productRepository.save(productEntity);

        brandCategoryService.updateBrandCategoryPrice(productEntity);

        return productMapper.toDto(productEntity);
    }

    /**
     * 상품 수정
     * @param product 상품 정보
     * @return 수정된 상품 정보
     */
    @CacheEvict(value = CachePrefix.PRODUCT_1M, key = "#product.id")
    @Transactional
    public Product updateProduct(@NonNull Product product) {
        ProductEntity productEntity = productRepository.findById(product.getId())
            .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + product.getId()));

        ProductEntity productEntityToUpdate = productMapper.toEntity(product);

        BrandEntity brandEntity = brandRepository.findById(product.getBrand().getId())
            .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + product.getBrand().getId()));
        productEntityToUpdate.setBrandEntity(brandEntity);

        CategoryEntity categoryEntity = categoryRepository.findById(product.getCategory().getId())
            .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + product.getCategory().getId()));
        productEntityToUpdate.setCategoryEntity(categoryEntity);

        productRepository.save(productEntityToUpdate);

        brandCategoryService.updateBrandCategoryPrice(productEntity);

        return productMapper.toDto(productEntity);
    }

    /**
     * 상품 삭제
     * @param productId 상품 ID
     */
    @CacheEvict(value = CachePrefix.PRODUCT_1M, key = "#productId")
    public void deleteProduct(@NonNull Long productId) {
        ProductEntity productEntity = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        productRepository.deleteById(productId);
        brandCategoryService.updateBrandCategoryPrice(productEntity);
    }
}