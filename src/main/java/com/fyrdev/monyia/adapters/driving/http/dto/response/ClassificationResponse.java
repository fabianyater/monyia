package com.fyrdev.monyia.adapters.driving.http.dto.response;

public record ClassificationResponse(
        String date,
        String periodicity,
        CategoryResponse category,
        Long value,
        String type,
        String description) {
}
