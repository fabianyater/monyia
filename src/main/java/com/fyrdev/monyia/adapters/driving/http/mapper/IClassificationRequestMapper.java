package com.fyrdev.monyia.adapters.driving.http.mapper;

import com.fyrdev.monyia.adapters.driving.http.dto.response.ClassificationResponse;
import com.fyrdev.monyia.domain.model.ClassificationResult;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IClassificationRequestMapper {
    ClassificationResponse toClassificationResponse(ClassificationResult classificationResult);
}
