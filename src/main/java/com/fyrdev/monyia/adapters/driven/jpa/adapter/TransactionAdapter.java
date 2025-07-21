package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.domain.model.dto.GoalTransactionsResponse;
import com.fyrdev.monyia.domain.model.dto.LoanTransactionsResponse;
import com.fyrdev.monyia.domain.model.dto.TransactionResponseSummary;
import com.fyrdev.monyia.adapters.driven.jpa.entity.TransactionEntity;
import com.fyrdev.monyia.adapters.driven.jpa.mapper.ITransactionEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.ITransactionRepository;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.dto.TransactionSummaryByCategoriesResponse;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import com.fyrdev.monyia.domain.spi.ITransactionPersistencePort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class TransactionAdapter implements ITransactionPersistencePort {
    private final ITransactionRepository transactionRepository;
    private final ITransactionEntityMapper transactionEntityMapper;

    @Override
    public Transaction saveNewTransaction(Transaction transaction) {
        TransactionEntity transactionEntity = transactionEntityMapper.toEntity(transaction);

        if (transaction.getLoanId() == null) {
            transactionEntity.setLoanEntity(null); // Asignar explícitamente a null
        }

        if (transaction.getGoalId() == null) {
            transactionEntity.setGoalEntity(null);
        }

        if (transaction.getPocketId() == null) {
            transactionEntity.setPocketEntity(null);
        }

        if (transaction.getToPocketId() == null) {
            transactionEntity.setDestinationPocketEntity(null);
        }

        transactionRepository.save(transactionEntity);

        return transactionEntityMapper.toTransaction(transactionEntity);
    }

    @Override
    public BigDecimal getMonthlyIncome(Long pocketId, Long userId, LocalDateTime startDate) {
        return transactionRepository.getMonthlyIncome(pocketId, userId, getStartOfMonth(startDate), getStartOfNextMonth(startDate));
    }

    @Override
    public BigDecimal getMonthlyExpense(Long pocketId, Long userId, LocalDateTime startDate) {
        return transactionRepository.getMonthlyExpense(pocketId, userId, getStartOfMonth(startDate), getStartOfNextMonth(startDate));
    }

    @Override
    public List<TransactionSummaryByCategoriesResponse> getTransactionSummaryByCategories(Long pocketId, Long userId, TransactionType transactionType, LocalDateTime startDate) {
        List<TransactionType> types = List.of(transactionType, TransactionType.TRANSFER);

        return transactionRepository
                .getTotalAmountByCategoryGrouped(types, userId, pocketId, getStartOfMonth(startDate), getStartOfNextMonth(startDate))
                .stream()
                .map(obj -> new TransactionSummaryByCategoriesResponse(
                        (Long) obj[0],
                        (String) obj[1],
                        (String) obj[2],
                        ((BigDecimal) obj[3]).longValue()
                ))
                .toList();
    }

    @Override
    public List<TransactionResponseSummary> listTransactionsByCategory(Long pocketId, Long userId, Long categoryId, TransactionType transactionType, String categoryName) {

        return transactionRepository
                .findTransactionsByFilters(pocketId, userId, categoryId, transactionType.name(), categoryName)
                .stream()
                .map(obj -> new TransactionResponseSummary(
                        (Long) obj[0],
                        (String) obj[1],
                        ((BigDecimal) obj[2]).doubleValue(),
                        ((Timestamp) obj[3]).toLocalDateTime().toLocalDate(),
                        (String) obj[4],
                        (String) obj[5],
                        null
                ))
                .toList();
    }

    @Override
    public List<LoanTransactionsResponse> findAllTransactionsByLoanId(Long loanId, String loanType) {

        return transactionRepository
                .findLoanPaymentsByLoanIdAndType(loanId, loanType)
                .stream()
                .map(obj -> new LoanTransactionsResponse(
                        (Long) obj[0],
                        (String) obj[1],
                        (String) obj[2],
                        (String) obj[3],
                        ((BigDecimal) obj[4]).doubleValue(),
                        ((Timestamp) obj[5]).toLocalDateTime().toString()
                ))
                .toList();
    }

    @Override
    public List<GoalTransactionsResponse> findAllTransactionsByGoalId(Long goalId, Long userId) {
        return transactionRepository.findGoalTransactionByGoalId(goalId, userId)
                .stream()
                .map(obj -> new GoalTransactionsResponse(
                        (Long) obj[0],
                        (String) obj[1],
                        (String) obj[2],
                        (String) obj[3],
                        ((BigDecimal) obj[4]).doubleValue(),
                        ((Timestamp) obj[5]).toLocalDateTime().toString(),
                        (String) obj[6]
                ))
                .toList();
    }

    @Override
    public Double sumByUserAndDateRangeAndType(Long userId, Long pocketId, TransactionType type, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.sumByUserAndPocketAndDateRangeAndType(userId, pocketId, type.name(), startDate, endDate);
    }

    @Override
    public List<Transaction> getLatestTransactions(Long pocketId, Long userId) {
        var transactions = transactionRepository.findLatestTransactions(pocketId, userId);
        return transactionEntityMapper.toTransactionLs(transactions);
    }

    @Override
    public List<TransactionResponseSummary> getTransactions(Long pocketId, Long userId, LocalDate startMonth) {
        LocalDateTime startDate = startMonth.atStartOfDay();
        LocalDateTime endDate = startDate.plusMonths(1);

        return transactionRepository
                .findByPocketAndUserAndMonth(pocketId, userId, startDate, endDate)
                .stream()
                .map(obj -> new TransactionResponseSummary(
                        obj.getId(),
                        obj.getDescription(),
                        obj.getAmount().doubleValue(),
                        obj.getDate().toLocalDate(),
                        obj.getCategoryEntity().getName(),
                        obj.getCategoryEntity().getDefaultEmoji(),
                        obj.getTransactionType().name()
                ))
                .toList();
    }

    private LocalDateTime getStartOfMonth(LocalDateTime startDate) {
        return startDate.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    private LocalDateTime getStartOfNextMonth(LocalDateTime startDate) {
        return getStartOfMonth(startDate).plusMonths(1);
    }

}
