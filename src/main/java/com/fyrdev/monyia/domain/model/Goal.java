package com.fyrdev.monyia.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Goal {
    private Long id;
    private String name;
    private BigDecimal amount;
    private BigDecimal balance;
    private String emoji;
    private LocalDateTime dueDate;
    private Long userId;

    public Goal() {
    }

    public Goal(Long id, String name, BigDecimal amount, BigDecimal balance, String emoji, LocalDateTime dueDate, Long userId) {
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
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
