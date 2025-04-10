package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.enums.TransactionType;

import java.math.BigDecimal;

public interface ITransactionPersistencePort {
    Transaction saveNewTransaction(Transaction transaction);
    BigDecimal getMonthlyIncome(Long pocketId, Long userId);
    BigDecimal getMonthlyExpense(Long pocketId, Long userId);
}
