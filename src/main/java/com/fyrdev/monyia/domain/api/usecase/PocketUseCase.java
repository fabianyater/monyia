package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.configuration.exceptionhandler.ResourceNotFoundException;
import com.fyrdev.monyia.domain.api.IPocketServicePort;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.IPocketPersistencePort;
import com.fyrdev.monyia.domain.util.DomainConstants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PocketUseCase implements IPocketServicePort {
    private final IPocketPersistencePort pocketPersistencePort;
    private final IAuthenticationPort authenticationPort;

    public PocketUseCase(IPocketPersistencePort pocketPersistencePort, IAuthenticationPort authenticationPort) {
        this.pocketPersistencePort = pocketPersistencePort;
        this.authenticationPort = authenticationPort;
    }

    @Override
    public void saveNewPocket(Pocket pocket) {
        if (pocket.getUuid() == null) {
            pocket.setUuid(UUID.randomUUID());
        }

        pocket.setDate(LocalDateTime.now());
        pocket.setUserId(authenticationPort.getAuthenticatedUserId());

        pocketPersistencePort.saveNewPocket(pocket);
    }

    @Override
    public void saveNewPocket(Pocket pocket, Long userId) {
        if (pocket.getUuid() == null) {
            pocket.setUuid(UUID.randomUUID());
        }

        pocket.setDate(LocalDateTime.now());
        pocket.setUserId(userId);

        pocketPersistencePort.saveNewPocket(pocket);
    }

    @Override
    public Double getTotalBalanceByTransactionType(Long pocketId, TransactionType transactionType) {
        return 0.0;
    }

    @Override
    public List<Pocket> getPockets(Long userId) {
        return pocketPersistencePort.getPocketsByUserId(userId);
    }
}
