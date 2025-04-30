package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.LoanTransactionsResponse;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.TransactionResponseSummary;
import com.fyrdev.monyia.domain.model.TransactionSummaryByCategoriesResponse;
import com.fyrdev.monyia.domain.model.enums.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionPersistencePort {
    Transaction saveNewTransaction(Transaction transaction);
    BigDecimal getMonthlyIncome(Long pocketId, Long userId);
    BigDecimal getMonthlyExpense(Long pocketId, Long userId);
    List<TransactionSummaryByCategoriesResponse> getTransactionSummaryByCategories(Long pocketId, Long userId, TransactionType transactionType);
    List<TransactionResponseSummary> listTransactionsByCategory(Long pocketId, Long userId, Long categoryId, String transactionType, String categoryName);
    List<LoanTransactionsResponse> findAllTransactionsByLoanId(Long loanId);
}
