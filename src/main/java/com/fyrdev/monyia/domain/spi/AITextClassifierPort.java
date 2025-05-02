package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.dto.ClassificationResult;

import java.util.List;

public interface AITextClassifierPort {
    ClassificationResult classifyTransaction(String text);
    List<String> suggestEmojis(String categoryName);
}
