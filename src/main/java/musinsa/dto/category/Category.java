package musinsa.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Category {
    private Long id;

    @NotBlank(message = "Category name must not be blank")
    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}