package com.fyrdev.monyia.domain.model;

public class Category {
    private Long id;
    private String name;
    private String emoji;

    public Category(Long id, String name, String emoji) {
        this.id = id;
        this.name = name;
        this.emoji = emoji;
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
}
