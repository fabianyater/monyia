package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import com.fyrdev.monyia.domain.exception.CategoryNotFoundException;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.ICategoryPersistencePort;
import com.fyrdev.monyia.domain.spi.ITransactionPersistencePort;
import com.fyrdev.monyia.domain.util.DomainConstants;

import java.util.UUID;

public class TransactionUseCase implements ITransactionServicePort {
    private final ITransactionPersistencePort transactionPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IAuthenticationPort authenticationPort;


    public TransactionUseCase(ITransactionPersistencePort transactionPersistencePort,
                              ICategoryPersistencePort categoryPersistencePort,
                              IAuthenticationPort authenticationPort) {
        this.transactionPersistencePort = transactionPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
        this.authenticationPort = authenticationPort;
    }

    @Override
    public void saveNewTransaction(Transaction transaction) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        Category category = categoryPersistencePort.getCategoryById(transaction.getCategoryId());
        String type = transaction.getTransactionType().name();

        if (transaction.getUserId() == null) {
            transaction.setUuid(UUID.randomUUID());
        }

        if (category == null) {
            throw new CategoryNotFoundException(DomainConstants.CATEGORY_NOT_FOUND_MESSAGE);
        }

        transaction.setCategoryId(category.getId());
        transaction.setUserId(userId);

        transactionPersistencePort.saveNewTransaction(transaction);
    }
}
