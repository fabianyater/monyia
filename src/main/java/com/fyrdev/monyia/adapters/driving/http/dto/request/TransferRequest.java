package com.fyrdev.monyia.adapters.driving.http.dto.request;

public record TransferRequest(Long fromPocketId, Long toPocketId, Double amount) {
}
