package musinsa.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import musinsa.dto.category.CategoryPrice;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LowestPriceBrand {
    private Long id;

    private String brand;

    private List<CategoryPrice> category;

    private Double totalPrice;
}