package com.fyrdev.monyia.adapters.driving.http.mapper;

import com.fyrdev.monyia.adapters.driving.http.dto.request.BudgetRequest;
import com.fyrdev.monyia.domain.model.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBudgetRequestMapper {
    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "periodicity", source = "frequency")
    Budget toBudget(BudgetRequest budgetRequest);
}
