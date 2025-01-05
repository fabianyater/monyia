package com.fyrdev.monyia.adapters.driven.jpa.mapper;

import com.fyrdev.monyia.adapters.driven.jpa.entity.CategoryEntity;
import com.fyrdev.monyia.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICategoryEntityMapper {
    CategoryEntity toEntity(Category category);
}
