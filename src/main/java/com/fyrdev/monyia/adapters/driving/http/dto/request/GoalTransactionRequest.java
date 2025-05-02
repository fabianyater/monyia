package com.fyrdev.monyia.adapters.driving.http.dto.request;

import com.fyrdev.monyia.domain.model.enums.GoalTransactionType;

public record GoalTransactionRequest(Long goalId, Double amount, GoalTransactionType type) {
}
