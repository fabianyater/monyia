package com.fyrdev.monyia.domain.model;

import com.fyrdev.monyia.domain.model.enums.Periodicity;
import com.fyrdev.monyia.domain.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private Long id;
    private UUID uuid;
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private Periodicity periodicity;
    private TransactionType transactionType;
    private Long categoryId;
    private Long userId;

    public Transaction() {
    }

    public Transaction(Long id, UUID uuid, String description, BigDecimal amount, LocalDateTime date, Periodicity periodicity, TransactionType transactionType, Long categoryId, Long userId) {
        this.id = id;
        this.uuid = uuid;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.periodicity = periodicity;
        this.transactionType = transactionType;
        this.categoryId = categoryId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
