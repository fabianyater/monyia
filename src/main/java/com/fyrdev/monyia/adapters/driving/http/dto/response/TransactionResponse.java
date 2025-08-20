package com.fyrdev.monyia.adapters.driving.http.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(Long id,
                                  String description,
                                  BigDecimal value,
                                  LocalDateTime date,
                                  String category,
                                  String emoji,
                                  String type,
                                  String pocketName,
                                  Boolean isTransfer) {
}
