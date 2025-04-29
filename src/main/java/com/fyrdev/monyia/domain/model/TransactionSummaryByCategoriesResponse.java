package com.fyrdev.monyia.domain.model;

import java.util.List;
import java.util.Objects;

public record TransactionSummaryByCategoriesResponse(Long id, String name, String defaultEmoji, Long totalAmount) {
}
