package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.Loan;
import com.fyrdev.monyia.domain.model.Pocket;

import java.util.List;

public interface ILoanPersistencePort {
    Loan saveLoan(Loan loan);
    List<Loan> findAllLoans(Long userId);
    Loan findLoanDetails(Long loanId, Long userId);
    Double totalLoaned(Long userId);
}
