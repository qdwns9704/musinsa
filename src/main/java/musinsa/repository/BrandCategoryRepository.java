package musinsa.repository;

import musinsa.entity.BrandCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandCategoryRepository extends JpaRepository<BrandCategoryEntity, Long> {

    @Query("SELECT bcp FROM BrandCategoryEntity bcp " +
        "JOIN FETCH bcp.brandEntity b " +
        "JOIN FETCH bcp.categoryEntity c " +
        "WHERE b.id = :brandId AND c.id = :categoryId")
    Optional<BrandCategoryEntity> findByBrandIdAndCategoryId(Long brandId, Long categoryId);

    @Modifying
    @Query("DELETE FROM BrandCategoryEntity bcp WHERE bcp.brandEntity.id = :brandId")
    void deleteAllByBrandId(@Param("brandId") Long brandId);

    @Modifying
    @Query("DELETE FROM BrandCategoryEntity bcp WHERE bcp.categoryEntity.id = :categoryId")
    void deleteAllByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT bce.brandEntity.id, bce.brandEntity.name, COUNT(bce.categoryEntity.id) as categoryCount " +
        "FROM BrandCategoryEntity bce " +
        "GROUP BY bce.brandEntity.id, bce.brandEntity.name " +
        "HAVING COUNT(bce.categoryEntity.id) = ( " +
        "  SELECT COUNT(bce2.categoryEntity.id) " +
        "  FROM BrandCategoryEntity bce2 " +
        "  GROUP BY bce2.brandEntity.id " +
        "  ORDER BY COUNT(bce2.categoryEntity.id) DESC " +
        "  LIMIT 1 " +
        ")")
    List<Object[]> findBrandsWithMostCategories();

    @Query("SELECT bce FROM BrandCategoryEntity bce WHERE bce.brandEntity.id = :brandId")
    List<BrandCategoryEntity> findAllByBrandId(Long brandId);
}