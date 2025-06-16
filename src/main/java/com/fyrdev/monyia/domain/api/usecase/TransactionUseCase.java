package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import com.fyrdev.monyia.domain.exception.PocketNotFoundExceptiont;
import com.fyrdev.monyia.domain.model.*;
import com.fyrdev.monyia.domain.model.dto.GoalTransactionsResponse;
import com.fyrdev.monyia.domain.model.dto.LoanTransactionsResponse;
import com.fyrdev.monyia.domain.model.dto.TransactionResponseSummary;
import com.fyrdev.monyia.domain.model.dto.TransactionSummaryByCategoriesResponse;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.ICategoryPersistencePort;
import com.fyrdev.monyia.domain.spi.IPocketPersistencePort;
import com.fyrdev.monyia.domain.spi.ITransactionPersistencePort;
import com.fyrdev.monyia.domain.util.DomainConstants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class TransactionUseCase implements ITransactionServicePort {
    private final ITransactionPersistencePort transactionPersistencePort;
    private final IPocketPersistencePort pocketPersistencePort;
    private final IAuthenticationPort authenticationPort;
    private final ICategoryPersistencePort categoryPersistencePort;

    public TransactionUseCase(ITransactionPersistencePort transactionPersistencePort,
                              IPocketPersistencePort pocketPersistencePort,
                              IAuthenticationPort authenticationPort,
                              ICategoryPersistencePort categoryPersistencePort) {
        this.transactionPersistencePort = transactionPersistencePort;
        this.pocketPersistencePort = pocketPersistencePort;
        this.authenticationPort = authenticationPort;
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public Transaction saveNewTransaction(Transaction transaction) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        TransactionType type = transaction.getTransactionType();

        if (transaction.getUuid() == null) {
            transaction.setUuid(UUID.randomUUID());
        }

        if (transaction.getPocketId() != null) {
            Pocket pocket = pocketPersistencePort.getPocketByIdAndUserId(transaction.getPocketId(), userId);
            if (pocket == null) {
                throw new PocketNotFoundExceptiont(DomainConstants.POCKET_NOT_FOUND_MESSAGE);
            }
            transaction.setPocketId(pocket.getId());

            if (type != TransactionType.TRANSFER) {
                updatePocketBalance(pocket, transaction.getAmount(), type);
            }
        }

        return transactionPersistencePort.saveNewTransaction(transaction);
    }

    @Override
    public BigDecimal getMonthlyIncome(Long pocketId, LocalDateTime startDate) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return transactionPersistencePort.getMonthlyIncome(pocketId, userId, startDate);
    }

    @Override
    public BigDecimal getMonthlyExpense(Long pocketId, LocalDateTime startDate) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return transactionPersistencePort.getMonthlyExpense(pocketId, userId, startDate);
    }

    @Override
    public List<TransactionSummaryByCategoriesResponse> getTransactionSummaryByCategories(Long pocketId, TransactionType transactionType, LocalDateTime startDate) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return transactionPersistencePort.getTransactionSummaryByCategories(pocketId, userId, transactionType, startDate)
                .stream()
                .sorted(Comparator
                        .comparing(TransactionSummaryByCategoriesResponse::totalAmount)
                        .reversed())
                .toList()
                ;
    }

    @Override
    public List<TransactionResponseSummary> listTransactionsByCategory(Long pocketId, TransactionType transactionType, String categoryName, LocalDate startDate, LocalDate endDate) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        Category category = categoryPersistencePort.getCategoryByName(categoryName, userId);

        var result = transactionPersistencePort
                .listTransactionsByCategory(pocketId, userId, category.getId(), transactionType, categoryName)
                .stream()
                .sorted(Comparator.comparing(TransactionResponseSummary::date).reversed())
                .toList();

        if (startDate != null && endDate != null) {
            return result.stream()
                    .filter(txn ->
                            !txn.date().isBefore(startDate) &&
                            !txn.date().isAfter(endDate))
                    .toList();
        }

        return result;
    }

    @Override
    public List<LoanTransactionsResponse> findAllTransactionsByLoanId(Long loanId, String loanType) {

        return transactionPersistencePort.findAllTransactionsByLoanId(loanId, loanType)
                .stream()
                .sorted(Comparator.comparing(LoanTransactionsResponse::date).reversed())
                .toList();
    }

    @Override
    public List<GoalTransactionsResponse> findAllTransactionsByGoalId(Long goalId) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return transactionPersistencePort.findAllTransactionsByGoalId(goalId, userId)
                .stream()
                .sorted(Comparator.comparing(GoalTransactionsResponse::date).reversed())
                .toList();
    }

    @Override
    public Double sumByUserAndDateRangeAndType(Long pocketId, LocalDateTime startDate, LocalDateTime endDate, TransactionType type) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        Double result = transactionPersistencePort.sumByUserAndDateRangeAndType(userId, pocketId, type, startDate, endDate);

        return result != null ? result : 0.0;
    }

    @Override
    public List<Transaction> getLatestTransactionsByPocketId(Long pocketId) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return transactionPersistencePort.getLatestTransactionsByPocketIdAndUserId(pocketId, userId)
                .stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .limit(5)
                .toList();
    }

    private void updatePocketBalance(Pocket pocket, BigDecimal amount, TransactionType transactionType) {
        if (transactionType == TransactionType.EXPENSE) {
            pocketPersistencePort.updateBalanceById(pocket.getBalance().subtract(amount), pocket.getId());
        } else {
            pocketPersistencePort.updateBalanceById(pocket.getBalance().add(amount), pocket.getId());
        }
    }
}
