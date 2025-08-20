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
    private Boolean transfer;
    private Long categoryId;
    private Long pocketId;
    private Long toPocketId;
    private Long loanId;
    private Long goalId;
    private Long budgetId;

    public Transaction() {
    }

    public Transaction(Long id, UUID uuid, String description, BigDecimal amount, LocalDateTime date, Periodicity periodicity, TransactionType transactionType, Boolean transfer, Long categoryId, Long pocketId, Long toPocketId, Long loanId, Long goalId, Long budgetId) {
        this.id = id;
        this.uuid = uuid;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.periodicity = periodicity;
        this.transactionType = transactionType;
        this.transfer = transfer;
        this.categoryId = categoryId;
        this.pocketId = pocketId;
        this.toPocketId = toPocketId;
        this.loanId = loanId;
        this.goalId = goalId;
        this.budgetId = budgetId;
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

    public Long getPocketId() {
        return pocketId;
    }

    public void setPocketId(Long pocketId) {
        this.pocketId = pocketId;
    }

    public Long getToPocketId() {
        return toPocketId;
    }

    public void setToPocketId(Long toPocketId) {
        this.toPocketId = toPocketId;
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

    public Long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }

    public Boolean getTransfer() {
        return transfer;
    }

    public void setTransfer(Boolean transfer) {
        this.transfer = transfer;
    }
}
