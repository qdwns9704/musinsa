package musinsa.struct.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import musinsa.dto.brand.LowestPriceBrand;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LowestPriceBrandResponse {
    private LowestPriceBrand lowestPrice;
}
