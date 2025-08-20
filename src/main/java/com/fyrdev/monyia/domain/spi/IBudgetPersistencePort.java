package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.Budget;

import java.util.List;

public interface IBudgetPersistencePort {
    Budget saveBudget(Budget budget);

    Budget getBudgetByCategoryId(Long categoryId, Long userId);

    List<Budget> getAllBudgetsByUserId(Long userId);

    Budget getBudgetById(Long budgetId, Long userId);
}
