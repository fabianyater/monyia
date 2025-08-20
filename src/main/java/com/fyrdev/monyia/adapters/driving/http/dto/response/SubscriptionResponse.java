package com.fyrdev.monyia.adapters.driving.http.dto.response;

import com.fyrdev.monyia.domain.model.enums.Periodicity;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SubscriptionResponse(
        Long id,
        String name,
        String urlImage,
        Double amount,
        LocalDate dueDate,
        Periodicity periodicity,
        String daysLeft) {
}
