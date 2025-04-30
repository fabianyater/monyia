package com.fyrdev.monyia.adapters.driving.http.dto.response;

public record LoanResponse(Long id, String loanParty, String description, Long amount, String loanType) {
}
