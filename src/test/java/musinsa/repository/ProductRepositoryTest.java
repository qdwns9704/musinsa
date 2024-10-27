package musinsa.repository;

import musinsa.entity.BrandEntity;
import musinsa.entity.CategoryEntity;
import musinsa.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    private CategoryEntity categoryEntity;

    private BrandEntity brandEntity;

    private ProductEntity productEntity;

    @BeforeEach
    void setup() {
        // Setup category and brand
        categoryEntity = new CategoryEntity();
        categoryEntity.setName("TOP");
        categoryEntity = categoryRepository.save(categoryEntity);

        brandEntity = new BrandEntity();
        brandEntity.setName("Test Brand");
        brandEntity = brandRepository.save(brandEntity);

        productEntity = new ProductEntity();
        productEntity.setName("Product A")
            .setPrice(1000.0)
            .setCategoryEntity(categoryEntity)
            .setBrandEntity(brandEntity);
        productRepository.save(productEntity);

        ProductEntity productEntity2 = new ProductEntity();
        productEntity2.setName("Product B")
            .setPrice(5000.0)
            .setCategoryEntity(categoryEntity)
            .setBrandEntity(brandEntity);
        productRepository.save(productEntity2);
    }

    @Test
    void getProduct() {
        // Given
        Long productId = productEntity.getId();

        // When
        ProductEntity savedProduct = productRepository.findById(productId).orElse(null);

        // Then
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo(productId);
        assertThat(savedProduct.getName()).isEqualTo("Product A");
        assertThat(savedProduct.getPrice()).isEqualTo(1000.0);
        assertThat(savedProduct.getCategoryEntity()).isEqualTo(categoryEntity);
        assertThat(savedProduct.getBrandEntity()).isEqualTo(brandEntity);
    }

    @Test
    void saveProduct() {
        // Given
        ProductEntity product = new ProductEntity();
        product.setName("Product C")
            .setPrice(3000.0)
            .setCategoryEntity(categoryEntity)
            .setBrandEntity(brandEntity);

        // When
        ProductEntity savedProduct = productRepository.save(product);

        // Then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Product C");
        assertThat(savedProduct.getPrice()).isEqualTo(3000.0);
        assertThat(savedProduct.getCategoryEntity()).isEqualTo(categoryEntity);
        assertThat(savedProduct.getBrandEntity()).isEqualTo(brandEntity);
    }

    @Test
    void updateProduct() {
        // Given
        ProductEntity product = new ProductEntity();
        product.setName("Product C")
            .setPrice(3000.0)
            .setCategoryEntity(categoryEntity)
            .setBrandEntity(brandEntity);
        ProductEntity savedProduct = productRepository.save(product);

        // When
        savedProduct.setName("Product D");
        savedProduct.setPrice(4000.0);
        ProductEntity updatedProduct = productRepository.save(savedProduct);

        // Then
        assertThat(updatedProduct.getId()).isEqualTo(savedProduct.getId());
        assertThat(updatedProduct.getName()).isEqualTo("Product D");
        assertThat(updatedProduct.getPrice()).isEqualTo(4000.0);
        assertThat(updatedProduct.getCategoryEntity()).isEqualTo(categoryEntity);
        assertThat(updatedProduct.getBrandEntity()).isEqualTo(brandEntity);
    }

    @Test
    void deleteProduct() {
        // Given
        ProductEntity product = new ProductEntity();
        product.setName("Product C")
            .setPrice(3000.0)
            .setCategoryEntity(categoryEntity)
            .setBrandEntity(brandEntity);
        ProductEntity savedProduct = productRepository.save(product);

        // When
        productRepository.deleteById(savedProduct.getId());

        // Then
        assertThat(productRepository.findById(savedProduct.getId())).isEmpty();
    }
}