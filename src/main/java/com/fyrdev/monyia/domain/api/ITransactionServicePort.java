package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.dto.GoalTransactionsResponse;
import com.fyrdev.monyia.domain.model.dto.LoanTransactionsResponse;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.dto.TransactionResponseSummary;
import com.fyrdev.monyia.domain.model.dto.TransactionSummaryByCategoriesResponse;
import com.fyrdev.monyia.domain.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ITransactionServicePort {
    Transaction saveNewTransaction(Transaction transaction);
    BigDecimal getMonthlyIncome(Long pocketId, LocalDateTime startDate);
    BigDecimal getMonthlyExpense(Long pocketId, LocalDateTime startDate);
    List<TransactionSummaryByCategoriesResponse> getTransactionSummaryByCategories(Long pocketId, TransactionType transactionType, LocalDateTime startDate);
    List<TransactionResponseSummary> listTransactionsByCategory(Long pocketId, TransactionType transactionType, String categoryName, LocalDate startDate, LocalDate endDate);
    List<LoanTransactionsResponse> findAllTransactionsByLoanId(Long loanId, String loanType);
    List<GoalTransactionsResponse> findAllTransactionsByGoalId(Long goalId);
    Double sumByUserAndDateRangeAndType(Long pocketId, LocalDateTime startDate, LocalDateTime endDate, TransactionType type);
    List<Transaction> getLatestTransactionsByPocketId(Long pocketId);
    List<TransactionResponseSummary> getTransactionsByPocketId(Long pocketId, LocalDate startMonth);
}
