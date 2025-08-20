package com.fyrdev.monyia.adapters.driving.http.dto.response;

import java.math.BigDecimal;

public record BudgetResponse(
        Long id,
        String name,
        BigDecimal amount,
        BigDecimal spent,
        BigDecimal left,
        Double percentageCompleted,
        String period,
        String emoji,
        String category) {
}
