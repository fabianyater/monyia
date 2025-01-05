package com.fyrdev.monyia.adapters.driving.http.mapper;

import com.fyrdev.monyia.adapters.driving.http.dto.request.CategoryRequest;
import com.fyrdev.monyia.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICategoryRequestMapper {
    Category toCategory(CategoryRequest categoryRequest);
}
