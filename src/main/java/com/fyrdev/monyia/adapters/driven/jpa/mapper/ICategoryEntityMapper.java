package com.fyrdev.monyia.adapters.driven.jpa.mapper;

import com.fyrdev.monyia.adapters.driven.jpa.entity.CategoryEntity;
import com.fyrdev.monyia.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICategoryEntityMapper {
    @Mapping(target = "userEntity.id", source = "userId")
    CategoryEntity toEntity(Category category);
    List<Category> toCategoriesList(List<CategoryEntity> categoryEntities);
}
