package com.fyrdev.monyia.adapters.driving.http.mapper;

import com.fyrdev.monyia.adapters.driving.http.dto.request.GoalRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.GoalResponse;
import com.fyrdev.monyia.domain.model.Goal;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IGoalResponseMapper {
    GoalResponse toGoalResponse(Goal goal);

    Goal toGoal(GoalResponse goalResponse);

    List<GoalResponse> toGoalResponseList(List<Goal> goals);
}
