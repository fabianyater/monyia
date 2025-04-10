package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.enums.TransactionType;

import java.math.BigDecimal;

public interface ITransactionServicePort {
    Transaction saveNewTransaction(Transaction transaction);
    BigDecimal getMonthlyIncome(Long pocketId);
    BigDecimal getMonthlyExpense(Long pocketId);
}
