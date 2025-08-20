package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.dto.PocketBalanceSummary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IPocketServicePort {
    void saveNewPocket(Pocket pocket);
    void saveNewPocket(Pocket pocket, Long userId);
    Pocket getPocketByIdAndUserId(Long pocketId);
    PocketBalanceSummary getBalance(Long pocketId, LocalDate startDate);
    List<Pocket> getPockets();
    Double getTotalBalanceByUserId();
    boolean isPocketBalanceSufficient(Long pocketId, BigDecimal amount);
    void transferBetweenPockets(Long fromPocketId, Long toPocketId, BigDecimal amount);
    void updatePocketById(Long pocketId, Pocket pocket);
    void updateBalanceById(BigDecimal balance, Long pocketId);
    void makeTransactionWithPreviousBalance();
}
