package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.adapters.driving.http.dto.response.BudgetResponse;
import com.fyrdev.monyia.domain.model.Budget;

import java.math.BigDecimal;
import java.util.List;

public interface IBudgetServicePort {
    Budget saveBudget(Budget budget);
    Budget updateBudgetBalance(Long categoryId, BigDecimal amount);
    List<BudgetResponse> getAllBudgets();
    BudgetResponse getBudgetById(Long budgetId);
}
