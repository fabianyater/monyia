package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.mapper.ITransactionEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.ITransactionRepository;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.spi.ITransactionPersistencePort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TransactionAdapter implements ITransactionPersistencePort {
    private final ITransactionRepository transactionRepository;
    private final ITransactionEntityMapper transactionEntityMapper;

    @Override
    public void saveNewTransaction(Transaction transaction) {
        transactionRepository.save(transactionEntityMapper.toEntity(transaction));
    }

    @Override
    public BigDecimal getMonthlyIncome(Long pocketId, Long userId) {
        return transactionRepository.getMonthlyIncome(pocketId, userId, getStartOfMonth(), getStartOfNextMonth());
    }

    @Override
    public BigDecimal getMonthlyExpense(Long pocketId, Long userId) {
        return transactionRepository.getMonthlyExpense(pocketId, userId, getStartOfMonth(), getStartOfNextMonth());
    }

    @Override
    public BigDecimal getMonthlyTotal(Long pocketId, Long userId) {
        return transactionRepository.getMonthlyTotal(pocketId, userId, getStartOfMonth(), getStartOfNextMonth());
    }

    private LocalDateTime getStartOfMonth() {
        return LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    private LocalDateTime getStartOfNextMonth() {
        return getStartOfMonth().plusMonths(1);
    }

}
