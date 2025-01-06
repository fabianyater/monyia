package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.domain.api.IPocketServicePort;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.IPocketPersistencePort;

import java.time.LocalDateTime;
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
}
