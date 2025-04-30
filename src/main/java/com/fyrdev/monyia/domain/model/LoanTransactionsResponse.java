package com.fyrdev.monyia.domain.model;

public record LoanTransactionsResponse(
        Long id,
        String emoji,
        String categoryName,
        String pocketName,
        Double amount,
        String date) {
}
