package com.fyrdev.monyia.adapters.driving.http.dto.response;

import java.time.LocalDateTime;

public record GoalResponse(Long id, String name, Double amount, Double balance, LocalDateTime dueDate, String emoji) {
}
