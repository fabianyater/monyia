package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.Loan;

public interface ILoanPersistencePort {
    Loan saveLoan(Loan loan);
}
