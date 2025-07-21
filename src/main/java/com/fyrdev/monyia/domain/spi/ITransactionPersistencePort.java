package com.fyrdev.monyia.domain.spi;

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

public interface ITransactionPersistencePort {
    Transaction saveNewTransaction(Transaction transaction);
    BigDecimal getMonthlyIncome(Long pocketId, Long userId, LocalDateTime startDate);
    BigDecimal getMonthlyExpense(Long pocketId, Long userId, LocalDateTime startDate);
    List<TransactionSummaryByCategoriesResponse> getTransactionSummaryByCategories(Long pocketId, Long userId, TransactionType transactionType, LocalDateTime startDate);
    List<TransactionResponseSummary> listTransactionsByCategory(Long pocketId, Long userId, Long categoryId, TransactionType transactionType, String categoryName);
    List<LoanTransactionsResponse> findAllTransactionsByLoanId(Long loanId, String loanType);
    List<GoalTransactionsResponse> findAllTransactionsByGoalId(Long goalId, Long userId);
    Double sumByUserAndDateRangeAndType(Long userId, Long pocketId, TransactionType type, LocalDateTime startDate, LocalDateTime endDate);
    List<Transaction> getLatestTransactionsByPocketIdAndUserId(Long pocketId, Long userId);
}
