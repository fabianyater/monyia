package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.ClassificationResult;
import com.fyrdev.monyia.domain.model.EmojiSuggestion;

import java.time.LocalDateTime;
import java.util.List;

public interface AITextClassifierPort {
    ClassificationResult classifyTransaction(String text);
    List<String> suggestEmojis(String categoryName);
}
