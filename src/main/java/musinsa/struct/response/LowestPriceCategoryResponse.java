package musinsa.struct.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import musinsa.dto.product.Product;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LowestPriceCategoryResponse {
    private List<Product> products = List.of();

    private Double totalPrice = 0D;
}
