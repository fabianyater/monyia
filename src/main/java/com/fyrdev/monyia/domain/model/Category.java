package com.fyrdev.monyia.domain.model;

public class Category {
    private Long id;
    private String name;
    private String emoji;
    private Long userId;

    public Category(Long id, String name, String emoji, Long userId) {
        this.id = id;
        this.name = name;
        this.emoji = emoji;
        this.userId = userId;
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

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
