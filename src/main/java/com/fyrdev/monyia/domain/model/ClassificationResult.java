package com.fyrdev.monyia.domain.model;

import java.math.BigDecimal;

public record ClassificationResult(
        String date,
        String periodicity,
        Category category,
        Long value,
        String type,
        String description) {
}
