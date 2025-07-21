package com.fyrdev.monyia.adapters.driving.http.dto.request;

import com.fyrdev.monyia.domain.model.enums.GoalTransactionType;

import java.math.BigDecimal;

public record GoalTransactionRequest(Long goalId, Long pocketId, BigDecimal amount, GoalTransactionType type) {
}
