package com.fyrdev.monyia.domain.model;

import com.fasterxml.jackson.databind.JsonNode;

public class EmojiSuggestion {
    private final JsonNode emojis;

    public EmojiSuggestion(JsonNode emojis) {
        this.emojis = emojis;
    }

    public JsonNode getEmojis() {
        return emojis;
    }
}
