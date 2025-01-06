package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.Transaction;

public interface ITransactionServicePort {
    void saveNewTransaction(Transaction transaction);
}
