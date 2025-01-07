package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.mapper.ITransactionEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.ITransactionRepository;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.spi.ITransactionPersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionAdapter implements ITransactionPersistencePort {
    private final ITransactionRepository transactionRepository;
    private final ITransactionEntityMapper transactionEntityMapper;

    @Override
    public void saveNewTransaction(Transaction transaction) {
        transactionRepository.save(transactionEntityMapper.toEntity(transaction));
    }
}
