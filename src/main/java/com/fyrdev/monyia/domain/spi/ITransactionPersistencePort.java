package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.Transaction;

import java.math.BigDecimal;

public interface ITransactionPersistencePort {
    void saveNewTransaction(Transaction transaction);
    BigDecimal getMonthlyIncome(Long pocketId, Long userId);
    BigDecimal getMonthlyExpense(Long pocketId, Long userId);
    BigDecimal getMonthlyTotal(Long pocketId, Long userId);
}
