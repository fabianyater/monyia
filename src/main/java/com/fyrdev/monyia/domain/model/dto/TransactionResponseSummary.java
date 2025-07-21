package com.fyrdev.monyia.domain.model.dto;

import java.time.LocalDate;

public record TransactionResponseSummary(Long id, String description, Double amount, LocalDate date, String categoryName,
                                         String emoji, String type) {
}
