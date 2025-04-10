package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import com.fyrdev.monyia.domain.exception.PocketNotFoundExceptiont;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.IPocketPersistencePort;
import com.fyrdev.monyia.domain.spi.ITransactionPersistencePort;
import com.fyrdev.monyia.domain.util.DomainConstants;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionUseCase implements ITransactionServicePort {
    private final ITransactionPersistencePort transactionPersistencePort;
    private final IPocketPersistencePort pocketPersistencePort;
    private final IAuthenticationPort authenticationPort;

    public TransactionUseCase(ITransactionPersistencePort transactionPersistencePort,
                              IPocketPersistencePort pocketPersistencePort,
                              IAuthenticationPort authenticationPort) {
        this.transactionPersistencePort = transactionPersistencePort;
        this.pocketPersistencePort = pocketPersistencePort;
        this.authenticationPort = authenticationPort;
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

    private void updatePocketBalance(Pocket pocket, BigDecimal amount, TransactionType transactionType) {
        if (transactionType == TransactionType.EXPENSE) {
            pocketPersistencePort.updateBalanceById(pocket.getBalance().subtract(amount).longValue(), pocket.getId());
        } else {
            pocketPersistencePort.updateBalanceById(pocket.getBalance().add(amount).longValue(), pocket.getId());
        }
    }
}
