package musinsa.service;

import musinsa.entity.BrandCategoryEntity;
import musinsa.entity.ProductEntity;
import musinsa.repository.BrandCategoryRepository;
import musinsa.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BrandCategoryService {
    @Autowired
    private BrandCategoryRepository brandCategoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Transactional
    public void updateBrandCategoryPrice(ProductEntity productEntity) {
        Long brandId = productEntity.getBrandEntity().getId();
        Long categoryId = productEntity.getCategoryEntity().getId();

        // 기존 레코드가 있는지 조회
        BrandCategoryEntity brandCategoryEntity = brandCategoryRepository.findByBrandIdAndCategoryId(brandId, categoryId).orElse(null);

        boolean saveOrUpdateRequired = false;
        if (brandCategoryEntity == null) {
            brandCategoryEntity = new BrandCategoryEntity()
                .setBrandEntity(productEntity.getBrandEntity())
                .setCategoryEntity(productEntity.getCategoryEntity())
                .setLowestPriceProductEntity(productEntity)
                .setHighestPriceProductEntity(productEntity);
            saveOrUpdateRequired = true;
        } else {
            if (brandCategoryEntity.getLowestPriceProductEntity() == null
                || productEntity.getPrice() <= brandCategoryEntity.getLowestPriceProductEntity().getPrice()) {
                brandCategoryEntity.setLowestPriceProductEntity(productEntity);
                saveOrUpdateRequired = true;
            }

            if (brandCategoryEntity.getHighestPriceProductEntity() == null
                || productEntity.getPrice() >= brandCategoryEntity.getHighestPriceProductEntity().getPrice()) {
                brandCategoryEntity.setHighestPriceProductEntity(productEntity);
                saveOrUpdateRequired = true;
            }
        }

        if (saveOrUpdateRequired) {
            brandCategoryRepository.save(brandCategoryEntity);
        }
    }
}