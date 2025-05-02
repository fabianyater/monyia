package com.fyrdev.monyia.domain.model.dto;

public record LoanTransactionsResponse(
        Long id,
        String emoji,
        String categoryName,
        String pocketName,
        Double amount,
        String date) {
}
