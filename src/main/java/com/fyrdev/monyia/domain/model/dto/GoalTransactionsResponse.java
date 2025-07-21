package com.fyrdev.monyia.domain.model.dto;

public record GoalTransactionsResponse(
        Long id,
        String name,
        String emoji,
        String categoryName,
        Double amount,
        String date,
        String type) {
}
