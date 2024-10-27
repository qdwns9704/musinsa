package musinsa.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.experimental.Accessors;
import musinsa.dto.brand.Brand;
import musinsa.dto.category.Category;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Product {
    private Long id;

    @NotNull(message = "Brand must not be null")
    private Brand brand;

    @NotNull(message = "Category must not be null")
    private Category category;

    @NotBlank(message = "Product name must not be blank")
    private String name;

    @PositiveOrZero(message = "Price must be greater than or equal to zero")
    @NotNull(message = "Price must not be null")
    private Double price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}