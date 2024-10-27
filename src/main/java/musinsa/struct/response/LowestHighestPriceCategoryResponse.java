package musinsa.struct.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import musinsa.dto.brand.BrandPrice;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LowestHighestPriceCategoryResponse {
    private String category;

    private BrandPrice lowestPrice;

    private BrandPrice highestPrice;
}
