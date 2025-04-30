package com.fyrdev.monyia.adapters.driving.http.dto.request;

public record LoanPaymentRequest(Long loanId, Long pocketId, Double amount) {
}
