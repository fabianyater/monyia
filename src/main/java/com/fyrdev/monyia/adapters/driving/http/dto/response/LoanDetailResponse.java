package com.fyrdev.monyia.adapters.driving.http.dto.response;

import java.time.LocalDateTime;

public record LoanDetailResponse(
        Long id,
        String loanParty,
        String description,
        Double amount,
        Double balance,
        LocalDateTime startDate,
        String loanType,
        String pocketName) {
}
