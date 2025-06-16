package com.fyrdev.monyia.adapters.driving.http.dto.request;

import java.math.BigDecimal;

public record TransferRequest(Long fromPocketId, Long toPocketId, BigDecimal amount) {
}
