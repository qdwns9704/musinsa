package musinsa.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import musinsa.entity.CategoryEntity;
import musinsa.repository.BrandCategoryRepository;
import musinsa.repository.CategoryRepository;
import musinsa.dto.category.Category;
import musinsa.mapper.CategoryMapper;
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
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandCategoryRepository brandCategoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 단건 카테고리 조회
     * 1분간 유효한 캐시를 사용하여 카테고리를 조회합니다.
     * @param categoryId 카테고리 ID
     * @return 카테고리 정보
     */
    @Transactional(readOnly = true)
    @Cacheable(value = CachePrefix.CATEGORY_1M, key = "#categoryId")
    public Category getCategory(@NonNull Long categoryId) {
        return categoryRepository.findById(categoryId)
            .map(categoryMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));
    }

    /**
     * 카테고리 목록 조회
     * @param page 페이지
     * @param count 페이지 당 조회 수
     * @param criteria 정렬 기준
     * @param sort 정렬 방식
     * @return 카테고리 목록
     */
    @Transactional(readOnly = true)
    @Cacheable(value = CachePrefix.CATEGORY_3S, key = "{#page, #count, #criteria, #sort}")
    public ListResponse<Category> getCategories(int page, int count, @NonNull String criteria, @NonNull String sort) {
        PageRequest pageRequest = PageRequest.of(page - 1, count, Sort.Direction.fromString(sort), criteria);
        Page<CategoryEntity> categoryEntities = categoryRepository.findAll(pageRequest);

        return ListResponse.of(
            Optional.of(categoryEntities.getContent()).map(categoryMapper::toDtos).orElseGet(ArrayList::new)
            , Optional.of(categoryEntities.getTotalElements()).map(Number::intValue).orElse(0)
        );
    }

    /**
     * 카테고리 생성
     * @param category 카테고리 정보
     * @return 생성된 카테고리 정보
     */
    public Category createCategory(@NonNull Category category) {
        CategoryEntity categoryEntity = categoryMapper.toEntity(category);
        categoryEntity = categoryRepository.save(categoryEntity);
        return categoryMapper.toDto(categoryEntity);
    }

    /**
     * 카테고리 수정
     * @param category 카테고리 정보
     * @return 수정된 카테고리 정보
     */
    @CacheEvict(value = CachePrefix.CATEGORY_1M, key = "#category.id")
    public Category updateCategory(@NonNull Category category) {
        CategoryEntity categoryEntity = categoryMapper.toEntity(category);
        categoryEntity = categoryRepository.save(categoryEntity);
        return categoryMapper.toDto(categoryEntity);
    }

    /**
     * 카테고리 삭제
     * @param categoryId 카테고리 ID
     */
    @CacheEvict(value = CachePrefix.CATEGORY_1M, key = "#categoryId")
    public void deleteCategory(@NonNull Long categoryId) {
        categoryRepository.deleteById(categoryId);
        brandCategoryRepository.deleteAllByCategoryId(categoryId);
    }
}