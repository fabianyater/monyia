package com.fyrdev.monyia.domain.model;

import com.fyrdev.monyia.domain.model.enums.Periodicity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Budget {
    private Long id;
    private String name;
    private String description;
    private BigDecimal amount;
    private BigDecimal balance;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Periodicity periodicity;
    private Integer dayOfTheMonth;
    private String emoji;
    private Long categoryId;
    private Long userId;

    public Budget() {
    }

    public Budget(Long id, String name, String description, BigDecimal amount, BigDecimal balance, LocalDateTime startDate, LocalDateTime endDate, Periodicity periodicity, Integer dayOfTheMonth, String emoji, Long categoryId, Long userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.balance = balance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodicity = periodicity;
        this.dayOfTheMonth = dayOfTheMonth;
        this.emoji = emoji;
        this.categoryId = categoryId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getDayOfTheMonth() {
        return dayOfTheMonth;
    }

    public void setDayOfTheMonth(Integer dayOfTheMonth) {
        this.dayOfTheMonth = dayOfTheMonth;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
}
