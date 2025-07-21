package com.fyrdev.monyia.domain.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionResponseSummary(Long id, String description, Double amount, LocalDateTime date, String categoryName,
                                         String emoji, String type) {
}
