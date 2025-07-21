package com.fyrdev.monyia.adapters.driven.jpa.mapper;

import com.fyrdev.monyia.adapters.driven.jpa.entity.PocketEntity;
import com.fyrdev.monyia.domain.model.Pocket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPocketEntityMapper {
    @Mapping(target = "userEntity.id", source = "userId")
    PocketEntity toEntity(Pocket pocket);

    Pocket toPocket(PocketEntity pocketEntity);
}
