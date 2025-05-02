package com.fyrdev.monyia.domain.model;

import com.fyrdev.monyia.domain.model.enums.Periodicity;
import com.fyrdev.monyia.domain.model.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private Long id;
    private UUID uuid;
    private String description;
    private Double amount;
    private LocalDateTime date;
    private Periodicity periodicity;
    private TransactionType transactionType;
    private Long categoryId;
    private Long pocketId;
    private Long loanId;
    private Long goalId;

    public Transaction() {
    }

    public Transaction(Long id, UUID uuid, String description, Double amount, LocalDateTime date, Periodicity periodicity, TransactionType transactionType, Long categoryId, Long pocketId, Long loanId, Long goalId) {
        this.id = id;
        this.uuid = uuid;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.periodicity = periodicity;
        this.transactionType = transactionType;
        this.categoryId = categoryId;
        this.pocketId = pocketId;
        this.loanId = loanId;
        this.goalId = goalId;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
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

    public Long getPocketId() {
        return pocketId;
    }

    public void setPocketId(Long pocketId) {
        this.pocketId = pocketId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }
}
