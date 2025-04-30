package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import com.fyrdev.monyia.domain.exception.PocketNotFoundExceptiont;
import com.fyrdev.monyia.domain.model.*;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.ICategoryPersistencePort;
import com.fyrdev.monyia.domain.spi.IPocketPersistencePort;
import com.fyrdev.monyia.domain.spi.ITransactionPersistencePort;
import com.fyrdev.monyia.domain.util.DomainConstants;

import java.math.BigDecimal;
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
                              IAuthenticationPort authenticationPort, ICategoryPersistencePort categoryPersistencePort) {
        this.transactionPersistencePort = transactionPersistencePort;
        this.pocketPersistencePort = pocketPersistencePort;
        this.authenticationPort = authenticationPort;
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public Transaction saveNewTransaction(Transaction transaction) {
        Long userId = authenticationPort.getAuthenticatedUserId();

        Pocket pocket = pocketPersistencePort.getPocketByIdAndUserId(transaction.getPocketId(), userId);

        if (pocket == null) {
            throw new PocketNotFoundExceptiont(DomainConstants.POCKET_NOT_FOUND_MESSAGE);
        }

        TransactionType type = transaction.getTransactionType();

        if (transaction.getUuid() == null) {
            transaction.setUuid(UUID.randomUUID());
        }

        transaction.setPocketId(pocket.getId());
        updatePocketBalance(pocket, transaction.getAmount(), type);

        return transactionPersistencePort.saveNewTransaction(transaction);
    }

    @Override
    public BigDecimal getMonthlyIncome(Long pocketId) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return transactionPersistencePort.getMonthlyIncome(pocketId, userId);
    }

    @Override
    public BigDecimal getMonthlyExpense(Long pocketId) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return transactionPersistencePort.getMonthlyExpense(pocketId, userId);
    }

    @Override
    public List<TransactionSummaryByCategoriesResponse> getTransactionSummaryByCategories(Long pocketId, TransactionType transactionType) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return transactionPersistencePort.getTransactionSummaryByCategories(pocketId, userId, transactionType)
                .stream()
                .sorted(Comparator
                        .comparing(TransactionSummaryByCategoriesResponse::totalAmount)
                        .reversed())
                .toList()
                ;
    }

    @Override
    public List<TransactionResponseSummary> listTransactionsByCategory(Long pocketId, String transactionType, String categoryName) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        Category category = categoryPersistencePort.getCategoryByName(categoryName, userId);

        return transactionPersistencePort
                .listTransactionsByCategory(pocketId, userId, category.getId(), transactionType, categoryName)
                .stream()
                .sorted(Comparator.comparing(TransactionResponseSummary::date).reversed())
                .toList();
    }

    @Override
    public List<LoanTransactionsResponse> findAllTransactionsByLoanId(Long loanId) {

        return transactionPersistencePort.findAllTransactionsByLoanId(loanId)
                .stream()
                .sorted(Comparator.comparing(LoanTransactionsResponse::date).reversed())
                .toList();
    }

    private void updatePocketBalance(Pocket pocket, Double amount, TransactionType transactionType) {
        if (transactionType == TransactionType.EXPENSE) {
            pocketPersistencePort.updateBalanceById(pocket.getBalance() - amount, pocket.getId());
        } else {
            pocketPersistencePort.updateBalanceById(pocket.getBalance() + amount, pocket.getId());
        }
    }
}
