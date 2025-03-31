package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.enums.TransactionType;

import java.util.List;

public interface IPocketPersistencePort {
    void saveNewPocket(Pocket pocket);
    List<Pocket> getPocketsByUserId(Long userId);
    Pocket getPocketByIdAndUserId(Long pocketId, Long userId);
    Double getTotalBalanceByTransactionType(Long pocketId, TransactionType transactionType);
    int updateBalanceById(Long balance, Long pocketId);
}
