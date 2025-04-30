package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.enums.TransactionType;

import java.util.List;

public interface IPocketPersistencePort {
    void saveNewPocket(Pocket pocket);
    List<Pocket> getPocketsByUserId(Long userId);
    Pocket getPocketByIdAndUserId(Long pocketId, Long userId);
    Double getBalance(Long pocketId, Long userId);
    int updateBalanceById(Double balance, Long pocketId);
    Double getTotalBalanceByUserId(Long userId);
}
