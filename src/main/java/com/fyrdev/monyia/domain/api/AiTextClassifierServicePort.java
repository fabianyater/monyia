package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.dto.ClassificationResult;

import java.util.List;

public interface AiTextClassifierServicePort {
    ClassificationResult classifyTransaction(String text);
    List<String> suggestEmojis(String categoryName);
}
