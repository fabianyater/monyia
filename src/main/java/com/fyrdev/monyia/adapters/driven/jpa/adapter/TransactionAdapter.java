package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.entity.TransactionEntity;
import com.fyrdev.monyia.adapters.driven.jpa.mapper.ITransactionEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.ITransactionRepository;
import com.fyrdev.monyia.adapters.driving.http.dto.response.TransactionResponse;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.dto.IncomeAndExpenseSummary;
import com.fyrdev.monyia.domain.model.dto.TransactionSummaryByCategoriesResponse;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import com.fyrdev.monyia.domain.spi.ITransactionPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

        if (transaction.getBudgetId() == null) {
            transactionEntity.setBudgetEntity(null);
        }

        transactionRepository.save(transactionEntity);

        return transactionEntityMapper.toTransaction(transactionEntity);
    }

    @Override
    public List<IncomeAndExpenseSummary> getMonthlyIncomeAndExpenseSummary(Long pocketId, Long userId, LocalDateTime startDate) {
        return transactionRepository.getMonthlyIncomeAndExpenseSummary(
                        pocketId,
                        userId,
                        getStartOfMonth(startDate),
                        getStartOfNextMonth(startDate))
                .stream()
                .map(obj -> new IncomeAndExpenseSummary(
                        (BigDecimal) obj[0],
                        (String) obj[1]
                ))
                .toList();
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
    public List<TransactionResponse> listTransactionsByCategory(Long pocketId, Long userId, Long categoryId, TransactionType transactionType, String categoryName) {

        return transactionRepository
                .findTransactionsByFilters(pocketId, userId, categoryId, transactionType.name(), categoryName)
                .stream()
                .map(obj -> new TransactionResponse(
                        (Long) obj[0],
                        (String) obj[1],
                        ((BigDecimal) obj[2]),
                        ((Timestamp) obj[3]).toLocalDateTime(),
                        (String) obj[4],
                        (String) obj[5],
                        (String) obj[6],
                        (String) obj[8],
                        (Boolean) obj[7]
                ))
                .toList();
    }

    @Override
    public List<TransactionResponse> findAllTransactionsByLoanId(Long loanId, String loanType) {

        return transactionRepository
                .findLoanPaymentsByLoanIdAndType(loanId, loanType)
                .stream()
                .map(obj -> new TransactionResponse(
                        (Long) obj[0],
                        null,
                        ((BigDecimal) obj[4]),
                        ((Timestamp) obj[5]).toLocalDateTime(),
                        (String) obj[2],
                        (String) obj[1],
                        null,
                        (String) obj[3],
                        false
                ))
                .toList();
    }

    @Override
    public List<TransactionResponse> findAllTransactionsByGoalId(Long goalId, Long userId) {
        return transactionRepository.findGoalTransactionByGoalId(goalId, userId)
                .stream()
                .map(obj -> new TransactionResponse(
                        (Long) obj[0],
                        (String) obj[1],
                        ((BigDecimal) obj[4]),
                        ((Timestamp) obj[5]).toLocalDateTime(),
                        (String) obj[3],
                        (String) obj[2],
                        (String) obj[6],
                        null,
                        false
                ))
                .toList();
    }

    @Override
    public List<TransactionResponse> findAllTransactionsByBudgetId(Long budgetId, Long userId) {
        return transactionRepository.findBudgetTransactionsByBudgetId(budgetId, userId)
                .stream()
                .map(obj -> new TransactionResponse(
                        (Long) obj[0],
                        (String) obj[1],
                        ((BigDecimal) obj[4]),
                        ((Timestamp) obj[5]).toLocalDateTime(),
                        (String) obj[3],
                        (String) obj[2],
                        (String) obj[6],
                        null,
                        false
                ))
                .toList();
    }

    @Override
    public List<Transaction> getLatestTransactions(Long pocketId, Long userId) {
        var transactions = transactionRepository.findLatestTransactions(pocketId, userId);
        return transactionEntityMapper.toTransactionLs(transactions);
    }

    @Override
    public List<TransactionResponse> getTransactions(Long pocketId, Long userId, LocalDate startMonth) {
        LocalDateTime startDate = startMonth.atStartOfDay();
        LocalDateTime endDate = startDate.plusMonths(1);

        return transactionRepository
                .findByPocketAndUserAndMonth(pocketId, userId, startDate, endDate)
                .stream()
                .map(obj -> new TransactionResponse(
                        obj.getId(),
                        obj.getDescription(),
                        obj.getAmount(),
                        obj.getDate(),
                        obj.getCategoryEntity().getName(),
                        obj.getCategoryEntity().getDefaultEmoji(),
                        obj.getTransactionType().name(),
                        null,
                        obj.isTransfer()
                ))
                .toList();
    }

    @Override
    public List<TransactionResponse> getAllTransactionsByUserId(Long userId, Integer page, Integer size, String order) {
        Sort.Direction sortDirection = Sort.Direction.fromString(order.toUpperCase());
        Sort.Order sortOrder = new Sort.Order(sortDirection, "date");
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrder));

        return transactionRepository.findAllTransactionsByUserId(userId, pageable)
                .stream()
                .map(obj -> new TransactionResponse(
                        obj.getId(),
                        obj.getDescription(),
                        obj.getAmount(),
                        obj.getDate(),
                        obj.getCategoryEntity().getName(),
                        obj.getCategoryEntity().getDefaultEmoji(),
                        obj.getTransactionType().name(),
                        obj.getPocketEntity().getName(),
                        obj.isTransfer()
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
