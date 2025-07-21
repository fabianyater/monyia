package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.configuration.exceptionhandler.ResourceNotFoundException;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.api.IPocketServicePort;
import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import com.fyrdev.monyia.domain.exception.InsufficientBalanceException;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.dto.PocketBalanceSummary;
import com.fyrdev.monyia.domain.model.enums.Periodicity;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import com.fyrdev.monyia.domain.spi.AITextClassifierPort;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.IPocketPersistencePort;
import com.fyrdev.monyia.domain.util.DomainConstants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
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
    public PocketBalanceSummary getBalance(Long pocketId, LocalDate startDate) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        Pocket pocket = getPocketByIdAndUserId(pocketId);
        LocalDate today = startDate;

        // Fechas del mes anterior
        LocalDate startOfPreviousMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfPreviousMonth = today.withDayOfMonth(1).minusDays(1);

        // Fechas del mes actual
        LocalDate startOfCurrentMonth = today.withDayOfMonth(1);
        LocalDate endOfCurrentMonth = today.withDayOfMonth(today.lengthOfMonth());

        // Balance neto del mes anterior (ingresos - gastos)
        double previousIncome = transactionServicePort.sumByUserAndDateRangeAndType(
                pocketId,
                startOfPreviousMonth.atStartOfDay(),
                endOfPreviousMonth.atTime(LocalTime.MAX),
                TransactionType.INCOME
        );

        double previousExpense = transactionServicePort.sumByUserAndDateRangeAndType(
                pocketId,
                startOfPreviousMonth.atStartOfDay(),
                endOfPreviousMonth.atTime(LocalTime.MAX),
                TransactionType.EXPENSE
        );

        double previousMonthlyNetChange = previousIncome - previousExpense;

        // Balance neto del mes actual (ingresos - gastos)
        double currentIncome = transactionServicePort.sumByUserAndDateRangeAndType(
                pocketId,
                startOfCurrentMonth.atStartOfDay(),
                endOfCurrentMonth.atTime(LocalTime.MAX),
                TransactionType.INCOME
        );

        double currentExpense = transactionServicePort.sumByUserAndDateRangeAndType(
                pocketId,
                startOfCurrentMonth.atStartOfDay(),
                endOfCurrentMonth.atTime(LocalTime.MAX),
                TransactionType.EXPENSE
        );

        double monthlyNetChange = currentIncome - currentExpense;

        // Saldo actual del bolsillo
        double currentBalance = pocketPersistencePort.getBalance(pocketId, userId);

        // Cálculo de la tendencia en porcentaje
        double balanceTrendPercentage = (previousMonthlyNetChange == 0)
                ? (currentBalance != 0 ? 100.0 : 0.0)
                : ((currentBalance - previousMonthlyNetChange) / Math.abs(previousMonthlyNetChange)) * 100;

        return new PocketBalanceSummary(currentBalance, monthlyNetChange, previousMonthlyNetChange, balanceTrendPercentage);
    }


    @Override
    public List<Pocket> getPockets() {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return pocketPersistencePort.getPocketsByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(Pocket::getId))
                .toList();
    }

    @Override
    public Double getTotalBalanceByUserId() {
        return pocketPersistencePort.getTotalBalanceByUserId(authenticationPort.getAuthenticatedUserId());
    }

    @Override
    public boolean isPocketBalanceSufficient(Long pocketId, BigDecimal amount) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        Double currentBalance = pocketPersistencePort.getBalance(pocketId, userId);

        return currentBalance < amount.doubleValue();
    }

    @Override
    public void transferBetweenPockets(Long fromPocketId, Long toPocketId, BigDecimal amount) {
        Pocket fromPocket = getPocketByIdAndUserId(fromPocketId);
        Pocket toPocket = getPocketByIdAndUserId(toPocketId);
        String categoryName = "Transferencia";
        Category category = categoryServicePort.getCategoryByName(categoryName);

        if (fromPocket.getBalance().doubleValue() < amount.doubleValue()) {
            throw new InsufficientBalanceException(DomainConstants.INSUFFICIENT_BALANCE_MESSAGE);
        }

        fromPocket.setBalance(fromPocket.getBalance().subtract(amount));
        toPocket.setBalance(toPocket.getBalance().add(amount));

        pocketPersistencePort.updateBalanceById(fromPocket.getBalance(), fromPocket.getId());
        pocketPersistencePort.updateBalanceById(toPocket.getBalance(), toPocket.getId());

        Transaction fromTransaction = createNewTransaction(amount, fromPocket, toPocket, category, true);
        Transaction toTransaction = createNewTransaction(amount, fromPocket, toPocket, category, false);

        transactionServicePort.saveNewTransaction(fromTransaction);
        transactionServicePort.saveNewTransaction(toTransaction);
    }

    @Override
    public void updatePocketById(Long pocketId, Pocket pocket) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        Pocket existingPocket = getPocketByIdAndUserId(pocketId);

        existingPocket.setName(pocket.getName());
        existingPocket.setEmoji(pocket.getEmoji());
        existingPocket.setBalance(pocket.getBalance());
        existingPocket.setExcludeBalance(pocket.getExcludeBalance());
        existingPocket.setUserId(userId);

        pocketPersistencePort.saveNewPocket(existingPocket);
    }

    private static Transaction createNewTransaction(BigDecimal amount, Pocket fromPocket, Pocket toPocket, Category category, boolean isFromPocket) {
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
