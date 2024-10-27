package musinsa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "brand_category_prices", uniqueConstraints = {
    @UniqueConstraint(name = "uk_brand_category", columnNames = {"brand_id", "category_id"})
})
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BrandCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", referencedColumnName = "id", nullable = false)
    private BrandEntity brandEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private CategoryEntity categoryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lowest_price_product_id", referencedColumnName = "id")
    private ProductEntity lowestPriceProductEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "highest_price_product_id", referencedColumnName = "id")
    private ProductEntity highestPriceProductEntity;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}