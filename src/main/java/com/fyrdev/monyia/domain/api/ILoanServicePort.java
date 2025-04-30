package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.Loan;

public interface ILoanServicePort {
    void saveLoan(Loan loan);
}
