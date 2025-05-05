package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.domain.model.Pocket;
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
import java.util.Locale;

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
    public BigDecimal getMonthlyIncome(Long pocketId, Long userId) {
        return transactionRepository.getMonthlyIncome(pocketId, userId, getStartOfMonth(), getStartOfNextMonth());
    }

    @Override
    public BigDecimal getMonthlyExpense(Long pocketId, Long userId) {
        return transactionRepository.getMonthlyExpense(pocketId, userId, getStartOfMonth(), getStartOfNextMonth());
    }

    @Override
    public List<TransactionSummaryByCategoriesResponse> getTransactionSummaryByCategories(Long pocketId, Long userId, TransactionType transactionType) {
        List<TransactionType> types = List.of(transactionType, TransactionType.TRANSFER);

        return transactionRepository
                .getTotalAmountByCategoryGrouped(types, userId, pocketId)
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

        var result = transactionRepository
                .findTransactionsByFilters(pocketId, userId, categoryId, transactionType.name(), categoryName)
                .stream()
                .map(obj -> new TransactionResponseSummary(
                        (Long) obj[0],
                        (String) obj[1],
                        ((BigDecimal) obj[2]).doubleValue(),
                        ((Timestamp) obj[3]).toLocalDateTime().toLocalDate(),
                        (String) obj[4],
                        (String) obj[5]
                ))
                .toList();

        return result;
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

    private LocalDateTime getStartOfMonth() {
        return LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    private LocalDateTime getStartOfNextMonth() {
        return getStartOfMonth().plusMonths(1);
    }

}
