package com.fyrdev.monyia.domain.spi;

public interface AiClient {
    String sendPrompt(String prompt, String userText);
}
