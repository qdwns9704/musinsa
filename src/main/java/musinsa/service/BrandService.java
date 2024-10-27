package musinsa.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import musinsa.entity.BrandEntity;
import musinsa.repository.BrandCategoryRepository;
import musinsa.repository.BrandRepository;
import musinsa.dto.brand.Brand;
import musinsa.mapper.BrandMapper;
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
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandCategoryRepository brandCategoryRepository;

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 단건 브랜드 조회
     * 1분간 유효한 캐시를 사용하여 브랜드를 조회합니다.
     * @param brandId 브랜드 ID
     * @return 브랜드 정보
     */
    @Cacheable(value = CachePrefix.BRAND_1M, key = "#brandId")
    @Transactional(readOnly = true)
    public Brand getBrand(@NonNull Long brandId) {
        return brandRepository.findById(brandId)
            .map(brandMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + brandId));
    }

    /**
     * 브랜드 목록 조회
     * @param page 페이지
     * @param count 페이지 당 조회 수
     * @param criteria 정렬 기준
     * @param sort 정렬 방식
     * @return 브랜드 목록
     */
    @Cacheable(value = CachePrefix.BRAND_3S, key = "{#page, #count, #criteria, #sort}")
    @Transactional(readOnly = true)
    public ListResponse<Brand> getBrands(int page, int count, @NonNull String criteria, @NonNull String sort) {
        PageRequest pageRequest = PageRequest.of(page - 1, count, Sort.Direction.fromString(sort), criteria);
        Page<BrandEntity> brandEntities = brandRepository.findAll(pageRequest);

        return ListResponse.of(
            Optional.of(brandEntities.getContent()).map(brandMapper::toDtos).orElseGet(ArrayList::new)
            , Optional.of(brandEntities.getTotalElements()).map(Number::intValue).orElse(0)
        );
    }

    /**
     * 브랜드 생성
     * @param brand 브랜드 정보
     * @return 생성된 브랜드 정보
     */
    public Brand createBrand(@NonNull Brand brand) {
        BrandEntity brandEntity = brandMapper.toEntity(brand);
        brandEntity = brandRepository.save(brandEntity);
        return brandMapper.toDto(brandEntity);
    }

    /**
     * 브랜드 수정
     * @param brand 브랜드 정보
     * @return 수정된 브랜드 정보
     */
    @CacheEvict(value = CachePrefix.BRAND_1M, key = "#brand.id")
    public Brand updateBrand(@NonNull Brand brand) {
        BrandEntity brandEntity = brandMapper.toEntity(brand);
        brandEntity = brandRepository.save(brandEntity);
        return brandMapper.toDto(brandEntity);
    }

    /**
     * 브랜드 삭제
     * @param brandId 브랜드 ID
     */
    @CacheEvict(value = CachePrefix.BRAND_1M, key = "#brandId")
    public void deleteBrand(@NonNull Long brandId) {
        brandRepository.deleteById(brandId);
        brandCategoryRepository.deleteAllByBrandId(brandId);
    }
}