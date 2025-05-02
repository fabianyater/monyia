package com.fyrdev.monyia.domain.model.dto;

import com.fyrdev.monyia.domain.model.Category;

public record ClassificationResult(
        String date,
        String periodicity,
        Category category,
        Long value,
        String type,
        String description) {
}
