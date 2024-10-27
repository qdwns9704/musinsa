package musinsa.repository;

import musinsa.entity.ProductEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @EntityGraph(attributePaths = {"brandEntity", "categoryEntity"})
    Optional<ProductEntity> findById(Long productId);

    @Query("SELECT p "
        + "FROM ProductEntity p "
        + "JOIN FETCH p.brandEntity b "
        + "JOIN FETCH p.categoryEntity c "
        + "WHERE p.id = :id"
    )
    Optional<ProductEntity> findByIdWithDetails(@Param("id") Long productId);

    @Query("SELECT p FROM ProductEntity p "
        + "JOIN p.brandEntity b "
        + "JOIN p.categoryEntity c "
        + "WHERE c.id IN :categoryIds "
        + "AND p.price = (SELECT MIN(p2.price) "
        + "               FROM ProductEntity p2 "
        + "               WHERE p2.categoryEntity.id = c.id) "
        + "AND p.createdAt = (SELECT MAX(p3.createdAt) "
        + "                   FROM ProductEntity p3 "
        + "                   WHERE p3.categoryEntity.id = c.id "
        + "                     AND p3.price = p.price) "
        + "ORDER BY c.id ASC")
    List<ProductEntity> findLowestPriceByCategoryIds(@Param("categoryIds") Collection<Long> categoryIds);

    @Query("SELECT p FROM ProductEntity p " +
        "JOIN p.brandEntity b " +
        "JOIN p.categoryEntity c " +
        "WHERE c.name = :categoryName " +
        "ORDER BY p.price ASC, p.createdAt DESC LIMIT 1")
    Optional<ProductEntity> findLowestPriceByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT p FROM ProductEntity p " +
        "JOIN p.brandEntity b " +
        "JOIN p.categoryEntity c " +
        "WHERE c.name = :categoryName " +
        "ORDER BY p.price DESC, p.createdAt DESC LIMIT 1")
    Optional<ProductEntity> findHighestPriceByCategoryName(@Param("categoryName") String categoryName);
}