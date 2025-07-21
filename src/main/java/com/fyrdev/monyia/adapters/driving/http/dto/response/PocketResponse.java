package com.fyrdev.monyia.adapters.driving.http.dto.response;

public record PocketResponse(Long id, String name, Double balance, String emoji, Boolean excludeBalance) {
}
