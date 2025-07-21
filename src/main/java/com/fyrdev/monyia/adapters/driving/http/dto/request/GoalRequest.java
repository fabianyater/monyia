package com.fyrdev.monyia.adapters.driving.http.dto.request;

import java.time.LocalDateTime;

public record GoalRequest(String name, Double amount, LocalDateTime dueDate) {
}
