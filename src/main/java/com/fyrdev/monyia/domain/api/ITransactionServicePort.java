package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.adapters.driving.http.dto.response.TransactionResponse;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.dto.*;
import com.fyrdev.monyia.domain.model.enums.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ITransactionServicePort {
    Transaction saveNewTransaction(Transaction transaction);
    List<IncomeAndExpenseSummary> getMonthlyIncomeAndExpenseSummary(Long pocketId, LocalDateTime startDate);
    List<TransactionSummaryByCategoriesResponse> getTransactionSummaryByCategories(Long pocketId, TransactionType transactionType, LocalDateTime startDate);
    List<TransactionResponse> listTransactionsByCategory(Long pocketId, TransactionType transactionType, String categoryName, LocalDateTime startDate, LocalDateTime endDate);
    List<TransactionResponse> findAllTransactionsByLoanId(Long loanId, String loanType);
    List<TransactionResponse> findAllTransactionsByGoalId(Long goalId);
    List<TransactionResponse> findAllTransactionsByBudgetId(Long budgetId);
    List<Transaction> getLatestTransactionsByPocketId(Long pocketId);
    List<TransactionResponse> getTransactionsByPocketId(Long pocketId, LocalDate startMonth);
    List<TransactionResponse> getAllTransactionsWithFilters(QueryFilters filters, Integer page, Integer size, String order);
}
