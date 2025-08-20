package com.fyrdev.monyia.adapters.driven.jpa.mapper;

import com.fyrdev.monyia.adapters.driven.jpa.entity.BudgetEntity;
import com.fyrdev.monyia.domain.model.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBudgetEntityMapper {
    @Mapping(target = "userEntity.id", source = "userId")
    @Mapping(target = "categoryEntity.id", source = "categoryId")
    BudgetEntity toBudgetEntity(Budget budget);

    @Mapping(target = "userId", source = "userEntity.id")
    @Mapping(target = "categoryId", source = "categoryEntity.id")
    Budget toBudget(BudgetEntity budgetEntity);
}
