package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.ClassificationResult;

import java.time.LocalDateTime;
import java.util.List;

public interface AiTextClassifierServicePort {
    ClassificationResult classifyTransaction(String text);
    List<String> suggestEmojis(String categoryName);
}
