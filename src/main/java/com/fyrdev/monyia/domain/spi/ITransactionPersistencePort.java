package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.adapters.driving.http.dto.response.TransactionResponse;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.dto.*;
import com.fyrdev.monyia.domain.model.enums.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ITransactionPersistencePort {
    Transaction saveNewTransaction(Transaction transaction);
    List<IncomeAndExpenseSummary> getMonthlyIncomeAndExpenseSummary(Long pocketId, Long userId, LocalDateTime startDate);
    List<TransactionSummaryByCategoriesResponse> getTransactionSummaryByCategories(Long pocketId, Long userId, TransactionType transactionType, LocalDateTime startDate);
    List<TransactionResponse> listTransactionsByCategory(Long pocketId, Long userId, Long categoryId, TransactionType transactionType, String categoryName);
    List<TransactionResponse> findAllTransactionsByLoanId(Long loanId, String loanType);
    List<TransactionResponse> findAllTransactionsByGoalId(Long goalId, Long userId);
    List<TransactionResponse> findAllTransactionsByBudgetId(Long goalId, Long userId);
    List<Transaction> getLatestTransactions(Long pocketId, Long userId);
    List<TransactionResponse> getTransactions(Long pocketId, Long userId, LocalDate startMonth);
    List<TransactionResponse> getAllTransactionsByUserId(Long userId, Integer page, Integer size, String order);
}
