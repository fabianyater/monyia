package com.fyrdev.monyia.adapters.driving.http.mapper;

import com.fyrdev.monyia.adapters.driving.http.dto.request.BudgetRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.BudgetResponse;
import com.fyrdev.monyia.adapters.driving.http.dto.response.CategoryResponse;
import com.fyrdev.monyia.domain.model.Budget;
import com.fyrdev.monyia.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBudgetResponseMapper {
    BudgetResponse toBudgetResponse(Budget budget);
    List<BudgetResponse> toBudgetResponseList(List<Budget> budgets);
}
