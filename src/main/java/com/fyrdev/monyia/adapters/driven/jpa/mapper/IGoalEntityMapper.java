package com.fyrdev.monyia.adapters.driven.jpa.mapper;

import com.fyrdev.monyia.adapters.driven.jpa.entity.GoalEntity;
import com.fyrdev.monyia.domain.model.Goal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IGoalEntityMapper {
    Goal toGoal(GoalEntity goalEntity);
    @Mapping(target = "userEntity.id", source = "userId")
    GoalEntity toGoalEntity(Goal goal);
}
