package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.domain.api.AiTextClassifierServicePort;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.dto.ClassificationResult;
import com.fyrdev.monyia.domain.spi.AITextClassifierPort;

import java.util.List;

public class AiClassifierUseCase implements AiTextClassifierServicePort {
    private final AITextClassifierPort classifierPort;
    private final ICategoryServicePort categoryServicePort;

    public AiClassifierUseCase(AITextClassifierPort classifierPort, ICategoryServicePort categoryServicePort) {
        this.classifierPort = classifierPort;
        this.categoryServicePort = categoryServicePort;
    }

    @Override
    public ClassificationResult classifyTransaction(String text) {
        ClassificationResult result = classifierPort.classifyTransaction(text);
        Category category = categoryServicePort.getCategoryByName(result.category().getName());

        return new ClassificationResult(
                result.date(),
                result.periodicity(),
                category,
                result.value(),
                result.type(),
                result.description()
        );
    }

    @Override
    public List<String> suggestEmojis(String categoryName) {
        return classifierPort.suggestEmojis(categoryName);
    }
}
