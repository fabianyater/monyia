package com.fyrdev.monyia.domain.model;

import java.util.List;

public class Category {
    private Long id;
    private String name;
    private List<String> emojis;
    private String defaultEmoji;
    private Long userId;

    public Category() {
    }

    public Category(Long id, String name, List<String> emojis, String defaultEmoji, Long userId) {
        this.id = id;
        this.name = name;
        this.emojis = emojis;
        this.defaultEmoji = defaultEmoji;
        this.userId = userId;
    }

    public String getDefaultEmoji() {
        return defaultEmoji;
    }

    public void setDefaultEmoji(String defaultEmoji) {
        this.defaultEmoji = defaultEmoji;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEmojis() {
        return emojis;
    }

    public void setEmojis(List<String> emojis) {
        this.emojis = emojis;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
