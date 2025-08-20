package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.entity.BudgetEntity;
import com.fyrdev.monyia.adapters.driven.jpa.mapper.IBudgetEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.IBudgetRepository;
import com.fyrdev.monyia.domain.model.Budget;
import com.fyrdev.monyia.domain.spi.IBudgetPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BudgetAdapter implements IBudgetPersistencePort {
    private final IBudgetRepository budgetRepository;
    private final IBudgetEntityMapper budgetEntityMapper;

    @Override
    public Budget saveBudget(Budget budget) {
        var createdBudget = budgetRepository.save(budgetEntityMapper.toBudgetEntity(budget));
        return budgetEntityMapper.toBudget(createdBudget);
    }

    @Override
    public Budget getBudgetByCategoryId(Long categoryId, Long userId) {
        return budgetRepository
                .findByCategoryEntity_IdAndUserEntity_Id(categoryId, userId)
                .map(budgetEntityMapper::toBudget)
                .orElse(null);

    }

    @Override
    public List<Budget> getAllBudgetsByUserId(Long userId) {
        return budgetRepository.findByUserEntity_Id(userId)
                .stream()
                .map(budgetEntityMapper::toBudget)
                .toList();
    }

    @Override
    public Budget getBudgetById(Long budgetId, Long userId) {
        return budgetRepository.findByIdAndUserEntity_Id(budgetId, userId)
                .map(budgetEntityMapper::toBudget)
                .orElse(null);
    }
}
