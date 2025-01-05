package com.fyrdev.monyia.adapters.driving.http.mapper;

import com.fyrdev.monyia.adapters.driving.http.dto.request.UserRequest;
import com.fyrdev.monyia.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserRequestMapper {
    User toUser(UserRequest userRequest);
}
