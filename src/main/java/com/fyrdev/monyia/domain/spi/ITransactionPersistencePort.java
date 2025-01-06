package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.Transaction;

public interface ITransactionPersistencePort {
    void saveNewTransaction(Transaction transaction);
}
