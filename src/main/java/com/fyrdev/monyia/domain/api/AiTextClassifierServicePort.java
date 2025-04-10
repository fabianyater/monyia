package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.ClassificationResult;

import java.time.LocalDateTime;

public interface AiTextClassifierServicePort {
    ClassificationResult classifyTransaction(String text);
}
