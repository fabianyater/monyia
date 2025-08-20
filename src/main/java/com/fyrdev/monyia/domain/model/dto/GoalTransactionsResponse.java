package com.fyrdev.monyia.domain.model.dto;

public record GoalTransactionsResponse(
        Long id,
        String emoji,
        String categoryName,
        Double amount,
        String date,
        String type) {
}
