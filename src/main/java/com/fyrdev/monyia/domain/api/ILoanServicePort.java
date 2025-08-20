package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.Loan;

import java.math.BigDecimal;
import java.util.List;

public interface ILoanServicePort {
    void saveLoan(Loan loan);
    List<Loan> getLoansByPocketId();
    Loan getLoanDetails(Long loanId);
    void makePayment(Long loanId, Long pocketId, BigDecimal amount);
    Double totalLoaned();
    void incrementLoan(Long loanId, BigDecimal amount);
}
