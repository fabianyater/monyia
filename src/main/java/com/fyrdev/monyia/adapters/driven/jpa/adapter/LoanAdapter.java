package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.entity.LoanEntity;
import com.fyrdev.monyia.adapters.driven.jpa.mapper.ILoanEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.ILoanRepository;
import com.fyrdev.monyia.domain.model.Loan;
import com.fyrdev.monyia.domain.spi.ILoanPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.List;

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

    @Override
    public List<Loan> findAllLoans(Long userId) {
        return loanRepository.findAllLoansByUserId(userId)
                .stream()
                .map(loanEntityMapper::toLoan)
                .toList();
    }

    @Override
    public Loan findLoanDetails(Long loanId, Long userId) {
        return loanRepository.findLoanDetailsByLoanIdAndUserId(loanId, userId)
                .map(loanEntityMapper::toLoan)
                .orElse(null);
    }

    @Override
    public Double totalLoaned(Long userId) {
        return loanRepository.sumTotalBalance(userId);
    }
}
