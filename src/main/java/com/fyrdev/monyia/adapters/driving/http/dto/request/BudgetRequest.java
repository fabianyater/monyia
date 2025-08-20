package com.fyrdev.monyia.adapters.driving.http.dto.request;

import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.enums.Periodicity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BudgetRequest(
        String name,
        String description,
        BigDecimal amount,
        CategoryRequest category,
        Periodicity frequency,
        Integer dayOfTheMonth) {
}
