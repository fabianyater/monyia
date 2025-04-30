package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.enums.TransactionType;

import java.util.List;

public interface IPocketServicePort {
    void saveNewPocket(Pocket pocket);
    void saveNewPocket(Pocket pocket, Long userId);
    Pocket getPocketByIdAndUserId(Long pocketId);
    Double getBalance(Long pocketId);
    List<Pocket> getPockets();
    Double getTotalBalanceByUserId();
    boolean isPocketBalanceSufficient(Long pocketId, Double amount);
}
