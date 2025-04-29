package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.configuration.exceptionhandler.ResourceNotFoundException;
import com.fyrdev.monyia.domain.api.IPocketServicePort;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.User;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import com.fyrdev.monyia.domain.spi.AITextClassifierPort;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.IPocketPersistencePort;
import com.fyrdev.monyia.domain.util.DomainConstants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PocketUseCase implements IPocketServicePort {
    private final IPocketPersistencePort pocketPersistencePort;
    private final IAuthenticationPort authenticationPort;
    private final AITextClassifierPort aiTextClassifierPort;

    public PocketUseCase(IPocketPersistencePort pocketPersistencePort, IAuthenticationPort authenticationPort, AITextClassifierPort aiTextClassifierPort) {
        this.pocketPersistencePort = pocketPersistencePort;
        this.authenticationPort = authenticationPort;
        this.aiTextClassifierPort = aiTextClassifierPort;
    }

    @Override
    public void saveNewPocket(Pocket pocket) {
        if (pocket.getUuid() == null) {
            pocket.setUuid(UUID.randomUUID());
        }

        pocket.setDate(LocalDateTime.now());
        pocket.setUserId(authenticationPort.getAuthenticatedUserId());

        List<String> emojis = aiTextClassifierPort.suggestEmojis(pocket.getName());

        pocket.setEmoji(emojis.get(0));

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
    public Pocket getBalance(Long pocketId) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        Pocket pocket = pocketPersistencePort.getPocketByIdAndUserId(pocketId, userId);

        if (pocket == null) {
            throw new ResourceNotFoundException(DomainConstants.POCKET_NOT_FOUND_MESSAGE);
        }

        return pocketPersistencePort.getBalance(pocketId);
    }

    @Override
    public List<Pocket> getPockets() {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return pocketPersistencePort.getPocketsByUserId(userId);
    }

    @Override
    public Double getTotalBalanceByUserId() {
        return pocketPersistencePort.getTotalBalanceByUserId(authenticationPort.getAuthenticatedUserId());
    }
}
