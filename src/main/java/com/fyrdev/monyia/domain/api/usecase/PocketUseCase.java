package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.configuration.exceptionhandler.ResourceNotFoundException;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.api.IPocketServicePort;
import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import com.fyrdev.monyia.domain.exception.InsufficientBalanceException;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.enums.Periodicity;
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
    private final ITransactionServicePort transactionServicePort;
    private final ICategoryServicePort categoryServicePort;

    public PocketUseCase(IPocketPersistencePort pocketPersistencePort,
                         IAuthenticationPort authenticationPort,
                         AITextClassifierPort aiTextClassifierPort,
                         ITransactionServicePort transactionServicePort,
                         ICategoryServicePort categoryServicePort) {
        this.pocketPersistencePort = pocketPersistencePort;
        this.authenticationPort = authenticationPort;
        this.aiTextClassifierPort = aiTextClassifierPort;
        this.transactionServicePort = transactionServicePort;
        this.categoryServicePort = categoryServicePort;
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
    public Pocket getPocketByIdAndUserId(Long pocketId) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        Pocket pocket = pocketPersistencePort.getPocketByIdAndUserId(pocketId, userId);

        if (pocket == null) {
            throw new ResourceNotFoundException(DomainConstants.POCKET_NOT_FOUND_MESSAGE);
        }

        return pocket;
    }

    @Override
    public Double getBalance(Long pocketId) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        Pocket pocket = getPocketByIdAndUserId(pocketId);

        return pocketPersistencePort.getBalance(pocket.getId(), userId);
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

    @Override
    public boolean isPocketBalanceSufficient(Long pocketId, Double amount) {
        Double currentBalance = getBalance(pocketId);

        return currentBalance < amount;
    }

    @Override
    public void transferBetweenPockets(Long fromPocketId, Long toPocketId, Double amount) {
        Pocket fromPocket = getPocketByIdAndUserId(fromPocketId);
        Pocket toPocket = getPocketByIdAndUserId(toPocketId);
        String categoryName = "Transferencia";
        Category category = categoryServicePort.getCategoryByName(categoryName);

        if (fromPocket.getBalance() < amount) {
            throw new InsufficientBalanceException(DomainConstants.INSUFFICIENT_BALANCE_MESSAGE);
        }

        fromPocket.setBalance(fromPocket.getBalance() - amount);
        toPocket.setBalance(toPocket.getBalance() + amount);

        pocketPersistencePort.updateBalanceById(fromPocket.getBalance(), fromPocket.getId());
        pocketPersistencePort.updateBalanceById(toPocket.getBalance(), toPocket.getId());

        Transaction fromTransaction = createNewTransaction(amount, fromPocket, toPocket, category, true);
        Transaction toTransaction = createNewTransaction(amount, fromPocket, toPocket, category, false);

        transactionServicePort.saveNewTransaction(fromTransaction);
        transactionServicePort.saveNewTransaction(toTransaction);
    }

    private static Transaction createNewTransaction(Double amount, Pocket fromPocket, Pocket toPocket, Category category, boolean isFromPocket) {
        Transaction transaction = new Transaction();
        transaction.setPocketId(isFromPocket ? fromPocket.getId() : toPocket.getId());
        transaction.setToPocketId(isFromPocket ? toPocket.getId() : null);
        transaction.setAmount(amount);
        transaction.setTransactionType(isFromPocket ? TransactionType.EXPENSE : TransactionType.INCOME);
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(setDescription(isFromPocket ? toPocket.getName() : fromPocket.getName(), isFromPocket));
        transaction.setUuid(UUID.randomUUID());
        transaction.setPeriodicity(Periodicity.ONCE);
        transaction.setCategoryId(category.getId());

        return transaction;
    }

    private static String setDescription(String name, boolean isFromPocket) {
        return isFromPocket ? "Transferencia a " + name : "Transferencia recibida de " + name;
    }
}
