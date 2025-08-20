package com.fyrdev.monyia.adapters.driving.http.dto.request;

import com.fyrdev.monyia.domain.model.enums.Periodicity;

import java.time.LocalDate;

public record SubscriptionRequest(
        String name,
        String urlImage,
        Double amount,
        LocalDate dueDate,
        Periodicity periodicity) {
}
