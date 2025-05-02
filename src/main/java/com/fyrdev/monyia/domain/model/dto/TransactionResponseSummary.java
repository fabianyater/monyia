package com.fyrdev.monyia.domain.model.dto;

public record TransactionResponseSummary(Long id, String description, Double amount, String date, String categoryName,
                                         String emoji) {
}
