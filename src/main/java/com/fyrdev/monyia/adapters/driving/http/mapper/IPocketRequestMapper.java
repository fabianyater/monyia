package com.fyrdev.monyia.adapters.driving.http.mapper;

import com.fyrdev.monyia.adapters.driving.http.dto.request.PocketRequest;
import com.fyrdev.monyia.domain.model.Pocket;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPocketRequestMapper {
    Pocket toPocket(PocketRequest pocketRequest);
}
