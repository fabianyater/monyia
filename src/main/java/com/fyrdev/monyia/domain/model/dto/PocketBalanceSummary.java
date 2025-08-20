package com.fyrdev.monyia.domain.model.dto;

import java.math.BigDecimal;

public record PocketBalanceSummary(
        BigDecimal currentBalance,
        BigDecimal monthlyNetChange,
        BigDecimal previousMonthlyNetChange,
        BigDecimal balanceTrendPercentage) {
}
