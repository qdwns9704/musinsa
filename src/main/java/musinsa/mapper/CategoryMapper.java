package musinsa.mapper;

import musinsa.entity.CategoryEntity;
import musinsa.dto.category.Category;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toDto(CategoryEntity entity);

    List<Category> toDtos(Collection<CategoryEntity> entities);

    CategoryEntity toEntity(Category dto);
}
