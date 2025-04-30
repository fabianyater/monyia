package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.LoanTransactionsResponse;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.TransactionResponseSummary;
import com.fyrdev.monyia.domain.model.TransactionSummaryByCategoriesResponse;
import com.fyrdev.monyia.domain.model.enums.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionServicePort {
    Transaction saveNewTransaction(Transaction transaction);
    BigDecimal getMonthlyIncome(Long pocketId);
    BigDecimal getMonthlyExpense(Long pocketId);
    List<TransactionSummaryByCategoriesResponse> getTransactionSummaryByCategories(Long pocketId, TransactionType transactionType);
    List<TransactionResponseSummary> listTransactionsByCategory(Long pocketId, String transactionType, String categoryName);
    List<LoanTransactionsResponse> findAllTransactionsByLoanId(Long loanId);
}
