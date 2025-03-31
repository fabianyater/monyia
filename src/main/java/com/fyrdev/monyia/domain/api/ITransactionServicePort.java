package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.Transaction;

import java.math.BigDecimal;

public interface ITransactionServicePort {
    void saveNewTransaction(Transaction transaction);
    BigDecimal getMonthlyIncome(Long pocketId);
    BigDecimal getMonthlyExpense(Long pocketId);
    BigDecimal getMonthlyTotal(Long pocketId);
}
