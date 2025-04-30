package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.entity.LoanEntity;
import com.fyrdev.monyia.adapters.driven.jpa.mapper.ILoanEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.ILoanRepository;
import com.fyrdev.monyia.domain.model.Loan;
import com.fyrdev.monyia.domain.spi.ILoanPersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoanAdapter implements ILoanPersistencePort {
    private final ILoanRepository loanRepository;
    private final ILoanEntityMapper loanEntityMapper;

    @Override
    public Loan saveLoan(Loan loan) {
        LoanEntity loanEntity = loanEntityMapper.toLoanEntity(loan);
        LoanEntity savedLoanEntity = loanRepository.save(loanEntity);

        return loanEntityMapper.toLoan(savedLoanEntity);
    }
}
