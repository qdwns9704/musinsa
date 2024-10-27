package musinsa.mapper;

import musinsa.entity.ProductEntity;
import musinsa.dto.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = {BrandMapper.class, CategoryMapper.class})
public interface ProductMapper {
    @Mapping(target = "brand", source = "brandEntity")
    @Mapping(target = "category", source = "categoryEntity")
    Product toDto(ProductEntity entity);

    List<Product> toDtos(Collection<ProductEntity> entities);

    @Mapping(target = "brandEntity", source = "brand")
    @Mapping(target = "categoryEntity", source = "category")
    ProductEntity toEntity(Product dto);
}
