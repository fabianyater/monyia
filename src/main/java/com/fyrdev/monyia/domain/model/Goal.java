package com.fyrdev.monyia.domain.model;

import java.time.LocalDateTime;

public class Goal {
    private Long id;
    private String name;
    private Double amount;
    private Double balance;
    private String emoji;
    private LocalDateTime dueDate;
    private Long userId;

    public Goal() {
    }

    public Goal(Long id, String name, Double amount, Double balance, String emoji, LocalDateTime dueDate, Long userId) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.balance = balance;
        this.emoji = emoji;
        this.dueDate = dueDate;
        this.userId = userId;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
