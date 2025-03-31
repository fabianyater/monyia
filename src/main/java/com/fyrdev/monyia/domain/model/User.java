package com.fyrdev.monyia.domain.model;

public class User {
    private Long id;
    private String uuid;
    private String name;
    private String email;
    private String color;
    private String password;

    public User() {
    }

    public User(Long id, String uuid, String name, String email, String color, String password) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.color = color;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
