package com.fyrdev.monyia.adapters.driving.http.dto.response;

import com.fyrdev.monyia.domain.model.enums.LoanStatus;

public record LoanResponse(Long id, String loanParty, String description, Long amount, String loanType, Double balance, LoanStatus status) {
}
