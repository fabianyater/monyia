package com.fyrdev.monyia.domain.model.dto;

import com.fyrdev.monyia.domain.model.enums.TransactionType;
import lombok.Builder;

@Builder
public record QueryFilters(String category, TransactionType type, String pocket, String text) {
}
