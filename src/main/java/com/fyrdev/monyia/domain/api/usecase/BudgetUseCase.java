package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.adapters.driving.http.dto.response.BudgetResponse;
import com.fyrdev.monyia.domain.api.IBudgetServicePort;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.model.Budget;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.enums.Periodicity;
import com.fyrdev.monyia.domain.spi.AITextClassifierPort;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.IBudgetPersistencePort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BudgetUseCase implements IBudgetServicePort {
    private final IBudgetPersistencePort budgetPersistencePort;
    private final IAuthenticationPort authenticationPort;
    private final ICategoryServicePort categoryServicePort;

    public BudgetUseCase(IBudgetPersistencePort budgetPersistencePort,
                         IAuthenticationPort authenticationPort,
                         ICategoryServicePort categoryServicePort) {
        this.budgetPersistencePort = budgetPersistencePort;
        this.authenticationPort = authenticationPort;
        this.categoryServicePort = categoryServicePort;
    }

    @Override
    public Budget saveBudget(Budget budget) {
        String emoji = categoryServicePort.getCategoryById(budget.getCategoryId()).getDefaultEmoji();
        budget.setStartDate(LocalDateTime.now());
        budget.setStartDate(budget.getStartDate().withDayOfMonth(budget.getDayOfTheMonth()));
        budget.setBalance(budget.getAmount());
        budget.setUserId(authenticationPort.getAuthenticatedUserId());
        budget.setEmoji(emoji);

        Periodicity frequency = budget.getPeriodicity();

        if (frequency.equals(Periodicity.MONTHLY)) {
            if (budget.getStartDate().getDayOfMonth() == 1) {
                budget.setEndDate(budget.getStartDate().withDayOfMonth(budget.getStartDate().toLocalDate().lengthOfMonth()));
            } else {
                budget.setEndDate(budget.getStartDate().plusMonths(1).withDayOfMonth(budget.getDayOfTheMonth()).minusDays(1));
            }
        }

        return budgetPersistencePort.saveBudget(budget);
    }

    @Override
    public Budget updateBudgetBalance(Long categoryId, BigDecimal amount) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        Budget budget = budgetPersistencePort.getBudgetByCategoryId(categoryId, userId);

        if (budget != null) {
            budget.setBalance(budget.getBalance().subtract(amount));

            budgetPersistencePort.saveBudget(budget);
        }

        return budget;
    }

    @Override
    public List<BudgetResponse> getAllBudgets() {
        Long userId = authenticationPort.getAuthenticatedUserId();
        var budgets = budgetPersistencePort.getAllBudgetsByUserId(userId);

        return budgets.stream()
                .map(budget -> new BudgetResponse(
                        budget.getId(),
                        budget.getName(),
                        budget.getAmount(),
                        budget.getAmount().subtract(budget.getBalance()),
                        budget.getAmount().subtract(budget.getAmount().subtract(budget.getBalance())),
                        ((budget.getAmount().doubleValue() - budget.getBalance().doubleValue()) / (budget.getAmount().doubleValue()) * 100),
                        formatPeriod(budget.getStartDate(), budget.getEndDate()),
                        budget.getEmoji(),
                        categoryServicePort.getCategoryById(budget.getCategoryId()).getName()
                ))
                .toList();
    }

    @Override
    public BudgetResponse getBudgetById(Long budgetId) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        var budget = budgetPersistencePort.getBudgetById(budgetId, userId);

        return new BudgetResponse(
                budget.getId(),
                budget.getName(),
                budget.getAmount(),
                getSpentBalance(budget.getAmount(), budget.getBalance()),
                getLeftBalance(budget.getAmount(), budget.getBalance()),
                getPercentageCompleted(budget.getAmount(), budget.getBalance()),
                formatPeriod(budget.getStartDate(), budget.getEndDate()),
                budget.getEmoji(),
                categoryServicePort.getCategoryById(budget.getCategoryId()).getName()
        );
    }

    private BigDecimal getSpentBalance(BigDecimal amount, BigDecimal balance) {
        return amount.subtract(balance);
    }

    private BigDecimal getLeftBalance(BigDecimal amount, BigDecimal balance) {
        return amount.subtract(amount.subtract(balance));
    }

    private Double getPercentageCompleted(BigDecimal amount, BigDecimal balance) {
        double result = amount.doubleValue() - balance.doubleValue();

        return result / (amount.doubleValue() * 100);
    }

    private String formatPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM yyyy");

        String startDay = startDate.format(dayFormatter);
        String endDay = endDate.format(dayFormatter);
        String monthYear = endDate.format(monthYearFormatter);

        return startDay + "-" + endDay + " " + monthYear;
    }
}
