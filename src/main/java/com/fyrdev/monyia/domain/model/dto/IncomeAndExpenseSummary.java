package com.fyrdev.monyia.domain.model.dto;

import java.math.BigDecimal;

public record IncomeAndExpenseSummary(BigDecimal amount, String type) {
}
