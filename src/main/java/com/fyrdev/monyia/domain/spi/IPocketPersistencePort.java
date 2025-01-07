package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.Pocket;

public interface IPocketPersistencePort {
    void saveNewPocket(Pocket pocket);
    Pocket getPocketByIdAndUserId(Long pocketId, Long userId);
    int updateBalanceById(Long balance, Long pocketId);
}
