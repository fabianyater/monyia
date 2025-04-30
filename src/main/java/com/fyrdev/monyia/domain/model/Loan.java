package com.fyrdev.monyia.domain.model;

import com.fyrdev.monyia.domain.model.enums.LoanType;

import java.time.LocalDateTime;

public class Loan {
    private Long id;
    private Double amount;
    private Double balance;
    private String loanParty;
    private String description;
    private LoanType loanType;
    private LocalDateTime startDate;
    private Long pocketId;

    public Loan() {
    }

    public Loan(Long id, Double amount, Double balance, String loanParty, String description, LoanType loanType, LocalDateTime startDate, Long pocketId) {
        this.id = id;
        this.amount = amount;
        this.balance = balance;
        this.loanParty = loanParty;
        this.description = description;
        this.loanType = loanType;
        this.startDate = startDate;
        this.pocketId = pocketId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getLoanParty() {
        return loanParty;
    }

    public void setLoanParty(String loanParty) {
        this.loanParty = loanParty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Long getPocketId() {
        return pocketId;
    }

    public void setPocketId(Long pocketId) {
        this.pocketId = pocketId;
    }
}
