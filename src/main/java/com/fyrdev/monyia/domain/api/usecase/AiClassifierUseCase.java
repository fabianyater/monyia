package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.domain.api.AiTextClassifierServicePort;
import com.fyrdev.monyia.domain.model.ClassificationResult;
import com.fyrdev.monyia.domain.spi.AITextClassifierPort;

import java.time.LocalDateTime;
import java.util.List;

public class AiClassifierUseCase implements AiTextClassifierServicePort {
    private final AITextClassifierPort classifierPort;

    public AiClassifierUseCase(AITextClassifierPort classifierPort) {
        this.classifierPort = classifierPort;
    }

    @Override
    public ClassificationResult classifyTransaction(String text) {
        return classifierPort.classifyTransaction(text);
    }
}
