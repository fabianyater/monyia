package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.configuration.exceptionhandler.ResourceNotFoundException;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.api.IPocketServicePort;
import com.fyrdev.monyia.domain.helpers.PocketTransactionHelper;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.dto.IncomeAndExpenseSummary;
import com.fyrdev.monyia.domain.model.dto.PocketBalanceSummary;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import com.fyrdev.monyia.domain.spi.AITextClassifierPort;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.IPocketPersistencePort;
import com.fyrdev.monyia.domain.util.DomainConstants;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PocketUseCase implements IPocketServicePort {
    private final IPocketPersistencePort pocketPersistencePort;
    private final IAuthenticationPort authenticationPort;
    private final AITextClassifierPort aiTextClassifierPort;
    private final PocketTransactionHelper pocketTransactionHelper;
    private final ICategoryServicePort categoryServicePort;

    public PocketUseCase(IPocketPersistencePort pocketPersistencePort,
                         IAuthenticationPort authenticationPort,
                         AITextClassifierPort aiTextClassifierPort, PocketTransactionHelper pocketTransactionHelper, ICategoryServicePort categoryServicePort) {
        this.pocketPersistencePort = pocketPersistencePort;
        this.authenticationPort = authenticationPort;
        this.aiTextClassifierPort = aiTextClassifierPort;
        this.pocketTransactionHelper = pocketTransactionHelper;
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
        getPocketByIdAndUserId(pocketId);
        LocalDateTime startOfPreviousMonth = startDate.minusMonths(1).withDayOfMonth(1).atStartOfDay();
        LocalDateTime startOfCurrentMonth = startDate.withDayOfMonth(1).atStartOfDay();

        var previousSummary = pocketTransactionHelper.getMonthlySummary(pocketId, userId, startOfPreviousMonth);
        var currentSummary = pocketTransactionHelper.getMonthlySummary(pocketId, userId, startOfCurrentMonth);

        BigDecimal previousIncome = getAmountFromType(previousSummary, TransactionType.INCOME);
        BigDecimal previousExpense = getAmountFromType(previousSummary, TransactionType.EXPENSE);
        BigDecimal previousMonthlyNetChange = previousIncome.subtract(previousExpense);

        BigDecimal currentIncome = getAmountFromType(currentSummary, TransactionType.INCOME);
        BigDecimal currentExpense = getAmountFromType(currentSummary, TransactionType.EXPENSE);
        BigDecimal monthlyNetChange = currentIncome.subtract(currentExpense);

        BigDecimal currentBalance = pocketPersistencePort.getBalance(pocketId, userId);

        double balanceTrendPercentage = (previousMonthlyNetChange.doubleValue() == 0)
                ? (currentBalance.doubleValue() != 0 ? 100.0 : 0.0)
                : ((currentBalance.doubleValue() - previousMonthlyNetChange.doubleValue()) / Math.abs(previousMonthlyNetChange.doubleValue())) * 100;

        return new PocketBalanceSummary(currentBalance, monthlyNetChange, previousMonthlyNetChange, BigDecimal.valueOf(balanceTrendPercentage));
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
        BigDecimal currentBalance = pocketPersistencePort.getBalance(pocketId, userId);

        return currentBalance.doubleValue() < amount.doubleValue();
    }

    @Override
    public void transferBetweenPockets(Long fromPocketId, Long toPocketId, BigDecimal amount) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        String categoryName = "Transferencia";
        Category category = categoryServicePort.getCategoryByName(categoryName);
        pocketTransactionHelper.transferBetweenPockets(fromPocketId, toPocketId, amount, userId, category);
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

    @Override
    public void updateBalanceById(BigDecimal balance, Long pocketId) {
        pocketPersistencePort.updateBalanceById(balance, pocketId);
    }

    @Override
    @Scheduled(cron = "0 1 00 1 * ?", zone = "America/Bogota")
    public void makeTransactionWithPreviousBalance() {
        Category category = categoryServicePort.getCategory("Ingreso");
        pocketTransactionHelper.makeTransactionWithPreviousBalance(category.getId());
    }

    public static BigDecimal getAmountFromType(List<IncomeAndExpenseSummary> previousIncomeAndExpenseValues, TransactionType type) {
        Optional<IncomeAndExpenseSummary> result = previousIncomeAndExpenseValues.stream()
                .filter(incomeAndExpenseSummary -> incomeAndExpenseSummary
                        .type().equals(type.name()))
                .findFirst();

        return result.isPresent() ? result.get().amount() : BigDecimal.ZERO;
    }
}
