package musinsa.mapper;

import musinsa.entity.BrandEntity;
import musinsa.dto.brand.Brand;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand toDto(BrandEntity entity);

    List<Brand> toDtos(Collection<BrandEntity> entities);

    BrandEntity toEntity(Brand dto);
}
