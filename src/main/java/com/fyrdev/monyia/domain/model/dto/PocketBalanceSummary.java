package com.fyrdev.monyia.domain.model.dto;

public record PocketBalanceSummary(
        Double currentBalance,
        Double monthlyNetChange,
        Double previousMonthlyNetChange,
        Double balanceTrendPercentage) {
}
