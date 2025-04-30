package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.Loan;

import java.util.List;

public interface ILoanServicePort {
    void saveLoan(Loan loan);
    List<Loan> getLoansByPocketId();
    Loan getLoanDetails(Long loanId);
}
