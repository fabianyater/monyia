package com.fyrdev.monyia.adapters.driving.http.dto.request;

import java.math.BigDecimal;

public record UpdatePocketRequest(String name, String emoji, BigDecimal balance, Boolean excludeBalance) {
}
