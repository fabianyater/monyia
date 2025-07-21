package com.fyrdev.monyia.adapters.driving.http.dto.request;

import java.math.BigDecimal;

public record LoanPaymentRequest(Long loanId, Long pocketId, BigDecimal amount) {
}
