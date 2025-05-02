package com.fyrdev.monyia.adapters.driving.http.mapper;

import com.fyrdev.monyia.adapters.driving.http.dto.request.GoalRequest;
import com.fyrdev.monyia.domain.model.Goal;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IGoalRequestMapper {
    GoalRequest toGoalRequest(Goal goal);
    Goal toGoal(GoalRequest goalRequest);
}
